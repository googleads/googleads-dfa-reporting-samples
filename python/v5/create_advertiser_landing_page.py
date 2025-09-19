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

"""This example creates an advertiser landing page."""

import argparse
import sys
import uuid

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id',
    type=int,
    help='The ID of the profile to add an advertiser landing page for')
argparser.add_argument(
    'advertiser_id',
    type=int,
    help='The ID of the advertiser to associate the landing page with')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = flags.advertiser_id

  try:
    # Construct a landing page object.
    landing_page = {
        'name': 'Test Landing Page #%s' % uuid.uuid4(),
        'advertiserId': advertiser_id,
        'archived': 'false',
        'url': 'https://www.google.com'
    }

    request = service.advertiserLandingPages().insert(
        profileId=str(profile_id), body=landing_page
    )

    # Execute request and print response.
    response = request.execute()

    print('Created advertiser landing page with ID %s and name "%s".' %
          (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
