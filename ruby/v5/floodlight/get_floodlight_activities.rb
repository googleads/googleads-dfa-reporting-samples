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
# This example displays floodlight activities for a given advertiser.
#
# To create an advertiser, run create_advertiser.rb.

require_relative '../dfareporting_utils'

def get_floodlight_activities(profile_id, advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  token = nil
  loop do
    result = service.list_floodlight_activities(profile_id,
      advertiser_id: advertiser_id,
      page_token: token,
      fields: 'nextPageToken,floodlightActivities(id,name)')

    # Display results.
    if result.floodlight_activities.any?
      result.floodlight_activities.each do |activity|
        puts format('Found floodlight activity with ID %d and name "%s".',
          activity.id, activity.name)
      end

      token = result.next_page_token
    else
      # Stop paging if there are no more results.
      token = nil
    end

    break if token.to_s.empty?
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :advertiser_id)

  get_floodlight_activities(args[:profile_id], args[:advertiser_id])
end
