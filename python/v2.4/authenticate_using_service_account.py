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

"""This example shows how to authenticate using a service account.

"""

import argparse
import sys

from apiclient import discovery
import dfareporting_utils
import httplib2
from oauth2client import client
from oauth2client import tools
from oauth2client.service_account import ServiceAccountCredentials

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'service_account_email',
    help='Email address of the service account to authenticate as.')
argparser.add_argument(
    'impersonation_user_email',
    help='Email address of the user to impersonate.')
argparser.add_argument(
    'p12_file',
    help='Path to the P12 file to use for authenticating.')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate using the supplied service account credentials
  http_auth = authenticate_using_service_account(
      flags.service_account_email, flags.impersonation_user_email,
      flags.p12_file)

  # Construct a service object via the discovery service.
  service = discovery.build(dfareporting_utils.API_NAME,
                            dfareporting_utils.API_VERSION, http=http_auth)

  try:
    # Construct the request.
    request = service.userProfiles().list()

    # Execute request and print response.
    response = request.execute()

    for profile in response['items']:
      print ('Found user profile with ID %s and user name "%s".'
             % (profile['profileId'], profile['userName']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def parse_arguments(argv):
  """Parses command line arguments."""
  parser = argparse.ArgumentParser(
      description=__doc__,
      formatter_class=argparse.RawDescriptionHelpFormatter,
      parents=[tools.argparser, argparser])

  return parser.parse_args(argv[1:])


def authenticate_using_service_account(
    service_account_email, impersonation_user_email, p12_file):
  """Authorizes an Http instance using service account credentials."""
  credentials = ServiceAccountCredentials.from_p12_keyfile(
      service_account_email, p12_file, scopes=dfareporting_utils.API_SCOPES)

  # Delegate domain-wide authority.
  delegated_credentials = credentials.create_delegated(impersonation_user_email)

  return delegated_credentials.authorize(httplib2.Http())

if __name__ == '__main__':
  main(sys.argv)
