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
# This example creates an agency paid regular placement in a given campaign.
#
# Requires the DCM site ID and campaign ID in which the placement will be
# created. To create a campaign, run create_campaign.rb. To get DCM site ID,
# run get_site.rb.

require_relative '../dfareporting_utils'

def create_placement(profile_id, campaign_id, site_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Look up the campaign.
  campaign = service.get_campaign(profile_id, campaign_id)

  # Create a new placement resource to insert.
  placement = DfareportingUtils::API_NAMESPACE::Placement.new(
    campaign_id: campaign_id,
    compatibility: 'DISPLAY',
    name: 'Example Placement',
    payment_source: 'PLACEMENT_AGENCY_PAID',
    site_id: site_id,
    size: DfareportingUtils::API_NAMESPACE::Size.new(
      height: 1,
      width: 1
    ),
    tag_formats: ['PLACEMENT_TAG_STANDARD']
  )

  # Set the pricing schedule for the placement.
  placement.pricing_schedule =
    DfareportingUtils::API_NAMESPACE::PricingSchedule.new(
      end_date: campaign.end_date,
      pricing_type: 'PRICING_TYPE_CPM',
      start_date: campaign.start_date
    )

  # Insert the placement strategy.
  result = service.insert_placement(profile_id, placement)

  # Display results.
  puts format('Created placement with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :campaign_id,
    :site_id)

  create_placement(args[:profile_id], args[:campaign_id], args[:site_id])
end
