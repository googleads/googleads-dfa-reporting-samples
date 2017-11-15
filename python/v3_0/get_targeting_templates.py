#!/usr/bin/python
#
# Copyright 2016 Google Inc. All Rights Reserved.
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

"""This example retrieves all targeting templates for your DCM user profile.

Displays name, ID, and advertiser ID for each targeting template found.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to look up targeting templates for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id

  try:
    # Limit the fields returned
    fields = 'nextPageToken,targetingTemplates(advertiserId,id,name)'

    # Construct the request.
    request = service.targetingTemplates().list(profileId=profile_id,
                                                fields=fields)

    while True:
      # Execute request and print response.
      response = request.execute()

      for template in response['targetingTemplates']:
        print ('Found targeting template with ID %s and name "%s" associated '
               'with advertiser ID %s.' % (template['id'], template['name'],
                                           template['advertiserId']))

      if response['targetingTemplates'] and response['nextPageToken']:
        request = service.targetingTemplates().list_next(request, response)
      else:
        break

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
