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
# This example creates a placement group in a given campaign.
#
# Requires the DCM site ID and campaign ID in which the placement group will be
# created. To create a campaign, run create_campaign.rb. To get DCM site ID,
# run get_site.rb.

require_relative '../dfareporting_utils'

def create_placement_group(profile_id, campaign_id, site_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Look up the campaign.
  campaign = service.get_campaign(profile_id, campaign_id)

  # Create a new placement group resource to insert.
  placement_group = DfareportingUtils::API_NAMESPACE::PlacementGroup.new(
    campaign_id: campaign_id,
    name: 'Example Placement Group',
    placement_group_type: 'PLACEMENT_PACKAGE',
    pricing_schedule: DfareportingUtils::API_NAMESPACE::PricingSchedule.new(
      start_date: campaign.start_date,
      end_date: campaign.end_date,
      pricing_type: 'PRICING_TYPE_CPM'
    ),
    site_id: site_id
  )

  # Insert the placement strategy.
  result = service.insert_placement_group(profile_id, placement_group)

  # Display results.
  puts format('Created placement group with ID %d and name "%s".', result.id,
    result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :campaign_id,
    :site_id)

  create_placement_group(args[:profile_id], args[:campaign_id], args[:site_id])
end
