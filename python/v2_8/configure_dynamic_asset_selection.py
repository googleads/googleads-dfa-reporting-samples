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

"""This example enables dynamic asset selection for an in-stream video creative.

Requires an existing in-stream video creative, a new video asset, and a
targeting template ID as input. To get an in-stream video creative, run
create_instream_video_creative.py. To get a targeting template, run
create_targeting_template.py.
"""

import argparse
import sys

from apiclient.http import MediaFileUpload
import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to configure dynamic asset selection for')
argparser.add_argument(
    'creative_id', type=int,
    help='The ID of the in-stream video creative to configure selection for.')
argparser.add_argument(
    'template_id', type=int,
    help='The ID of the template to use for targeting.')
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
  creative_id = flags.creative_id
  template_id = flags.template_id
  path_to_video_file = flags.path_to_video_file
  video_name = flags.video_name

  try:
    # Retrieve the specified creative.
    creative = service.creatives().get(profileId=profile_id,
                                       id=creative_id).execute()

    if not creative or creative['type'] != 'INSTREAM_VIDEO':
      sys.exit('Invalid creative specified.')

    if 'creativeAssetSelection' not in creative:
      # Locate an existing video asset to use as a default.
      default_asset_id = next((asset['id']
                               for asset in creative['creativeAssets']
                               if asset['role'] == 'PARENT_VIDEO'), None)

      if not default_asset_id:
        sys.exit('Default video asset could not be found.')

      # Enable dynamic asset selection for the creative.
      creative['dynamicAssetSelection'] = True

      # Create a new selection using the existing asset as a default.
      creative['creativeAssetSelection'] = {
          'defaultAssetId': default_asset_id,
          'rules': []
      }

    # Upload the new video asset and add it to the creative.
    video_asset = upload_creative_asset(
        service, profile_id, creative['advertiserId'], video_name,
        path_to_video_file, 'VIDEO')

    creative['creativeAssets'].append({
        'assetIdentifier': video_asset['assetIdentifier'],
        'role': 'PARENT_VIDEO'
    })

    # Create a rule targeting the new video asset and add it to the creative.
    creative['creativeAssetSelection']['rules'].append({
        'assetId': video_asset['id'],
        'name': 'Test rule for asset %s' % video_asset['id'],
        'targetingTemplateId': template_id
    })

    request = service.creatives().update(profileId=profile_id, body=creative)

    # Execute request and print response.
    response = request.execute()

    print ('Dynamic asset selection enabled for creative with ID %s.'
           % response['id'])

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def upload_creative_asset(
    service, profile_id, advertiser_id, asset_name, path_to_asset_file,
    asset_type):
  """Uploads a creative asset and returns a creative asset metadata object."""
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

  return response


if __name__ == '__main__':
  main(sys.argv)
