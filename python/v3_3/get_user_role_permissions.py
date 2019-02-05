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

"""This example displays all of the available subaccount permissions.

To get a subaccount ID, run get_subaccounts.py.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to list permissions for')
argparser.add_argument(
    'subaccount_id', type=int,
    help='The ID of the subaccount to list permissions for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  subaccount_id = flags.subaccount_id

  try:
    # Construct and execute the subaccount request.
    request = service.subaccounts().get(
        profileId=profile_id, id=subaccount_id)

    subaccount = request.execute()

    # Construct the user role permissions request.
    request = service.userRolePermissions().list(
        profileId=profile_id, ids=subaccount['availablePermissionIds'])

    # Execute request and print response.
    result = request.execute()

    for permission in result['userRolePermissions']:
      print ('Found user role permission with ID %s and name "%s".'
             % (permission['id'], permission['name']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
