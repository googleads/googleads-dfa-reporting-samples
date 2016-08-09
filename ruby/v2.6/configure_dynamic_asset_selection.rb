#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2016, Google Inc. All Rights Reserved.
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
# This example enables dynamic asset selection for an in-stream video creative.
#
# Requires an existing in-stream video creative, a new video asset, and a
# targeting template ID as input. To get an in-stream video creative, run
# create_instream_video_creative.rb. To get a targeting template, run
# create_targeting_template.rb.

require_relative 'creative_asset_utils'
require_relative 'dfareporting_utils'

def configure_dynamic_asset_selection(dfareporting, args)
  # Retrieve the specified creative.
  creative = dfareporting.creatives.get(
    :profileId => args[:profile_id],
    :id => args[:creative_id]
  ).execute()

  if creative.data.nil? or creative.data.type != 'INSTREAM_VIDEO'
    abort "Invalid creative specified"
  end

  unless creative.data.dynamic_asset_selection
    # Locate an existing video asset to use as a default.
    default_asset = creative.data.creative_assets.find do |asset|
      asset.role == 'PARENT_VIDEO'
    end

    if default_asset.nil?
      abort "Default video asset could not be found."
    end

    # Enable dynamic asset selection for the creative.
    creative.data.dynamic_asset_selection = true

    # Create a new selection using the existing asset as a default.
    creative.data.creative_asset_selection = {
      :defaultAssetId => default_asset.id
    }
  end

  # Upload the new video asset and add it to the creative.
  util = CreativeAssetUtils.new(dfareporting, args[:profile_id])
  video_asset = util.uploadAsset(creative.data.advertiser_id,
      args[:path_to_video_file], 'VIDEO')

  creative.data.creative_assets <<= {
    :assetIdentifier => video_asset.asset_identifier,
    :role => 'PARENT_VIDEO'
  }

  # Create a rule targeting the new video asset and add it to the creative.
  creative.data.creative_asset_selection.rules <<= {
    :assetId => video_asset.id,
    :name => 'Test rule for asset %d' %  video_asset.id,
    :targetingTemplateId => args[:template_id]
  }

  result = dfareporting.creatives.update({
    :profileId => args[:profile_id]
  }).body(creative.data).execute()

  puts 'Dynamic asset selection enabled for creative with ID %d.' %
      result.data.id
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :creative_id, :template_id, :path_to_video_file)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  configure_dynamic_asset_selection(dfareporting, args)
end
