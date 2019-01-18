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
# This example creates a content category with the given name and description.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_content_category(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Create a new content category resource to insert.
  content_category = DfareportingUtils::API_NAMESPACE::ContentCategory.new(
    name: format('Example Content Category #%s', SecureRandom.hex(3))
  )

  # Insert the content category.
  result = service.insert_content_category(profile_id, content_category)

  # Display results.
  puts format('Created content category with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id)

  create_content_category(args[:profile_id])
end
