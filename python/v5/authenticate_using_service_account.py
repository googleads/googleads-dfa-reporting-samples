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

"""This example demonstrates how to authenticate using a service account.

An optional Google account email to impersonate may be specified as follows:
    authenticate_using_service_account.py <path_to_json_file> -i <email>

This optional flag only applies to service accounts which have domain-wide
delegation enabled and wish to make API requests on behalf of an account
within that domain. Using this flag will not allow you to impersonate a
user from a domain you don't own (e.g., gmail.com).
"""

import argparse
import sys

from googleapiclient import discovery
import httplib2
from oauth2client import client
from oauth2client import tools
from oauth2client.service_account import ServiceAccountCredentials

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'path_to_service_account_json_file',
    help='Path to the service account JSON file to use for authenticating.')
argparser.add_argument(
    '-i',
    '--impersonation_email',
    help='Google account email to impersonate.')

# The OAuth 2.0 scopes to request.
OAUTH_SCOPES = ['https://www.googleapis.com/auth/dfareporting']


def main(argv):
  # Retrieve command line arguments.
  parser = argparse.ArgumentParser(
      description=__doc__,
      formatter_class=argparse.RawDescriptionHelpFormatter,
      parents=[tools.argparser, argparser])
  flags = parser.parse_args(argv[1:])

  # Authenticate using the supplied service account credentials
  http = authenticate_using_service_account(
      flags.path_to_service_account_json_file,
      flags.impersonation_email)

  # Construct a service object via the discovery service.
  service = discovery.build('dfareporting', 'v5', http=http)

  try:
    # Construct the request.
    request = service.userProfiles().list()

    # Execute request and print response.
    response = request.execute()

    for profile in response['items']:
      print('Found user profile with ID %s and user name "%s".' %
            (profile['profileId'], profile['userName']))

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


def authenticate_using_service_account(path_to_service_account_json_file,
                                       impersonation_email):
  """Authorizes an httplib2.Http instance using service account credentials."""
  # Load the service account credentials from the specified JSON keyfile.
  credentials = ServiceAccountCredentials.from_json_keyfile_name(
      path_to_service_account_json_file,
      scopes=OAUTH_SCOPES)

  # Configure impersonation (if applicable).
  if impersonation_email:
    credentials = credentials.create_delegated(impersonation_email)

  # Use the credentials to authorize an httplib2.Http instance.
  http = credentials.authorize(httplib2.Http())

  return http


if __name__ == '__main__':
  main(sys.argv)
