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

"""This example creates an in-stream video creative.

Requires a video asset and advertiser ID as input. To get an advertiser ID,
run get_advertisers.py.
"""

import argparse
import sys

from apiclient.http import MediaFileUpload
import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int, help='The ID of the profile to add a user role for')
argparser.add_argument(
    'advertiser_id', type=int,
    help='The ID of the advertiser to associate this creative with.')
argparser.add_argument(
    'video_name', help='Suggested name to use for the uploaded creative asset.')
argparser.add_argument(
    'path_to_video_file', help='Path to the asset file to be uploaded.')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  advertiser_id = flags.advertiser_id
  path_to_video_file = flags.path_to_video_file
  video_name = flags.video_name

  try:
    # Upload the creative asset
    creative_asset_id = upload_creative_asset(
        service, profile_id, advertiser_id, video_name, path_to_video_file,
        'VIDEO')

    # Construct the creative structure.
    creative = {
        'advertiserId': advertiser_id,
        'creativeAssets': [
            {'assetIdentifier': creative_asset_id, 'role': 'PARENT_VIDEO'}
        ],
        'name': 'Test in-stream video creative',
        'type': 'INSTREAM_VIDEO'
    }

    request = service.creatives().insert(profileId=profile_id, body=creative)

    # Execute request and print response.
    response = request.execute()

    print ('Created in-stream video creative with ID %s and name "%s".'
           % (response['id'], response['name']))

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def upload_creative_asset(
    service, profile_id, advertiser_id, asset_name, path_to_asset_file,
    asset_type):
  """Uploads a creative asset and returns an assetIdentifier."""
  # Construct the creative asset metadata
  creative_asset = {
      'assetIdentifier': {
          'name': asset_name,
          'type': asset_type
      }
  }

  media = MediaFileUpload(path_to_asset_file)
  if not media.mimetype():
    media = MediaFileUpload(path_to_asset_file, 'application/octet-stream')

  response = service.creativeAssets().insert(
      advertiserId=advertiser_id,
      profileId=profile_id,
      media_body=media,
      body=creative_asset).execute()

  return response['assetIdentifier']


if __name__ == '__main__':
  main(sys.argv)
