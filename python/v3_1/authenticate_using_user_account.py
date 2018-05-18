#!/usr/bin/python
#
# Copyright 2017 Google Inc. All Rights Reserved.
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

"""This example demonstrates how to authenticate using a user account.

Utilizes the OAuth 2.0 installed application flow.
"""

import argparse
import sys

from googleapiclient import discovery
import httplib2
from oauth2client import client
from oauth2client import tools
from oauth2client.file import Storage

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'json_file',
    help='Path to the JSON file to use for authenticating.')

# Filename used for the credential store.
CREDENTIAL_STORE_FILE = 'auth-sample.dat'

# The OAuth 2.0 scopes to request.
OAUTH_SCOPES = ['https://www.googleapis.com/auth/dfareporting']


def main(argv):
  # Retrieve command line arguments.
  parser = argparse.ArgumentParser(
      description=__doc__,
      formatter_class=argparse.RawDescriptionHelpFormatter,
      parents=[tools.argparser, argparser])
  flags = parser.parse_args(argv[1:])

  # Authenticate using the supplied user account credentials
  http = authenticate_using_user_account(flags)

  # Construct a service object via the discovery service.
  service = discovery.build('dfareporting', 'v3.1', http=http)

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


def authenticate_using_user_account(flags):
  """Authorizes an httplib2.Http instance using user account credentials."""
  # Set up a Flow object to be used if we need to authenticate.
  flow = client.flow_from_clientsecrets(flags.json_file, scope=OAUTH_SCOPES)

  # Check whether credentials exist in the credential store. Using a credential
  # store allows auth credentials to be cached, so they survive multiple runs
  # of the application. This avoids prompting the user for authorization every
  # time the access token expires, by remembering the refresh token.
  storage = Storage(CREDENTIAL_STORE_FILE)
  credentials = storage.get()

  # If no credentials were found, go through the authorization process and
  # persist credentials to the credential store.
  if credentials is None or credentials.invalid:
    credentials = tools.run_flow(flow, storage, flags)

  # Use the credentials to authorize an httplib2.Http instance.
  http = credentials.authorize(httplib2.Http())

  return http


if __name__ == '__main__':
  main(sys.argv)
