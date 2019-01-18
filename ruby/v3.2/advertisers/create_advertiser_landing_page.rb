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
# This example creates an advertiser landing page.

require_relative '../dfareporting_utils'
require 'securerandom'

def create_advertiser_landing_page(profile_id, advertiser_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Create a new landing page resource to insert.
  landing_page = DfareportingUtils::API_NAMESPACE::LandingPage.new(
    advertiser_id: advertiser_id,
    archived: false,
    name: format('Example Advertiser Landing Page #%s', SecureRandom.hex(3)),
    url: 'https://www.google.com'
  )

  # Insert the advertiser landing page.
  result = service.insert_advertiser_landing_page(profile_id, landing_page)

  # Display results.
  puts format('Created advertiser landing page with ID %d and name "%s".', result.id, result.name)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :advertiser_id)

  create_advertiser_landing_page(args[:profile_id], args[:advertiser_id])
end
