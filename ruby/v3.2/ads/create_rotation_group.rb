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
# This example creates a rotation group ad in a given campaign.

require_relative '../dfareporting_utils'

def create_rotation_group(profile_id, campaign_id, placement_id, creative_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Retrieve the campaign (to get end date).
  campaign = service.get_campaign(profile_id, campaign_id)

  # Construct creative assignment.
  creative_assignment =
    DfareportingUtils::API_NAMESPACE::CreativeAssignment.new(
      active: true,
      creative_id: creative_id,
      click_through_url: DfareportingUtils::API_NAMESPACE::ClickThroughUrl.new(
        default_landing_page: true
      )
    )

  # Construct placement assignment.
  placement_assignment =
    DfareportingUtils::API_NAMESPACE::PlacementAssignment.new(
      active: true,
      placement_id: placement_id
    )

  # Construct creative rotation.
  creative_rotation = DfareportingUtils::API_NAMESPACE::CreativeRotation.new(
    creative_assignments: [creative_assignment],
    type: 'CREATIVE_ROTATION_TYPE_RANDOM',
    weight_calculation_strategy: 'WEIGHT_STRATEGY_OPTIMIZED'
  )

  # Construct delivery schedule.
  delivery_schedule = DfareportingUtils::API_NAMESPACE::DeliverySchedule.new(
    impression_ratio: 1,
    priority: 'AD_PRIORITY_01'
  )

  # Construct and save ad.
  ad = DfareportingUtils::API_NAMESPACE::Ad.new(
    active: true,
    campaign_id: campaign_id,
    creative_rotation: creative_rotation,
    delivery_schedule: delivery_schedule,
    end_time: format('%sT00:00:00Z', campaign.end_date),
    name: 'Example Rotation Group',
    placement_assignments: [placement_assignment],
    start_time: format('%sT23:59:59Z', Time.now.strftime('%Y-%m-%d')),
    type: 'AD_SERVING_STANDARD_AD'
  )

  result = service.insert_ad(profile_id, ad)

  puts format('Created rotation group with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :campaign_id,
    :placement_id, :creative_id)

  create_rotation_group(args[:profile_id], args[:campaign_id],
    args[:placement_id], args[:creative_id])
end
