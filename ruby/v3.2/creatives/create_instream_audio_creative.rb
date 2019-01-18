#!/usr/bin/env ruby

#
# Copyright:: Copyright 2018, Google Inc. All Rights Reserved.
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# This example creates an in-stream audio creative.
#
# Requires an audio asset and advertiser ID as input. To get an advertiser ID,
# run get_advertisers.rb.

require_relative '../creative_asset_utils'
require_relative '../dfareporting_utils'

def create_instream_audio_creative(profile_id, advertiser_id,
  path_to_audio_file)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Upload the creative asset.
  util = CreativeAssetUtils.new(service, profile_id)
  creative_asset_id = util.upload_asset(advertiser_id, path_to_audio_file,
    'AUDIO').asset_identifier

  # Construct the creative structure.
  creative = DfareportingUtils::API_NAMESPACE::Creative.new(
    advertiser_id: advertiser_id,
    creative_assets: [
      DfareportingUtils::API_NAMESPACE::CreativeAsset.new(
        asset_identifier: creative_asset_id,
        role: 'PARENT_AUDIO'
      )
    ],
    name: 'Example in-stream audio creative',
    type: 'INSTREAM_AUDIO'
  )

  # Insert the creative.
  result = service.insert_creative(profile_id, creative)

  puts format('Created in-stream audio creative with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id,
    :path_to_audio_file)

  create_instream_audio_creative(args[:profile_id], args[:advertiser_id],
    args[:path_to_audio_file])
end
