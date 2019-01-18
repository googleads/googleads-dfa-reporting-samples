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
# This example creates a new activity group for a floodlight configuration.
#
# To get a floodlight configuration ID, run get_advertisers.rb.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_floodlight_activity_group(profile_id, floodlight_config_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Create a new floodlight activity group resource to insert.
  activity_group =
    DfareportingUtils::API_NAMESPACE::FloodlightActivityGroup.new(
      floodlight_configuration_id: floodlight_config_id,
      name: format('Example Floodlight Activity Group #%s', SecureRandom.hex(3)),
      type: 'COUNTER'
    )

  # Insert the floodlight activity group.
  result = service.insert_floodlight_activity_group(profile_id, activity_group)

  # Display results.
  puts format('Created floodlight activity group with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id,
    :floodlight_config_id)

  create_floodlight_activity_group(args[:profile_id],
    args[:floodlight_config_id])
end
