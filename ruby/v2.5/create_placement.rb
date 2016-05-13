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
# This example creates an agency paid regular placement in a given campaign.
#
# Requires the DCM site ID and campaign ID in which the placement will be
# created. To create a campaign, run create_campaign.rb. To get DCM site ID,
# run get_site.rb.

require_relative 'dfareporting_utils'

def create_placement(dfareporting, args)
  # Look up the campaign
  campaign = dfareporting.campaigns.get(
    :id => args[:campaign_id],
    :profileId => args[:profile_id]
  ).execute()

  # Create a new placement resource to insert
  placement = {
    :campaignId => args[:campaign_id],
    :compatibility => 'DISPLAY',
    :name => 'Example Placement',
    :paymentSource => 'PLACEMENT_AGENCY_PAID',
    :siteId => args[:site_id],
    :size => {
      :height => 1,
      :width => 1
    },
    :tagFormats => ['PLACEMENT_TAG_STANDARD']
  }

  # Set the pricing schedule for the placement
  placement[:pricingSchedule] = {
    :endDate => campaign.data.end_date,
    :pricingType => 'PRICING_TYPE_CPM',
    :startDate => campaign.data.start_date
  }

  # Insert the placement strategy
  result = dfareporting.placements.insert(
    :profileId => args[:profile_id]
  ).body(placement).execute()

  # Display results
  puts 'Created placement with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :campaign_id, :site_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_placement(dfareporting, args)
end
