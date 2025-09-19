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

"""This example illustrates how to get a list of all the files for a report."""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to use')
argparser.add_argument(
    'report_id', type=int,
    help='The ID of the report to list files for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  report_id = flags.report_id

  try:
    # Construct a get request for the specified report.
    request = (
        service.reports()
        .files()
        .list(profileId=str(profile_id), reportId=str(report_id))
    )

    while True:
      # Execute request and print response.
      response = request.execute()

      for report_file in response['items']:
        print('Report file with ID %s and file name "%s" has status %s.'
              % (report_file['id'], report_file['fileName'],
                 report_file['status']))

      if response['items'] and response['nextPageToken']:
        request = service.reports().files().list_next(request, response)
      else:
        break
  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
