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
# This example creates a rotation group ad in a given campaign.

require_relative 'dfareporting_utils'

def create_rotation_group(dfareporting, args)
  # Retrieve the campaign (to get end date)
  campaign = dfareporting.campaigns.get(
    :id => args[:campaign_id],
    :profileId => args[:profile_id]
  ).execute()

  # Construct creative assignment
  creative_assignment = {
    :active => true,
    :creativeId => args[:creative_id],
    :clickThroughUrl => {
      :defaultLandingPage => true
    }
  }

  # Construct placement assignment
  placement_assignment = {
    :active => true,
    :placementId => args[:placement_id]
  }

  # Construct creative rotation
  creative_rotation = {
    :creativeAssignments => [creative_assignment],
    :type => 'CREATIVE_ROTATION_TYPE_RANDOM',
    :weightCalculationStrategy => 'WEIGHT_STRATEGY_OPTIMIZED'
  }

  # Construct delivery schedule
  delivery_schedule = {
    :impressionRatio => 1,
    :priority => 'AD_PRIORITY_01'
  }

  # Construct and save ad
  ad = {
    :active => true,
    :campaignId => args[:campaign_id],
    :creativeRotation => creative_rotation,
    :deliverySchedule => delivery_schedule,
    :endTime => '%sT00:00:00Z' % campaign.data.end_date,
    :name => 'Example Rotation Group',
    :placementAssignments => [placement_assignment],
    :startTime => '%sT23:59:59Z' % Time.now.strftime('%Y-%m-%d'),
    :type => 'AD_SERVING_STANDARD_AD'
  }

  result = dfareporting.ads.insert(
    :profileId => args[:profile_id]
  ).body(ad).execute()

  puts 'Created rotation group with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :campaign_id, :placement_id, :creative_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_rotation_group(dfareporting, args)
end
