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
# This example displays all subaccounts.
#
# Note that the permissions assigned to a subaccount are not returned in a
# human-readable format with this example. Run get_available_permissions.rb to
# see what permissions are available on a subaccount.

require_relative '../dfareporting_utils'

def get_subaccounts(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  token = nil
  loop do
    result = service.list_subaccounts(profile_id,
      page_token: token,
      fields: 'nextPageToken,subaccounts(id,name)')

    # Display results.
    if result.subaccounts.any?
      result.subaccounts.each do |subaccount|
        puts format('Found subaccount with ID %d and name "%s".', subaccount.id,
          subaccount.name)
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
  args = DfareportingUtils.get_arguments(ARGV, :profile_id)

  get_subaccounts(args[:profile_id])
end
