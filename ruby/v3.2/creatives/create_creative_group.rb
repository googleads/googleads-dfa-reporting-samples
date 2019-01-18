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
# This example creates a creative group associated with a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb. Valid group numbers are
# limited to 1 or 2.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_creative_group(profile_id, advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Create a new creative group resource to insert.
  creative_group = DfareportingUtils::API_NAMESPACE::CreativeGroup.new(
    advertiser_id: advertiser_id,
    group_number: 1,
    name: format('Example Creative Group #%s', SecureRandom.hex(3))
  )

  # Insert the creative group.
  result = service.insert_creative_group(profile_id, creative_group)

  # Display results.
  puts format('Created creative group with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  create_creative_group(args[:profile_id], args[:advertiser_id])
end
