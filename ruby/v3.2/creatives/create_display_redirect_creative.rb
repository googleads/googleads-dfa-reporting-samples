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
# This example creates a display redirect creative associated with a given
# advertiser.
#
# To get a size ID, run get_size.rb.

require_relative '../dfareporting_utils'

def create_tracking_creative(profile_id, advertiser_id, image_url, size_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Create a new creative resource to insert.
  creative = DfareportingUtils::API_NAMESPACE::Creative.new(
    advertiser_id: advertiser_id,
    name: 'Example display redirect creative',
    redirect_url: image_url,
    size: DfareportingUtils::API_NAMESPACE::Size.new(id: size_id),
    type: 'DISPLAY_REDIRECT'
  )

  # Insert the creative.
  result = service.insert_creative(profile_id, creative)

  # Display results.
  puts format('Created display redirect creative with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :advertiser_id,
    :image_url, :size_id)

  create_tracking_creative(args[:profile_id], args[:advertiser_id],
    args[:image_url], args[:size_id])
end
