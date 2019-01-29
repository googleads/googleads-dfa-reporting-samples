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
# This example displays all user roles.

require_relative '../dfareporting_utils'

def get_user_roles(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  token = nil
  loop do
    result = service.list_user_roles(profile_id,
      page_token: token,
      fields: 'nextPageToken,userRoles(id,name)')

    # Display results.
    if result.user_roles.any?
      result.user_roles.each do |role|
        puts format('Found user role with ID %d and name "%s".', role.id,
          role.name)
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
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id)

  get_user_roles(args[:profile_id])
end
