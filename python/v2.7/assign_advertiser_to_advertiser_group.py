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

"""This example assigns an advertiser to an advertiser group.

CAUTION: An advertiser that has campaigns associated with it cannot be
removed from an advertiser group once assigned.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add advertiser groups for')
argparser.add_argument(
    'advertiser_group_id', type=int,
    help='The ID of the advertiser group to add to')
argparser.add_argument(
    'advertiser_id', type=int,
    help='The ID of the advertiser to add')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_group_id = flags.advertiser_group_id
  advertiser_id = flags.advertiser_id

  try:
    # Construct the request.
    advertiser = {
        'advertiserGroupId': advertiser_group_id
    }

    request = service.advertisers().patch(
        profileId=profile_id, id=advertiser_id, body=advertiser)

    # Execute request and print response.
    response = request.execute()

    print ('Assigned advertiser with ID %s to advertiser group with ID %s.'
           % (response['id'], response['advertiserGroupId']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
