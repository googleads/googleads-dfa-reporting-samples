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

"""This example creates a rotation group ad in a given campaign."""

import argparse
import sys
import time

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add a rotation group for')
argparser.add_argument(
    'campaign_id', type=int,
    help='The ID of the campaign to add a rotation group for')
argparser.add_argument(
    'placement_id', type=int,
    help='The ID of the placement to add a rotation group for')
argparser.add_argument(
    'creative_id', type=int,
    help='The ID of the creative to add to the rotation group')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  campaign_id = flags.campaign_id
  placement_id = flags.placement_id
  creative_id = flags.creative_id

  try:
    # Retrieve the campaign (to get end date).
    campaign = service.campaigns().get(
        profileId=profile_id, id=campaign_id).execute()

    # Construct creative assignment.
    creative_assignment = {
        'active': 'true',
        'creativeId': creative_id,
        'clickThroughUrl': {
            'defaultLandingPage': 'true'
        }
    }

    # Construct placement assignment.
    placement_assignment = {
        'active': 'true',
        'placementId': placement_id,
    }

    # Construct creative rotation.
    creative_rotation = {
        'creativeAssignments': [creative_assignment],
        'type': 'CREATIVE_ROTATION_TYPE_RANDOM',
        'weightCalculationStrategy': 'WEIGHT_STRATEGY_OPTIMIZED'
    }

    # Construct delivery schedule.
    delivery_schedule = {
        'impressionRatio': '1',
        'priority': 'AD_PRIORITY_01'
    }

    # Construct and save ad.
    ad = {
        'active': 'true',
        'campaignId': campaign_id,
        'creativeRotation': creative_rotation,
        'deliverySchedule': delivery_schedule,
        'endTime': '%sT00:00:00Z' % campaign['endDate'],
        'name': 'Test Rotation Group',
        'placementAssignments': [placement_assignment],
        'startTime': '%sT23:59:59Z' % time.strftime('%Y-%m-%d'),
        'type': 'AD_SERVING_STANDARD_AD'
    }

    request = service.ads().insert(profileId=profile_id, body=ad)

    # Execute request and print response.
    response = request.execute()

    print ('Created rotation group with ID %s and name "%s".'
           % (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
