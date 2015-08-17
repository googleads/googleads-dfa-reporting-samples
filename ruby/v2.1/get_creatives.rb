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
# This example lists all existing active creatives for a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb.

require_relative 'dfareporting_utils'

def get_creatives(dfareporting, args)
  # Construct the request
  request = dfareporting.creatives.list(
    :active => true,
    :advertiserId => args[:advertiser_id],
    :profileId => args[:profile_id],
    # Limit the fields returned
    :fields => 'nextPageToken,creatives(id,name,type)'
  )

  loop do
    result = request.execute()

    # Display results
    result.data.creatives.each do |creative|
      puts 'Found %s creative with ID %d and name "%s".' %
          [creative.type, creative.id, creative.name]
    end

    token = result.next_page_token

    break unless token and result.data.creatives.any?
    request = result.next_page
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_creatives(dfareporting, args)
end
