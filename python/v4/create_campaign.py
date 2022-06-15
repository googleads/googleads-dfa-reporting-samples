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

"""This example creates a campaign in a given advertiser.

To create an advertiser, run create_advertiser.py.
"""

import argparse
import sys
import uuid

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int, help='The ID of the profile to add a campaign for')
argparser.add_argument(
    'advertiser_id',
    type=int,
    help='The ID of the advertiser to associate the campaign with')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = flags.advertiser_id

  try:
    # Locate an advertiser landing page to use as a default.
    default_landing_page = get_advertiser_landing_page(service, profile_id,
                                                       advertiser_id)

    # Construct and save campaign.
    campaign = {
        'name': 'Test Campaign #%s' % uuid.uuid4(),
        'advertiserId': advertiser_id,
        'archived': 'false',
        'defaultLandingPageId': default_landing_page['id'],
        'startDate': '2015-01-01',
        'endDate': '2020-01-01'
    }

    request = service.campaigns().insert(profileId=profile_id, body=campaign)

    # Execute request and print response.
    response = request.execute()

    print('Created campaign with ID %s and name "%s".' % (response['id'],
                                                          response['name']))

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


def get_advertiser_landing_page(service, profile_id, advertiser_id):
  # Retrieve a single landing page from the specified advertiser.
  response = service.advertiserLandingPages().list(
      profileId=profile_id, advertiserIds=[advertiser_id],
      maxResults=1).execute()

  if not response['landingPages']:
    sys.exit(
        'No landing pages found for advertiser with ID %d.' % advertiser_id)

  return response['landingPages'][0]


if __name__ == '__main__':
  main(sys.argv)
