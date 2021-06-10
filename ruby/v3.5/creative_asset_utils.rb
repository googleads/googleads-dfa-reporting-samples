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
# Utility class for handling common tasks related to working with creative
# assets.

require_relative 'dfareporting_utils'

# Creative asset utilities used by DFA Reporting and Trafficking API creative
# examples.
class CreativeAssetUtils
  # Creates a new instance of CreativeAssetUtils.
  def initialize(service, profile_id)
    @service = service
    @profile_id = profile_id
  end

  # Uploads a creative asset and returns the creative asset metadata.
  def upload_asset(advertiser_id, path_to_asset_file, asset_type)
    asset_name = File.basename(path_to_asset_file)

    # Construct the creative asset metadata
    creative_asset = DfareportingUtils::API_NAMESPACE::CreativeAsset.new(
      asset_identifier: DfareportingUtils::API_NAMESPACE::CreativeAssetId.new(
        name: asset_name,
        type: asset_type
      )
    )

    # Upload the asset.
    mime_type = determine_mime_type(path_to_asset_file, asset_type)

    result = @service.insert_creative_asset(
      @profile_id,
      advertiser_id,
      creative_asset,
      content_type: mime_type,
      upload_source: path_to_asset_file
    )

    puts format('Creative asset was saved with name "%s".',
      result.asset_identifier.name)

    result
  end

  # Performs a naive mime-type lookup based on file name and asset type.
  def determine_mime_type(path_to_asset_file, asset_type)
    case asset_type
    when 'IMAGE', 'HTML_IMAGE'
      return format('image/%s', File.extname(path_to_asset_file))
    when 'VIDEO'
      format('video/%s', File.extname(path_to_asset_file))
    else
      'application/octet-stream'
    end
  end
end
