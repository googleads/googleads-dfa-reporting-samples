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
# This example displays all subaccounts.
#
# Note that the permissions assigned to a subaccount are not returned in a
# human-readable format with this example. Run get_available_permissions.rb to
# see what permissions are available on a subaccount.

require_relative 'dfareporting_utils'

def get_subaccounts(dfareporting, args)

  # Construct the request
  request = dfareporting.sites.list(
    :profileId => args[:profile_id],
    # Limit the fields returned
    :fields => 'nextPageToken,sites(id,keyName)'
  )

  loop do
    result = request.execute()

    # Display results
    result.data.sites.each do |site|
      puts 'Found site with ID %d and key name "%s".' % [site.id, site.keyName]
    end

    token = result.next_page_token

    break unless token and result.data.sites.any?
    request = result.next_page
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_subaccounts(dfareporting, args)
end
