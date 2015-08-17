#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2013, Google Inc. All Rights Reserved.
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

require_relative 'dfareporting_utils'

def get_dimension_values(dfareporting, args)
  # Create the dimension to query
  dimension = {
    :dimensionName => 'dfa:advertiser',
    :startDate => '2013-01-01',
    :endDate => '2013-12-31'
  }

  # Get all dimension values
  request = dfareporting.dimension_values.query(
    :profileId => args[:profile_id]
  ).body(dimension)

  loop do
    result = request.execute()

    # Display results
    result.data.items.each do |dimension|
      puts 'Dimension with ID %d and value "%s" was found.' %
          [dimension.id, dimension.value]
    end

    token = result.next_page_token

    break unless token and result.data.items.any?
    request = result.next_page
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_dimension_values(dfareporting, args)
end
