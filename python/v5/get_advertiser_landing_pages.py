#!/usr/bin/python
#
# Copyright 2017 Google Inc. All Rights Reserved.
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

"""This example lists all landing pages for the specified advertiser."""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id',
    type=int,
    help='The ID of the profile to look up advertiser landing pages for')
argparser.add_argument(
    'advertiser_id',
    type=int,
    help='The ID of the advertiser to look up landing pages for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = flags.advertiser_id

  try:
    # Construct the request.
    request = service.advertiserLandingPages().list(
        profileId=str(profile_id), advertiserIds=[advertiser_id]
    )

    while True:
      # Execute request and print response.
      response = request.execute()

      for landing_page in response['landingPages']:
        print('Found advertiser landing page with ID %s and name "%s".' %
              (landing_page['id'], landing_page['name']))

      if response['landingPages'] and response['nextPageToken']:
        request = service.advertiserLandingPages().list_next(request, response)
      else:
        break

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
