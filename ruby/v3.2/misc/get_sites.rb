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
# This example displays all existing sites for the current DCM account.

require_relative '../dfareporting_utils'

def get_subaccounts(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  token = nil
  loop do
    result = service.list_sites(profile_id,
      page_token: token,
      fields: 'nextPageToken,sites(id,keyName)')

    # Display results.
    if result.sites.any?
      result.sites.each do |site|
        puts format('Found site with ID %d and key name "%s".',
          site.id, site.key_name)
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
