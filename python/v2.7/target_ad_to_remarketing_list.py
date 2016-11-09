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

"""This example targets an ad to a remarketing list.

The first targetable remarketing list, either owned by or shared to the ad's
advertiser, will be used. To create a remarketing list, see
create_remarketing_list.py. To share a remarketing list with the ad's
advertiser, see share_remarketing_list_to_advertiser.py.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to use for targeting')
argparser.add_argument('ad_id', type=int, help='The ID of the ad to target')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  ad_id = flags.ad_id

  try:
    # Retrieve the ad.
    ad = service.ads().get(profileId=profile_id, id=ad_id).execute()

    # Retrieve a single targetable remarketing list for the ad.
    lists = service.targetableRemarketingLists().list(
        profileId=profile_id, advertiserId=ad['advertiserId'],
        maxResults=1).execute()

    if lists['targetableRemarketingLists']:
      list = lists['targetableRemarketingLists'][0]

      # Update the ad with a list targeting expression
      ad['remarketing_list_expression'] = { 'expression': list['id'] }
      response = service.ads().update(profileId=profile_id, body=ad).execute()

      print ('Ad %s updated to use remarketing list expression: "%s".'
            % (response['id'],
            response['remarketing_list_expression']['expression']))
  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
