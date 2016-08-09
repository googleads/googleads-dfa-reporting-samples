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

"""Inserts an offline conversion attributed to an encrypted user ID."""

import argparse
import sys
import time

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to add a placement for')
argparser.add_argument(
    'encrypted_user_id',
    help='The encrypted user ID to attribute the conversion to')
argparser.add_argument(
    'encryption_source',
    help='The source of the encrypted user ID')
argparser.add_argument(
    'encryption_entity_id', type=int,
    help='The ID of the entity used to encrypt the supplied user ID')
argparser.add_argument(
    'encryption_entity_type',
    help='The type of the entity used to encrypt the supplied user ID')
argparser.add_argument(
    'floodlight_activity_id', type=int,
    help='The ID of Floodlight activity this conversion is associated with')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  encrypted_user_id = flags.encrypted_user_id
  encryption_entity_id = flags.encryption_entity_id
  encryption_entity_type = flags.encryption_entity_type
  encryption_source = flags.encryption_source
  floodlight_activity_id = flags.floodlight_activity_id

  try:
    # Look up the Floodlight configuration ID based on activity ID.
    floodlight_activity = service.floodlightActivities().get(
        profileId=profile_id, id=floodlight_activity_id).execute()
    floodlight_config_id = floodlight_activity['floodlightConfigurationId']

    current_time_in_micros = int(time.time() * 1000000)

    # Construct the conversion.
    conversion = {
        'encryptedUserId': encrypted_user_id,
        'floodlightActivityId': floodlight_activity_id,
        'floodlightConfigurationId': floodlight_config_id,
        'ordinal': current_time_in_micros,
        'timestampMicros': current_time_in_micros
    }

    # Construct the encryption info.
    encryption_info = {
        'encryptionEntityId': encryption_entity_id,
        'encryptionEntityType': encryption_entity_type,
        'encryptionSource': encryption_source
    }

    # Insert the conversion.
    request_body = {
        'conversions': [conversion],
        'encryptionInfo': encryption_info
    }
    request = service.conversions().batchinsert(profileId=profile_id,
                                                body=request_body)
    response = request.execute()

    if not response['hasFailures']:
      print ('Successfully inserted conversion for encrypted user ID %s.'
          % encrypted_user_id)
    else:
      print ('Error(s) inserting conversion for encrypted user ID %s.'
          % encrypted_user_id)

      status = response['status'][0]
      for error in status['errors']:
        print '\t[%s]: %s' % (error['code'], error['message'])

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
