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

"""Shares an existing remarketing list with the specified advertiser."""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to share remarketing lists as')
argparser.add_argument(
    'advertiser_id', type=int,
    help='The ID of the advertiser to share the remarketing list with')
argparser.add_argument(
    'remarketing_list_id', type=int,
    help='The ID of the remarketing list to share')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = str(flags.advertiser_id)
  list_id = flags.remarketing_list_id

  try:
    # Load the existing share info.
    share = service.remarketingListShares().get(
        profileId=profile_id, remarketingListId=list_id).execute()

    share['sharedAdvertiserIds'] = share.get('sharedAdvertiserIds', [])

    if advertiser_id not in share['sharedAdvertiserIds']:
      share['sharedAdvertiserIds'].append(advertiser_id)

      # Update the share info with the newly added advertiser ID.
      response = service.remarketingListShares().update(
          profileId=profile_id, body=share).execute()

      print ('Remarketing list %s is now shared to advertiser ID(s): %s.'
             % (list_id, ','.join(response['sharedAdvertiserIds'])))
    else:
      print ('Remarketing list %s is already shared to advertiser %s'
             % (list_id, advertiser_id))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
