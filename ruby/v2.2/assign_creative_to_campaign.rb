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
# This example assigns a given creative to a given campaign.
#
# Note that both the creative and campaign must be associated with the same
# advertiser.

require_relative 'dfareporting_utils'

def assign_creative_to_campaign(dfareporting, args)
  # Create a new creative-campaign association to insert
  association = {
    :creativeId => args[:creative_id]
  }

  # Insert the advertiser group
  result = dfareporting.campaign_creative_associations.insert(
    :campaignId => args[:campaign_id],
    :profileId => args[:profile_id]
  ).body(association).execute()

  # Display results
  puts 'Creative with ID %d is now associated with campaign %d.' %
      [result.data.creative_id, args[:campaign_id]]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :campaign_id, :creative_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  assign_creative_to_campaign(dfareporting, args)
end
