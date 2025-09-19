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

"""This example downloads activity tags for a given floodlight activity."""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int, help='The ID of the profile to download tags for')
argparser.add_argument(
    'activity_id',
    type=int,
    help='The ID of the floodlight activity to download tags for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  activity_id = flags.activity_id

  try:
    # Construct the request.
    request = service.floodlightActivities().generatetag(
        profileId=str(profile_id), floodlightActivityId=str(activity_id)
    )

    # Execute request and print response.
    response = request.execute()

    if 'globalSiteTagGlobalSnippet' in response:
      # Print both global snippet and event snippet.
      print('Global site tag global snippet:\n\n%s' %
            response['globalSiteTagGlobalSnippet'])
      print('\n\nGlobal site tag event snippet:\n\n%s' %
            response['floodlightActivityTag'])
    else:
      # Print the Floodlight activity tag.
      print('Floodlight activity tag:\n\n%s' %
            response['floodlightActivityTag'])

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
