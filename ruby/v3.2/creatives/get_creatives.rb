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
# This example lists all existing active creatives for a given advertiser.
#
# To get an advertiser ID, run get_advertisers.rb.

require_relative '../dfareporting_utils'

def get_creatives(profile_id, _advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  token = nil
  begin
    result = service.list_creatives(profile_id,
      page_token: token,
      fields: 'nextPageToken,creatives(id,name,type)')

    # Display results.
    if result.creatives.any?
      result.creatives.each do |creative|
        puts format('Found %s creative with ID %d and name "%s".', creative.type, creative.id, creative.name)
      end

      token = result.next_page_token
    else
      # Stop paging if there are no more results.
      token = nil
    end
  end until token.to_s.empty?
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  get_creatives(args[:profile_id], args[:advertiser_id])
end
