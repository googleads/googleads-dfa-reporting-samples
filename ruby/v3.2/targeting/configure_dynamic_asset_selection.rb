#!/usr/bin/env ruby

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

require_relative '../creative_asset_utils'
require_relative '../dfareporting_utils'

def configure_dynamic_asset_selection(profile_id, creative_id, template_id,
  path_to_video_file)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Retrieve the specified creative.
  creative = service.get_creative(profile_id, creative_id)

  abort 'Invalid creative specified' if
    creative.nil? || (creative.type != 'INSTREAM_VIDEO')

  unless creative.dynamic_asset_selection?
    # Locate an existing video asset to use as a default.
    default_asset = creative.creative_assets.find do |asset|
      asset.role == 'PARENT_VIDEO'
    end

    abort 'Default video asset could not be found.' if default_asset.nil?

    # Enable dynamic asset selection for the creative.
    creative.dynamic_asset_selection = true

    # Create a new selection using the existing asset as a default.
    creative.creative_asset_selection =
      DfareportingUtils::API_NAMESPACE::CreativeAssetSelection.new(
        default_asset_id: default_asset.id,
        rules: []
      )
  end

  # Upload the new video asset and add it to the creative.
  util = CreativeAssetUtils.new(service, profile_id)
  video_asset = util.upload_asset(creative.advertiser_id, path_to_video_file,
    'VIDEO')

  creative.creative_assets <<=
    DfareportingUtils::API_NAMESPACE::CreativeAsset.new(
      asset_identifier: video_asset.asset_identifier,
      role: 'PARENT_VIDEO'
    )

  # Create a rule targeting the new video asset and add it to the creative.
  creative.creative_asset_selection.rules <<=
    DfareportingUtils::API_NAMESPACE::Rule.new(
      asset_id: video_asset.id,
      name: format('Test rule for asset %d', video_asset.id),
      targeting_template_id: template_id
    )

  result = service.update_creative(profile_id, creative)

  puts format('Dynamic asset selection enabled for creative with ID %d.',
    result.id)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :creative_id,
    :template_id, :path_to_video_file)

  configure_dynamic_asset_selection(args[:profile_id], args[:creative_id],
    args[:template_id], args[:path_to_video_file])
end
