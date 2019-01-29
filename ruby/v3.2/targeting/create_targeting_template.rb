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
# This example creates a targeting template associated with a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_targeting_template(profile_id, advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new targeting template resource to insert.
  # This template will be configured to serve ads on Monday, Wednesday, and
  # Friday from 9-10am and 3-5pm.
  # Note: targeting template names must be unique within an advetiser.
  targeting_template = DfareportingUtils::API_NAMESPACE::TargetingTemplate.new(
    advertiser_id: advertiser_id,
    day_part_targeting: DfareportingUtils::API_NAMESPACE::DayPartTargeting.new(
      days_of_week: %w[MONDAY WEDNESDAY FRIDAY],
      hours_of_day: [9, 15, 16],
      user_local_time: true
    ),
    name: format('Test Targeting Template #%s', SecureRandom.hex(3))
  )

  # Insert the targeting template.
  result = service.insert_targeting_template(profile_id, targeting_template)

  # Display results.
  puts format('Created targeting template with ID %d and name "%s".',
    result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :advertiser_id)

  create_targeting_template(args[:profile_id], args[:advertiser_id])
end
