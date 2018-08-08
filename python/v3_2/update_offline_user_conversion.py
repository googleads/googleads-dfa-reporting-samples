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

"""Updates an offline conversion attributed to an encrypted user ID.

To create a conversion attributed to an encrypted user ID, run
insert_offline_user_conversion.py.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id',
    type=int,
    help='The ID of the profile to update a conversion for')

# Values that specify how the existing conversion is encrypted.
argparser.add_argument(
    'encrypted_user_id',
    help='The encrypted user ID the conversion is attributed to')
argparser.add_argument(
    'encryption_source', help='The source of the encrypted user ID')
argparser.add_argument(
    'encryption_entity_id',
    type=int,
    help='The ID of the entity used to encrypt the supplied user ID')
argparser.add_argument(
    'encryption_entity_type',
    help='The type of the entity used to encrypt the supplied user ID')

# Values that identify the existing conversion.
argparser.add_argument(
    'floodlight_activity_id',
    type=int,
    help='The ID of Floodlight activity this conversion is associated with')
argparser.add_argument('ordinal', help='The ordinal of the conversion.')
argparser.add_argument(
    'timestamp', type=int, help='The timestamp of the conversion.')

# Values to update for the specified conversion.
argparser.add_argument(
    'new_quantity',
    type=int,
    help='The new quantity to set for the conversion.')
argparser.add_argument(
    'new_value', type=int, help='The new value to set for the conversion.')


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
  ordinal = flags.ordinal
  timestamp = flags.timestamp
  new_quantity = flags.new_quantity
  new_value = flags.new_value

  try:
    # Look up the Floodlight configuration ID based on activity ID.
    floodlight_activity = service.floodlightActivities().get(
        profileId=profile_id, id=floodlight_activity_id).execute()
    floodlight_config_id = floodlight_activity['floodlightConfigurationId']

    # Construct the conversion object with values that identify the conversion
    # to update.
    conversion = {
        'encryptedUserId': encrypted_user_id,
        'floodlightActivityId': floodlight_activity_id,
        'floodlightConfigurationId': floodlight_config_id,
        'ordinal': ordinal,
        'timestampMicros': timestamp
    }

    # Set the fields to be updated. These fields are required; to preserve a
    # value from the existing conversion, it must be copied over manually.
    conversion['quantity'] = new_quantity
    conversion['value'] = new_value

    # Construct the encryption info.
    encryption_info = {
        'encryptionEntityId': encryption_entity_id,
        'encryptionEntityType': encryption_entity_type,
        'encryptionSource': encryption_source
    }

    # Update the conversion.
    request_body = {
        'conversions': [conversion],
        'encryptionInfo': encryption_info
    }
    request = service.conversions().batchupdate(
        profileId=profile_id, body=request_body)
    response = request.execute()

    if not response['hasFailures']:
      print('Successfully updated conversion for encrypted user ID %s.' %
            encrypted_user_id)
    else:
      print('Error(s) updating conversion for encrypted user ID %s.' %
            encrypted_user_id)

      status = response['status'][0]
      for error in status['errors']:
        print '\t[%s]: %s' % (error['code'], error['message'])

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


if __name__ == '__main__':
  main(sys.argv)
