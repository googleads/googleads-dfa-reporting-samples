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
# This example creates a subaccount in a given DCM account.
#
# To get the account ID, run get_all_userprofiles.rb. To get the available
# permissions, run get_subaccount_permissions.rb.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_subaccount(profile_id, account_id, permission_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new subaccount resource to insert.
  subaccount = DfareportingUtils::API_NAMESPACE::Subaccount.new(
    account_id: account_id,
    available_permission_ids: [permission_id],
    name: format('Example Subaccount #%s', SecureRandom.hex(3))
  )

  # Insert the subaccount.
  result = service.insert_subaccount(profile_id, subaccount)

  # Display results.
  puts format('Created subaccount with ID %d and name "%s".', result.id,
    result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :account_id,
    :permission_id)

  create_subaccount(args[:profile_id], args[:account_id], args[:permission_id])
end
