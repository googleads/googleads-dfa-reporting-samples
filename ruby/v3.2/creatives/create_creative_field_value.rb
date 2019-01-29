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
# This example creates a creative field value for a given creative field.
#
# To get the creative field ID, run get_creative_fields.rb.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_creative_field_value(profile_id, field_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new creative field value resource to insert.
  field_value = DfareportingUtils::API_NAMESPACE::CreativeFieldValue.new(
    value: format('Example Creative Field Value #%s', SecureRandom.hex(3))
  )

  # Insert the creative field value.
  result = service.insert_creative_field_value(profile_id, field_id,
    field_value)

  # Display results.
  puts format('Created creative field value with ID %d and value "%s".',
    result.id, result.value)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :field_id)

  create_creative_field_value(args[:profile_id], args[:field_id])
end
