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
# This example creates a floodlight activity in a given activity group.
#
# To create an activity group, run create_floodlight_activity_group.rb.

require_relative 'dfareporting_utils'

def create_floodlight_activity(dfareporting, args)
  # Create a new floodlight activity resource to insert
  activity = {
    :countingMethod => 'STANDARD_COUNTING',
    :expectedUrl => 'http://www.google.com',
    :floodlightActivityGroupId => args[:activity_group_id],
    :name => 'Example Floodlight Activity'
  }

  # Insert the floodlight activity
  result = dfareporting.floodlight_activities.insert(
    :profileId => args[:profile_id]
  ).body(activity).execute()

  # Display results
  puts 'Created floodlight activity with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :activity_group_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_floodlight_activity(dfareporting, args)
end
