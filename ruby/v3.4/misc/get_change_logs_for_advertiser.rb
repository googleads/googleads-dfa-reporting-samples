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
# This example displays the change logs of a specified advertiser object.
#
# A similar pattern can be applied to get change logs for many other object
# types.

require_relative '../dfareporting_utils'

def get_change_logs_for_advertiser(profile_id, advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  token = nil
  loop do
    result = service.list_change_logs(profile_id,
      object_ids: [advertiser_id],
      object_type: 'OBJECT_ADVERTISER',
      page_token: token,
      fields: 'nextPageToken,changeLogs(action,fieldName,oldValue,newValue)')

    # Display results.
    if result.change_logs.any?
      result.change_logs.each do |log|
        puts format('%s: Field "%s" from "%s" to "%s".', log.action,
          log.field_name, log.old_value, log.new_value)
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
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :advertiser_id)

  get_change_logs_for_advertiser(args[:profile_id], args[:advertiser_id])
end
