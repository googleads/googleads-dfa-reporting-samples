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
# This example downloads HTML Tags for a given campaign and placement ID.
#
# To create campaigns, run create_campaign.rb. To create placements, run
# create_placement.rb.

require_relative 'dfareporting_utils'

def download_placement_tags(dfareporting, args)
  # Construct the request
  result = dfareporting.placements.generatetags(
    :campaignId => args[:campaign_id],
    :placementIds => [args[:placement_id]],
    :profileId => args[:profile_id]
  ).execute()

  result.data.placement_tags.each do |tag|
    tag.tag_datas.each do |data|
      puts "%s - %s\n\n" % [tag.placement_id, data.format]
      puts "%s\n\n" % data.impression_tag unless data.impression_tag.nil?
      puts "%s\n\n" % data.click_tag unless data.click_tag.nil?
    end
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :campaign_id, :placement_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  download_placement_tags(dfareporting, args)
end
