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
# This example displays all of the available user role permissions for a given
# subaccount.
#
# To get a subaccount ID, run get_subaccounts.rb.

require_relative '../dfareporting_utils'

def get_subaccount_permissions(profile_id, subaccount_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  # Construct and execute the subaccount request.
  subaccount = service.get_subaccount(profile_id, subaccount_id)

  # Construct the user role permissions request.
  result = service.list_user_role_permissions(profile_id, {
    :ids => subaccount.available_permission_ids
  })

  result.user_role_permissions.each do |permission|
    puts 'Found user role permission with ID %d and name "%s".' %
        [permission.id, permission.name]
  end
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :subaccount_id)

  get_subaccount_permissions(args[:profile_id], args[:subaccount_id])
end
