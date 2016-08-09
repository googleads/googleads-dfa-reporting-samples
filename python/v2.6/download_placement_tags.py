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

"""This example downloads HTML Tags for a given campaign and placement ID.

To create campaigns, run create_campaign.py. To create placements, run
create_placement.py.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to download tags for')
argparser.add_argument(
    'campaign_id', type=int,
    help='The ID of the campaign to download tags for')
argparser.add_argument(
    'placement_id', type=int,
    help='The ID of the placement to download tags for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  campaign_id = flags.campaign_id
  placement_id = flags.placement_id

  try:
    # Construct the request.
    request = service.placements().generatetags(
        profileId=profile_id, campaignId=campaign_id,
        placementIds=[placement_id])

    # Execute request and print response.
    response = request.execute()

    for placement_tag in response['placementTags']:
      print_placement_tag(placement_tag)

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def print_placement_tag(placement_tag):
  for tag_data in placement_tag['tagDatas']:
    print ('%s - %s\n'
           % (placement_tag['placementId'], tag_data['format']))

    if 'impressionTag' in tag_data:
      print '%s\n\n' % (tag_data['impressionTag'])
    if 'clickTag' in tag_data:
      print '%s\n\n' % (tag_data['clickTag'])


if __name__ == '__main__':
  main(sys.argv)
