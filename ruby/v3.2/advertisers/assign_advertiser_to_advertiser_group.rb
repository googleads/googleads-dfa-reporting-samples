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
# This example assigns an advertiser to an advertiser group.
#
# CAUTION: An advertiser that has campaigns associated with it cannot be
# removed from an advertiser group once assigned.

require_relative '../dfareporting_utils'

def assign_advertiser_to_advertiser_group(profile_id, advertiser_id,
  advertiser_group_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Create a the advertiser group association to patch.
  advertiser = DfareportingUtils::API_NAMESPACE::Advertiser.new(
    advertiser_group_id: advertiser_group_id
  )

  # Patch the advertiser group association.
  result = service.patch_advertiser(profile_id, advertiser_id, advertiser)

  # Display results
  puts format('Assigned advertiser with ID %d to advertiser group with ID %d.', result.id, result.advertiser_group_id)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id,
    :advertiser_group_id)

  assign_advertiser_to_advertiser_group(args[:profile_id], args[:advertiser_id],
    args[:advertiser_group_id])
end
