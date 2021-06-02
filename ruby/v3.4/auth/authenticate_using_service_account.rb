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
# This example demonstrates how to authenticate using a service account.
#
# An optional Google account email to impersonate may be specified as follows:
#    authenticate_using_service_account.rb <path_to_json_file> --i <email>
#
# This optional flag only applies to service accounts which have domain-wide
# delegation enabled and wish to make API requests on behalf of an account
# within that domain. Using this flag will not allow you to impersonate a
# user from a domain you don't own (e.g., gmail.com).

require 'google/apis/dfareporting_v3_4'
require 'googleauth'
require 'optparse'

API_NAMESPACE = Google::Apis::DfareportingV3_4

def authenticate_using_service_account(path_to_json_file, impersonation_email)
  # Create a Dfareporting service object.
  #
  # Note: application name should be replaced with a value that identifies
  # your application. Suggested format is "MyCompany-ProductName".
  service = API_NAMESPACE::DfareportingService.new
  service.client_options.application_name = 'Ruby service account sample'
  service.client_options.application_version = '1.0.0'

  # Generate an authorization object from the specified JSON file.
  File.open(path_to_json_file, 'r+') do |json|
    service.authorization =
      Google::Auth::ServiceAccountCredentials.make_creds(
        json_key_io: json,
        scope: [API_NAMESPACE::AUTH_DFAREPORTING]
      )
  end

  # Configure impersonation (if applicable).
  service.authorization.sub = impersonation_email unless
    impersonation_email.nil?

  service
end

def get_userprofiles(service)
  # Get all user profiles.
  result = service.list_user_profiles

  # Display results.
  result.items.each do |profile|
    puts format(
      'User profile with ID %d and name "%s" was found for account %d.',
      profile.profile_id, profile.user_name, profile.account_id
    )
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  impersonation_email = nil
  optparse = OptionParser.new do |opts|
    opts.banner = format('Usage: %s path_to_json_file [options]', $PROGRAM_NAME)
    opts.on_tail('-i', '--impersonate EMAIL',
      'Google account email to impersonate') do |email|
      impersonation_email = email
    end
  end
  optparse.parse!

  if ARGV.empty?
    puts optparse
    exit(-1)
  end

  # Authenticate and initialize API service using service account.
  service = authenticate_using_service_account(ARGV.shift, impersonation_email)

  get_userprofiles(service)
end
