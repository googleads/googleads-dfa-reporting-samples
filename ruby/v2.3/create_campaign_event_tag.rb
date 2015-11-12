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
# This example creates a creative field associated with a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb.

require_relative 'dfareporting_utils'

def create_campaign_event_tag(dfareporting, args)
  # Create a new event tag resource to insert
  event_tag = {
    :campaignId => args[:campaign_id],
    :name => 'Example Campaign Event Tag',
    :status => 'ENABLED',
    :type => 'CLICK_THROUGH_EVENT_TAG',
    :url => 'https://www.google.com'
  }

  # Insert the event tag
  result = dfareporting.event_tags.insert(
    :profileId => args[:profile_id]
  ).body(event_tag).execute()

  # Display results
  puts 'Created campaign event tag with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :campaign_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_campaign_event_tag(dfareporting, args)
end
