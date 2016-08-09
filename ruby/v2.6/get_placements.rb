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
# This example displays all available placements.

require_relative 'dfareporting_utils'

def get_placements(dfareporting, args)
  # Construct the request
  request = dfareporting.placements.list(
    :profileId => args[:profile_id],
    # Limit the fields returned
    :fields => 'nextPageToken,placements(campaignId,id,name)'
  )

  loop do
    result = request.execute()

    # Display results
    result.data.placements.each do |placement|
      puts 'Found placement with ID %d and name "%s" for campaign %d.' %
          [placement.id, placement.name, placement.campaign_id]
    end

    token = result.next_page_token

    break unless token and result.data.placements.any?
    request = result.next_page
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_placements(dfareporting, args)
end
