#!/usr/bin/python
#
# Copyright 2018 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""An end-to-end example of how to find and run a report."""

import argparse
import random
import sys
import time

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int, help='The ID of the profile to use')

# The following values control retry behavior while the report is processing.
# Minimum amount of time between polling requests. Defaults to 10 seconds.
MIN_RETRY_INTERVAL = 10
# Maximum amount of time between polling requests. Defaults to 10 minutes.
MAX_RETRY_INTERVAL = 10 * 60
# Maximum amount of time to spend polling. Defaults to 1 hour.
MAX_RETRY_ELAPSED_TIME = 60 * 60


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id

  try:
    # 1. Find a report to run.
    report = find_report(service, profile_id)

    if report:
      # 2. Run the report.
      report_file = run_report(service, profile_id, report['id'])

      # 3. Wait for the report file to be ready.
      wait_for_report_file(service, report['id'], report_file['id'])
    else:
      print('No report found for profile ID %d.\n' % profile_id)

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


def find_report(service, profile_id):
  """Finds a report to run."""
  target = None

  # Construct the request.
  request = service.reports().list(profileId=str(profile_id))

  while True:
    response = request.execute()

    for report in response['items']:
      if is_target_report(report):
        target = report
        break

    if not target and response['items'] and response['nextPageToken']:
      request = service.reports().list_next(request, response)
    else:
      break

  if target:
    print('Found report %s with name "%s".' % (target['id'], target['name']))
    return target

  print('Unable to find report for profile ID %d.' % profile_id)
  return None


def is_target_report(report):
  # Provide custom validation logic here.
  # For example purposes, any report is considered valid.
  return report is not None


def run_report(service, profile_id, report_id):
  """Runs the report."""
  # Run the report.
  report_file = (
      service.reports()
      .run(profileId=str(profile_id), reportId=str(report_id))
      .execute()
  )

  print(
      'Running report %s, current file status is %s.'
      % (report_id, report_file['status'])
  )
  return report_file


def wait_for_report_file(service, report_id, file_id):
  """Waits for the report file to finish processing."""
  # Wait for the report file to finish processing.
  # An exponential backoff strategy is used to conserve request quota.
  sleep = 0
  start_time = time.time()
  while True:
    report_file = (
        service.files()
        .get(reportId=str(report_id), fileId=str(file_id))
        .execute()
    )

    status = report_file['status']
    if status == 'REPORT_AVAILABLE':
      print('File status is %s, ready to download.' % status)
      return
    elif status != 'PROCESSING':
      print('File status is %s, processing failed.' % status)
      return
    elif time.time() - start_time > MAX_RETRY_ELAPSED_TIME:
      print('File processing deadline exceeded.')
      return

    sleep = next_sleep_interval(sleep)
    print('File status is %s, sleeping for %d seconds.' % (status, sleep))
    time.sleep(sleep)


def next_sleep_interval(previous_sleep_interval):
  """Calculates the next sleep interval based on the previous."""
  min_interval = previous_sleep_interval or MIN_RETRY_INTERVAL
  max_interval = previous_sleep_interval * 3 or MIN_RETRY_INTERVAL
  return min(MAX_RETRY_INTERVAL, random.randint(min_interval, max_interval))


if __name__ == '__main__':
  main(sys.argv)
