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
# This example assigns a given creative to a given campaign.
#
# Note that both the creative and campaign must be associated with the same
# advertiser.

require_relative '../dfareporting_utils'

def assign_creative_to_campaign(profile_id, campaign_id, creative_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new creative-campaign association to insert
  association =
    DfareportingUtils::API_NAMESPACE::CampaignCreativeAssociation.new(
      creative_id: creative_id
    )

  # Insert the advertiser group.
  result = service.insert_campaign_creative_association(profile_id, campaign_id,
    association)

  # Display results.
  puts format('Creative with ID %d is now associated with campaign %d.', result.creative_id, campaign_id)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :campaign_id,
    :creative_id)

  assign_creative_to_campaign(args[:profile_id], args[:campaign_id],
    args[:creative_id])
end
