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
# This example creates a display image gallery creative.
#
# Requires two image assets and an advertiser ID as input. To get an advertiser
# ID, run get_advertisers.rb.

require_relative '../creative_asset_utils'
require_relative '../dfareporting_utils'

def create_enhanced_image_creative(profile_id, advertiser_id, size_id,
  path_to_image1_file, path_to_image2_file)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  util = CreativeAssetUtils.new(service, profile_id)

  # Upload the first image asset.
  image1_asset_id = util.upload_asset(advertiser_id, path_to_image1_file,
    'HTML_IMAGE').asset_identifier

  # Upload the second image asset.
  image2_asset_id = util.upload_asset(advertiser_id, path_to_image2_file,
    'HTML_IMAGE').asset_identifier

  # Construct the creative structure.
  creative = DfareportingUtils::API_NAMESPACE::Creative.new(
    advertiser_id: advertiser_id,
    auto_advance_images: true,
    click_tags: [
      DfareportingUtils::API_NAMESPACE::ClickTag.new(
        event_name: image1_asset_id.name,
        name: image1_asset_id.name
      ),
      DfareportingUtils::API_NAMESPACE::ClickTag.new(
        event_name: image2_asset_id.name,
        name: image2_asset_id.name
      )
    ],
    creative_assets: [
      DfareportingUtils::API_NAMESPACE::CreativeAsset.new(
        asset_identifier: image1_asset_id,
        role: 'PRIMARY'
      ),
      DfareportingUtils::API_NAMESPACE::CreativeAsset.new(
        asset_identifier: image2_asset_id,
        role: 'PRIMARY'
      )
    ],
    name: 'Example display image gallery creative',
    size: DfareportingUtils::API_NAMESPACE::Size.new(id: size_id),
    type: 'DISPLAY_IMAGE_GALLERY'
  )

  # Insert the creative.
  result = service.insert_creative(profile_id, creative)

  puts format(
    'Created display image gallery creative with ID %d and name "%s".',
    result.id, result.name
  )
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :advertiser_id,
    :size_id, :path_to_image1_file, :path_to_image2_file)

  create_enhanced_image_creative(args[:profile_id], args[:advertiser_id],
    args[:size_id], args[:path_to_image1_file], args[:path_to_image2_file])
end
