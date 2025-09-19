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

"""This example creates an agency paid regular placement in a given campaign.

Requires the DFA site ID and campaign ID in which the placement will be created
into. To create a campaign, run create_campaign.py. To get DFA site ID, run
get_site.py.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add a placement for')
argparser.add_argument(
    'campaign_id', type=int,
    help='The ID of the campaign to associate the placement with')
argparser.add_argument(
    'site_id', type=int,
    help='The ID of the site to associate the placement with')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  campaign_id = flags.campaign_id
  site_id = flags.site_id

  try:
    # Look up the campaign
    campaign = (
        service.campaigns()
        .get(profileId=str(profile_id), id=str(campaign_id))
        .execute()
    )

    # Construct and save placement.
    placement = {
        'name': 'Test Placement',
        'campaignId': campaign_id,
        'compatibility': 'DISPLAY',
        'siteId': site_id,
        'size': {
            'height': '1',
            'width': '1'
        },
        'paymentSource': 'PLACEMENT_AGENCY_PAID',
        'tagFormats': ['PLACEMENT_TAG_STANDARD']
    }

    # Set the pricing schedule for the placement.
    placement['pricingSchedule'] = {
        'startDate': campaign['startDate'],
        'endDate': campaign['endDate'],
        'pricingType': 'PRICING_TYPE_CPM'
    }

    request = service.placements().insert(
        profileId=str(profile_id), body=placement
    )

    # Execute request and print response.
    response = request.execute()

    print('Created placement with ID %s and name "%s".'
          % (long(response['id']), response['name']))

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
