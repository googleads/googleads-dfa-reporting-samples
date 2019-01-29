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
# This example creates a floodlight activity in a given activity group.
#
# To create an activity group, run create_floodlight_activity_group.rb.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_floodlight_activity(profile_id, activity_group_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new floodlight activity resource to insert.
  activity = DfareportingUtils::API_NAMESPACE::FloodlightActivity.new(
    counting_method: 'STANDARD_COUNTING',
    expected_url: 'http://www.google.com',
    floodlight_activity_group_id: activity_group_id,
    floodlight_tag_type: 'GLOBAL_SITE_TAG',
    name: format('Example Floodlight Activity #%s', SecureRandom.hex(3))
  )

  # Insert the floodlight activity.
  result = service.insert_floodlight_activity(profile_id, activity)

  # Display results.
  puts format('Created floodlight activity with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :activity_group_id)

  create_floodlight_activity(args[:profile_id], args[:activity_group_id])
end
