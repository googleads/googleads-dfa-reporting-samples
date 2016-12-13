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

"""This example lists all active, unconverted Flash creatives in a DCM account.

An unconverted Flash creative is defined as:
1. Having a primary Flash asset.
2. Lacking a converted HTML5 asset.

Creatives in this state will be deactivated on January 2, 2017. See
https://support.google.com/dcm/answer/6353522 for more information.
"""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to look up creatives for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  try:
    # Limit the fields returned.
    fields = ('nextPageToken,creatives(artworkType,'
              'creativeAssets(assetIdentifier/type,role),id,name,type)')

    # Construct the request.
    request = service.creatives().list(
        profileId=profile_id, active=True, fields=fields,
        # Only retrieve active creatives with an affected type.
        types=['CUSTOM_DISPLAY',
               'CUSTOM_DISPLAY_INTERSTITIAL',
               'DISPLAY',
               'FLASH_INPAGE',
               'RICH_MEDIA_DISPLAY_BANNER',
               'RICH_MEDIA_DISPLAY_EXPANDING',
               'RICH_MEDIA_DISPLAY_INTERSTITIAL',
               'RICH_MEDIA_DISPLAY_MULTI_FLOATING_INTERSTITIAL',
               'RICH_MEDIA_IM_EXPAND',
               'RICH_MEDIA_INPAGE_FLOATING',
               'RICH_MEDIA_MOBILE_IN_APP',
               'RICH_MEDIA_PEEL_DOWN'])

    while True:
      # Execute request and print response.
      response = request.execute()

      for creative in response['creatives']:
        if is_unconverted_flash_creative(creative):
          print ('Unconverted Flash creative found: %s (ID: %s, type: %s)'
                 % (creative['name'], creative['id'], creative['type']))

      if response['creatives'] and response['nextPageToken']:
        request = service.creatives().list_next(request, response)
      else:
        break

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def is_unconverted_flash_creative(creative):
  """Determines whether a creative contains an unconverted Flash asset."""
  if creative.get('artworkType') == 'ARTWORK_TYPE_FLASH':
    return True
  elif 'creativeAssets' in creative:
    has_primary_flash = False
    has_primary_html5 = False
    has_other_html5 = False

    for asset in creative['creativeAssets']:
      if asset['assetIdentifier']['type'] == 'FLASH':
        has_primary_flash = has_primary_flash or asset['role'] == 'PRIMARY'
      elif asset['assetIdentifier']['type'] == 'HTML':
        has_primary_html5 = has_primary_html5 or asset['role'] == 'PRIMARY'
        has_other_html5 = has_other_html5 or asset['role'] != 'PRIMARY'

    if creative['type'].startswith('CUSTOM'):
      return has_primary_flash
    elif creative['type'] == 'FLASH_INPAGE':
      return has_primary_flash and not has_primary_html5 and not has_other_html5
    else:
      return has_primary_flash and not has_primary_html5
  else:
    return False

if __name__ == '__main__':
  main(sys.argv)
