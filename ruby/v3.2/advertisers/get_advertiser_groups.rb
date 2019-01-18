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
# This example displays all advertiser groups for the specified user profile.

require_relative '../dfareporting_utils'

def get_advertiser_groups(profile_id)
  # Authenticate and initialize API service
  service = DfareportingUtils.get_service

  token = nil
  begin
    result = service.list_advertiser_groups(profile_id,
      page_token: token,
      fields: 'nextPageToken,advertiserGroups(id,name)')

    # Display results.
    if result.advertiser_groups.any?
      result.advertiser_groups.each do |group|
        puts format('Found advertiser group with ID %d and name "%s".', group.id, group.name)
      end

      token = result.next_page_token
    else
      # Stop paging if there are no more results.
      token = nil
    end
  end until token.to_s.empty?
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments
  args = DfareportingUtils.get_arguments(ARGV, :profile_id)

  get_advertiser_groups(args[:profile_id])
end
