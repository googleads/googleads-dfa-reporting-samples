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
# This example illustrates how to list all reports for a profile

require_relative '../dfareporting_utils'

def get_reports(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  token = nil
  begin
    result = service.list_reports(profile_id, {
      :page_token => token,
      :fields => 'nextPageToken,items(id,name,type)'
    })

    # Display results.
    if result.items.any?
      result.items.each do |report|
        puts '%s report with ID %d and name "%s" was found.' %
            [report.type, report.id, report.name]
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
  args = DfareportingUtils.get_arguments(ARGV, :profile_id)

  get_reports(args[:profile_id])
end
