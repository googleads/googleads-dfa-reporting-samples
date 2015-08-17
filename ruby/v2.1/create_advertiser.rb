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
# This example creates an advertiser in a given DCM account.

require_relative 'dfareporting_utils'

def create_advertiser(dfareporting, args)
  # Create a new advertiser resource to insert
  advertiser_group = {
    :name => 'Example Advertiser',
    :status => 'APPROVED'
  }

  # Insert the advertiser
  result = dfareporting.advertisers.insert(
    :profileId => args[:profile_id]
  ).body(advertiser_group).execute()

  # Display results
  puts 'Created advertiser with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_advertiser(dfareporting, args)
end
