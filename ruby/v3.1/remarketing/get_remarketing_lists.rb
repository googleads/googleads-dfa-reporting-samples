#!/usr/bin/env ruby
# Encoding: utf-8
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
# Displays all remarketing lists owned by the specified advertiser.
#
# Note: the RemarketingLists resource will only return lists owned by the
# specified advertiser. To see all lists that can be used for targeting ads
# (including those shared from other accounts or advertisers), use the
# TargetableRemarketingLists resource instead.

require_relative '../dfareporting_utils'

def get_remarketing_lists(profile_id, advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  token = nil
  begin
    result = service.list_remarketing_lists(profile_id, advertiser_id, {
      :page_token => token,
      :fields => 'nextPageToken,remarketingLists(id,name)'
    })

    # Display results.
    if result.remarketing_lists.any?
      result.remarketing_lists.each do |list|
        puts 'Found remarketing list with ID %d and name "%s".' %
            [list.id, list.name]
      end

      token = result.next_page_token
    else
      # Stop paging if there are no more results.
      token = nil
    end
  end until token.to_s.empty?
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  get_remarketing_lists(args[:profile_id], args[:advertiser_id])
end
