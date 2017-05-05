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

"""Handles common tasks across all API samples."""

import argparse
import os

from googleapiclient import discovery
import httplib2
from oauth2client import client
from oauth2client import file as oauthFile
from oauth2client import tools

API_NAME = 'dfareporting'
API_VERSION = 'v2.6'
API_SCOPES = ['https://www.googleapis.com/auth/dfareporting',
              'https://www.googleapis.com/auth/dfatrafficking',
              'https://www.googleapis.com/auth/ddmconversions']

# Filename used for the credential store.
CREDENTIAL_STORE_FILE = API_NAME + '.dat'


def get_arguments(argv, desc, parents=None):
  """Validates and parses command line arguments.

  Args:
    argv: list of strings, the command-line parameters of the application.
    desc: string, a description of the sample being executed.
    parents: list of argparse.ArgumentParser, additional command-line parsers.

  Returns:
    The parsed command-line arguments.
  """
  # Include the default oauth2client argparser
  parent_parsers = [tools.argparser]

  if parents:
    parent_parsers.extend(parents)

  parser = argparse.ArgumentParser(
      description=desc,
      formatter_class=argparse.RawDescriptionHelpFormatter,
      parents=parent_parsers)
  return parser.parse_args(argv[1:])


def setup(flags):
  """Handles authentication and loading of the API.

  Args:
    flags: command-line flags obtained by calling ''get_arguments()''.

  Returns:
    An initialized service object.
  """
  # Name of a file containing the OAuth 2.0 information for this
  # application, including client_id and client_secret, which are found
  # on the Credentials tab on the Google Developers Console.
  client_secrets = os.path.join(os.path.dirname(__file__),
                                'client_secrets.json')

  # Set up a Flow object to be used if we need to authenticate.
  flow = client.flow_from_clientsecrets(
      client_secrets,
      scope=API_SCOPES,
      message=tools.message_if_missing(client_secrets))

  # Prepare credentials, and authorize HTTP object with them.
  # If the credentials don't exist or are invalid run through the installed
  # client flow. The Storage object will ensure that if successful the good
  # credentials will get written back to a file.
  storage = oauthFile.Storage(CREDENTIAL_STORE_FILE)
  credentials = storage.get()
  if credentials is None or credentials.invalid:
    credentials = tools.run_flow(flow, storage, flags)
  http = credentials.authorize(http=httplib2.Http())

  # Construct and return a service object via the discovery service.
  return discovery.build(API_NAME, API_VERSION, http=http)
