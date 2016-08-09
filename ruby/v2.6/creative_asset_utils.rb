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
# Utility class for handling common tasks related to working with creative
# assets.

require 'google/api_client'
require 'google/api_client/media'

class CreativeAssetUtils

  # Creates a new instance of CreativeAssetUtils
  def initialize(service, profile_id)
    @service = service
    @profile_id = profile_id
  end

  # Uploads a creative asset and returns the asset metadata
  def uploadAsset(advertiser_id, path_to_asset_file, asset_type)
    asset_name = File.basename(path_to_asset_file)

    # Construct the creative asset metadata
    creative_asset = {
      :assetIdentifier => {
        :name => asset_name,
        :type => asset_type
      }
    }

    # Upload the asset
    mime_type = self.determine_mime_type(path_to_asset_file, asset_type)
    media = Google::APIClient::UploadIO.new(path_to_asset_file, mime_type)

    result = @service.creative_assets.insert(
      :advertiserId => advertiser_id,
      :profileId => @profile_id,
      :uploadType => 'multipart'
    ).body(creative_asset).media(media).execute()

    puts 'Creative asset was saved with name "%s".' %
        result.data.asset_identifier.name

    return result.data
  end

  # Performs a naive mime-type lookup based on file name and asset type
  def determine_mime_type(path_to_asset_file, asset_type)
    case asset_type
    when 'IMAGE', 'HTML_IMAGE'
      return 'image/%s' % File.extname(path_to_asset_file)
    when 'VIDEO'
      return 'video/%s' % File.extname(path_to_asset_file)
    else
      return 'application/octet-stream'
    end
  end
end
