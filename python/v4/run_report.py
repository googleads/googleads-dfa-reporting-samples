#!/usr/bin/python
#
# Copyright 2015 Google Inc. All Rights Reserved.
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

"""This example illustrates how to run a report."""

import argparse
import random
import sys
import time

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to use')
argparser.add_argument(
    'report_id', type=int,
    help='The ID of the report to run')

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
  report_id = flags.report_id

  try:
    # Run the report.
    report_file = service.reports().run(profileId=profile_id,
                                        reportId=report_id).execute()
    print 'File with ID %s has been created.' % report_file['id']

    # Wait for the report file to finish processing.
    # An exponential backoff strategy is used to conserve request quota.
    sleep = 0
    start_time = time.time()
    while True:
      report_file = service.files().get(reportId=report_id,
                                        fileId=report_file['id']).execute()

      status = report_file['status']
      if status == 'REPORT_AVAILABLE':
        print 'File status is %s, ready to download.' % status
        return
      elif status != 'PROCESSING':
        print 'File status is %s, processing failed.' % status
        return
      elif time.time() - start_time > MAX_RETRY_ELAPSED_TIME:
        print 'File processing deadline exceeded.'
        return

      sleep = next_sleep_interval(sleep)
      print 'File status is %s, sleeping for %d seconds.' % (status, sleep)
      time.sleep(sleep)

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def next_sleep_interval(previous_sleep_interval):
  """Calculates the next sleep interval based on the previous."""
  min_interval = previous_sleep_interval or MIN_RETRY_INTERVAL
  max_interval = previous_sleep_interval * 3 or MIN_RETRY_INTERVAL
  return min(MAX_RETRY_INTERVAL, random.randint(min_interval, max_interval))


if __name__ == '__main__':
  main(sys.argv)
