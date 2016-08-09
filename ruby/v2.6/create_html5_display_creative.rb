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
# This example creates an HTML5 display creative.
#
# Requires an HTML5 asset, backup image asset, and an advertiser ID as input.
# To get an advertiser ID, run get_advertisers.rb.

require_relative 'creative_asset_utils'
require_relative 'dfareporting_utils'

def create_html5_banner_creative(dfareporting, args)
  util = CreativeAssetUtils.new(dfareporting, args[:profile_id])

  # Upload the HTML5 asset
  html5_asset_id = util.uploadAsset(args[:advertiser_id],
      args[:path_to_html5_asset_file], 'HTML').asset_identifier

  # Upload the backup image asset
  backup_image_asset_id = util.uploadAsset(args[:advertiser_id],
      args[:path_to_backup_image_file], 'HTML_IMAGE').asset_identifier

  # Construct the creative structure
  creative = {
    :advertiserId => args[:advertiser_id],
    :backupImageClickThroughUrl => 'https://www.google.com',
    :backupImageReportingLabel => 'backup',
    :backupImageTargetWindow => { :targetWindowOption => 'NEW_WINDOW' },
    :clickTags => [{
      :eventName => 'exit',
      :name => 'click_tag',
      :value => 'https://www.google.com'
    }],
    :creativeAssets => [
      {
        :assetIdentifier => html5_asset_id,
        :role => 'PRIMARY'
      },
      {
        :assetIdentifier => backup_image_asset_id,
        :role => 'BACKUP_IMAGE'
      }
    ],
    :name => 'Example HTML5 display creative',
    :size => { :id => args[:size_id] },
    :type => 'DISPLAY'
  }

  # Insert the creative
  result = dfareporting.creatives.insert(
    :profileId => args[:profile_id]
  ).body(creative).execute()

  puts 'Created HTML5 display creative with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :advertiser_id, :size_id, :path_to_html5_asset_file,
      :path_to_backup_image_file)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_html5_banner_creative(dfareporting, args)
end
