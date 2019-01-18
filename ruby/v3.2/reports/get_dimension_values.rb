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
# This example illustrates how to list all values for a dimension

require_relative '../dfareporting_utils'
require 'date'

def get_dimension_values(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Create the dimension to query.
  dimension = DfareportingUtils::API_NAMESPACE::DimensionValueRequest.new(
    dimension_name: 'dfa:advertiser',
    start_date: DateTime.now.prev_year.strftime('%Y-%m-%d'),
    end_date: DateTime.now.strftime('%Y-%m-%d')
  )

  token = nil
  loop do
    result = service.query_dimension_value(
      profile_id, dimension,
      page_token: token,
      fields: 'nextPageToken,items(id,value)'
    )

    # Display results.
    if result.items.any?
      result.items.each do |dimension_value|
        puts format('Dimension with ID %d and value "%s" was found.',
          dimension_value.id, dimension_value.value)
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
  args = DfareportingUtils.get_arguments(ARGV, :profile_id)

  get_dimension_values(args[:profile_id])
end
