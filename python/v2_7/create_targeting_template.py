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

"""This example creates a targeting template associated with a given advertiser.

To get an advertiser ID, run get_advertisers.py.
"""

import argparse
import sys
import uuid

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add a targeting template for')
argparser.add_argument(
    'advertiser_id', type=int,
    help='The ID of the advertiser to add a targeting template for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = flags.advertiser_id

  try:
    # Construct a basic targeting template.
    # This template will be configured to serve ads on Monday, Wednesday, and
    # Friday from 9-10am and 3-5pm.
    # Note: targeting template names must be unique within an advetiser.
    targeting_template = {
        'advertiserId': advertiser_id,
        'dayPartTargeting': {
            'daysOfWeek': ['MONDAY', 'WEDNESDAY', 'FRIDAY'],
            'hoursOfDay': [9, 15, 16],
            'userLocalTime': True
        },
        'name': 'Test Targeting Template #%s' % uuid.uuid4()
    }

    request = service.targetingTemplates().insert(profileId=profile_id,
                                                  body=targeting_template)

    # Execute request and print response.
    response = request.execute()

    print ('Created targeting template with ID %s and name "%s".'
           % (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
