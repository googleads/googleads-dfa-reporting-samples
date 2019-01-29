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
# This example creates a remarketing list for a given advertiser and floodight
# activity, using a custom rule.
#
# Note: this sample assumes that the floodlight activity specified has a U1
# custom floodlight variable.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_remarketing_list(profile_id, advertiser_id, floodlight_activity_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a list population term.
  # This term matches all visitors with a U1 value exactly matching
  # "test_value"
  term = DfareportingUtils::API_NAMESPACE::ListPopulationTerm.new(
    operator: 'STRING_EQUALS',
    type: 'CUSTOM_VARIABLE_TERM',
    value: 'test_value',
    variable_name: 'U1'
  )

  # Add the term to a clause and the clause to a population rule.
  # This rule will target all visitors who trigger the specified floodlight
  # activity and satisfy the custom rule defined in the list population term.
  rule = DfareportingUtils::API_NAMESPACE::ListPopulationRule.new(
    floodlight_activity_id: floodlight_activity_id,
    list_population_clauses: [
      DfareportingUtils::API_NAMESPACE::ListPopulationClause.new(
        terms: [term]
      )
    ]
  )

  # Create the remarketing list.
  list = DfareportingUtils::API_NAMESPACE::RemarketingList.new(
    name: format('Test remarketing list #%s', SecureRandom.hex(3)),
    active: true,
    advertiser_id: advertiser_id,
    life_span: 30,
    list_population_rule: rule
  )

  # Insert the remarketing list.
  list = service.insert_remarketing_list(profile_id, list)

  # Display results.
  puts format('Remarketing list with ID %d and name "%s" was created.', list.id, list.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :advertiser_id,
    :floodlight_activity_id)

  create_remarketing_list(args[:profile_id], args[:advertiser_id],
    args[:floodlight_activity_id])
end
