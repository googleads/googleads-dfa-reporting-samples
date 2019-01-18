#!/usr/bin/env ruby

#
# Copyright:: Copyright 2017, Google Inc. All Rights Reserved.
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
# This example displays all advertiser landing pages for the specified user
# profile.

require_relative '../dfareporting_utils'

def get_advertiser_landing_pages(profile_id, advertiser_id)
  # Authenticate and initialize API service
  service = DfareportingUtils.get_service

  token = nil
  loop do
    result = service.list_advertiser_landing_pages(profile_id,
      advertiser_ids: [advertiser_id],
      page_token: token,
      fields: 'nextPageToken,landingPages(id,name)')

    # Display results.
    if result.landing_pages.any?
      result.landing_pages.each do |landing_page|
        puts format('Found advertiser landing page with ID %d and name "%s".',
          landing_page.id, landing_page.name)
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
  # Retrieve command line arguments
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  get_advertiser_landing_pages(args[:profile_id], args[:advertiser_id])
end
