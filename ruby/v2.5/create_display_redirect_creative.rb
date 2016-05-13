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
# This example creates a display redirect creative associated with a given
# advertiser.
#
# To get a size ID, run get_size.rb.

require_relative 'dfareporting_utils'

def create_tracking_creative(dfareporting, args)
  # Create a new creative resource to insert
  creative = {
    :advertiserId => args[:advertiser_id],
    :name => 'Example display redirect creative',
    :redirectUrl => args[:image_url],
    :size => { :id => args[:size_id] },
    :type => 'DISPLAY_REDIRECT'
  }

  # Insert the creative
  result = dfareporting.creatives.insert(
    :profileId => args[:profile_id]
  ).body(creative).execute()

  # Display results
  puts 'Created display redirect creative with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :advertiser_id, :image_url, :size_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_tracking_creative(dfareporting, args)
end
