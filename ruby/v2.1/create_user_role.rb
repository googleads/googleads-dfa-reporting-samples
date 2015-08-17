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
# This example creates a user role in a given DFA account.
#
# To get the account ID, run get_all_userprofiles.rb. To get the parent user role
# ID, run get_user_roles.rb.

require_relative 'dfareporting_utils'

def create_user_role(dfareporting, args)
  # Create a new user role resource to insert
  user_role = {
    :accountId => args[:account_id],
    :name => 'Example User Role',
    :parentUserRoleId => args[:parent_role_id]
  }

  # Insert the user role
  result = dfareporting.user_roles.insert(
    :profileId => args[:profile_id]
  ).body(user_role).execute()

  # Display results
  puts 'Created user role with ID %d and name "%s".' %
      [result.data.id, result.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :account_id, :parent_role_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_user_role(dfareporting, args)
end
