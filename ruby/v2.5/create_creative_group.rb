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
# This example creates a creative group associated with a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb. Valid group numbers are
# limited to 1 or 2.

require_relative 'dfareporting_utils'

def create_creative_group(dfareporting, args)
  # Create a new creative group resource to insert
  creative_group = {
    :advertiserId => args[:advertiser_id],
    :groupNumber => 1,
    :name => 'Example Creative Group'
  }

  # Insert the creative group
  result = dfareporting.creative_groups.insert(
    :profileId => args[:profile_id]
  ).body(creative_group).execute()

  # Display results
  puts 'Created creative group with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_creative_group(dfareporting, args)
end
