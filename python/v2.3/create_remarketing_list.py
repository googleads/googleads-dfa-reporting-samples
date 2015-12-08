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

"""Creates a remarketing list for a given advertiser and floodlight activity.

Note: this sample assumes that the floodlight activity specified has a U1 custom
floodlight variable.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add a remarketing list for')
argparser.add_argument(
    'advertiser_id', type=int,
    help='The ID of the advertiser to add a remarketing list for')
argparser.add_argument(
    'activity_id', type=int,
    help='The ID of the floodlight activity to add a remarketing list for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = flags.advertiser_id
  activity_id = flags.activity_id

  try:
    # Create a list population term.
    # This term matches all visitors with a U1 value exactly matching
    # "test_value"
    term = {
        'operator': 'STRING_EQUALS',
        'type': 'CUSTOM_VARIABLE_TERM',
        'value': 'test_value',
        'variableName': 'U1'
    }

    # Add the term to a clause and the clause to a population rule.
    # This rule will target all visitors who trigger the specified floodlight
    # activity and satisfy the custom rule defined in the list population term.
    rule = {
        'floodlightActivityId': activity_id,
        'listPopulationClauses': {'terms': [term]}
    }

    # Create the remarketing list.
    list = {
        'name': 'Test Remarketing List',
        'active': 'true',
        'advertiserId': advertiser_id,
        'lifeSpan': 30,
        'listPopulationRule': rule
    }

    request = service.remarketingLists().insert(
        profileId=profile_id, body=list)

    # Execute request and print response.
    response = request.execute()

    print ('Created remarketing list with ID %s and name "%s."'
           % (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
