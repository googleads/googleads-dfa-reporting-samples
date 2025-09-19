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

"""This example creates a floodlight activity in a given activity group.

To create an activity group, run create_floodlight_activity_group.py.
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
    'activity_group_id', type=int,
    help='The ID of the floodlight activity group to create an activity for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  activity_group_id = flags.activity_group_id

  try:
    # Construct and save floodlight activity.
    floodlight_activity = {
        'countingMethod': 'STANDARD_COUNTING',
        'expectedUrl': 'http://www.google.com',
        'floodlightActivityGroupId': activity_group_id,
        'floodlightTagType': 'GLOBAL_SITE_TAG',
        'name': 'Test Floodlight Activity',
        'conversionCategory': 'CONVERSION_CATEGORY_PAGE_VIEW',
    }

    request = service.floodlightActivities().insert(
        profileId=str(profile_id), body=floodlight_activity
    )

    # Execute request and print response.
    response = request.execute()

    print('Created floodlight activity with ID %s and name "%s".'
          % (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
