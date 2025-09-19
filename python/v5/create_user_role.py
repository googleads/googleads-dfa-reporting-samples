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

"""This example creates a user role in a given DFA account.

To get the account ID, run get_all_userprofiles.py. To get the parent user role
ID, run get_user_roles.py.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add a user role for')
argparser.add_argument(
    'account_id', type=int,
    help='The ID of the subaccount to create a user role for')
argparser.add_argument(
    'parent_role_id', type=int,
    help='The ID of the parent user role to assign to this user role')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  account_id = flags.account_id
  parent_role_id = flags.parent_role_id

  try:
    # Construct the basic user role structure.
    user_role = {
        'name': 'Test User Role',
        'accountId': account_id,
        'parentUserRoleId': parent_role_id
    }

    request = service.userRoles().insert(
        profileId=str(profile_id), body=user_role
    )

    # Execute request and print response.
    response = request.execute()

    print('Created user role with ID %s and name "%s".'
          % (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
