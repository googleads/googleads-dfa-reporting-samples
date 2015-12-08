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

"""This example assigns a given creative to a given campaign.

Note that both the creative and campaign must be associated with the same
advertiser.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to create this association as')
argparser.add_argument(
    'campaign_id', type=int,
    help='The ID of the campaign to associate the creative with')
argparser.add_argument(
    'creative_id', type=int,
    help='The ID of the creative to be associated')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  campaign_id = flags.campaign_id
  creative_id = flags.creative_id

  try:
    # Construct the request.
    association = {
        'creativeId': creative_id
    }

    request = service.campaignCreativeAssociations().insert(
        profileId=profile_id, campaignId=campaign_id, body=association)

    # Execute request and print response.
    response = request.execute()

    print ('Creative with ID %s is now associated with campaign %s.'
           % (response['creativeId'], campaign_id))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
