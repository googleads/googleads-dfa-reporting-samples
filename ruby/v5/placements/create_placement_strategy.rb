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
# This example creates a placement strategy with the given name.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_placement_strategy(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new placement strategy resource to insert.
  strategy = DfareportingUtils::API_NAMESPACE::PlacementStrategy.new(
    name: format('Example Placement Strategy #%s', SecureRandom.hex(3))
  )

  # Insert the placement strategy.
  result = service.insert_placement_strategy(profile_id, strategy)

  # Display results.
  puts format('Created placement strategy with ID %d and name "%s".',
    result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id)

  create_placement_strategy(args[:profile_id])
end
