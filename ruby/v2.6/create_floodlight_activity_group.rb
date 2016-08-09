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
# This example creates a new activity group for a floodlight configuration.
#
# To get a floodlight configuration ID, run get_advertisers.rb.

require_relative 'dfareporting_utils'

def create_floodlight_activity_group(dfareporting, args)
  # Create a new floodlight activity group resource to insert
  activity_group = {
    :floodlightConfigurationId => args[:floodlight_config_id],
    :name => 'Example Floodlight Activity Group',
    :type => 'COUNTER'
  }

  # Insert the floodlight activity group
  result = dfareporting.floodlight_activity_groups.insert(
    :profileId => args[:profile_id]
  ).body(activity_group).execute()

  # Display results
  puts 'Created floodlight activity group with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :floodlight_config_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_floodlight_activity_group(dfareporting, args)
end
