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
# This example creates a creative field associated with a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_campaign_event_tag(profile_id, campaign_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  # Create a new event tag resource to insert.
  event_tag = DfareportingUtils::API_NAMESPACE::EventTag.new({
    :campaign_id => campaign_id,
    :name => 'Example Campaign Event Tag #%s' % SecureRandom.hex(3),
    :status => 'ENABLED',
    :type => 'CLICK_THROUGH_EVENT_TAG',
    :url => 'https://www.google.com'
  })

  # Insert the event tag.
  result = service.insert_event_tag(profile_id, event_tag)

  # Display results.
  puts 'Created campaign event tag with ID %d and name "%s".' %
      [result.id, result.name]
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :campaign_id)

  create_campaign_event_tag(args[:profile_id], args[:campaign_id])
end
