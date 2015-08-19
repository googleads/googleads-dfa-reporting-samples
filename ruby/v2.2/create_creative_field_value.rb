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
# This example creates a creative field value for a given creative field.
#
# To get the creative field ID, run get_creative_fields.rb.

require_relative 'dfareporting_utils'

def create_creative_field_value(dfareporting, args)
  # Create a new creative field value resource to insert
  field_value = {
    :value => 'Example Creative Field Value'
  }

  # Insert the creative field value
  result = dfareporting.creative_field_values.insert(
    :creativeFieldId => args[:field_id],
    :profileId => args[:profile_id]
  ).body(field_value).execute()

  # Display results
  puts 'Created creative field value with ID %d and value "%s".' %
      [result.data.id, result.data.value]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :field_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_creative_field_value(dfareporting, args)
end
