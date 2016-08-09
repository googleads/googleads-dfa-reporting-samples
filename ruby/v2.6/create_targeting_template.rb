#!/usr/bin/env ruby
# Encoding: utf-8
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

require 'securerandom'
require_relative 'dfareporting_utils'

def create_targeting_template(dfareporting, args)
  # Create a new targeting template resource to insert.
  # This template will be configured to serve ads on Monday, Wednesday, and
  # Friday from 9-10am and 3-5pm.
  # Note: targeting template names must be unique within an advetiser.
  targeting_template = {
    :advertiserId => args[:advertiser_id],
    :dayPartTargeting => {
      :daysOfWeek => ['MONDAY', 'WEDNESDAY', 'FRIDAY'],
      :hoursOfDay => [9, 15, 16],
      :userLocalTime => true
    },
    :name => 'Test Targeting Template #%s' % SecureRandom.hex(3)
  }

  # Insert the targeting template.
  result = dfareporting.targeting_templates.insert(
    :profileId => args[:profile_id]
  ).body(targeting_template).execute()

  # Display results.
  puts 'Created targeting template with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  # Authenticate and initialize API service.
  dfareporting = DfaReportingUtils.setup()

  create_targeting_template(dfareporting, args)
end
