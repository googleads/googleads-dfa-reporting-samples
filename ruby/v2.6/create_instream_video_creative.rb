#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2015, Google Inc. All Rights Reserved.
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
# This example creates an in-stream video creative.
#
# Requires a video asset and advertiser ID as input. To get an advertiser ID,
# run get_advertisers.rb.

require_relative 'creative_asset_utils'
require_relative 'dfareporting_utils'

def create_instream_video_creative(dfareporting, args)
  # Upload the creative asset
  util = CreativeAssetUtils.new(dfareporting, args[:profile_id])
  creative_asset_id = util.uploadAsset(args[:advertiser_id],
      args[:path_to_video_file], 'VIDEO').asset_identifier

  # Construct the creative structure
  creative = {
    :advertiserId => args[:advertiser_id],
    :creativeAssets => [
      {
        :assetIdentifier => creative_asset_id,
        :role => 'PARENT_VIDEO'
      }
    ],
    :name => 'Example in-stream video creative',
    :type => 'INSTREAM_VIDEO'
  }

  # Insert the creative
  result = dfareporting.creatives.insert(
    :profileId => args[:profile_id]
  ).body(creative).execute()

  puts 'Created in-stream video creative with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :advertiser_id, :path_to_video_file)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_instream_video_creative(dfareporting, args)
end
