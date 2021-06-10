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
# This example demonstrates how to authenticate using a user account, via the
# OAuth 2.0 installed application flow.

require 'google/apis/dfareporting_v3_5'
require 'googleauth'
require 'googleauth/stores/file_token_store'

API_NAMESPACE = Google::Apis::DfareportingV3_5

# This redirect URI allows you to copy the token from the success screen.
OAUTH_REDIRECT_URI = 'urn:ietf:wg:oauth:2.0:oob'.freeze

# Location where authorization credentials will be cached.
TOKEN_STORE_DIR = File.join(File.dirname(__FILE__), 'auth-sample.yaml')

def authenticate_using_user_account(path_to_json_file, token_store)
  # Load client ID from the specified file.
  client_id = Google::Auth::ClientId.from_file(path_to_json_file)

  # Set up the user authorizer.
  #
  # Note: providing a token store allows auth credentials to be cached, so they
  # survive multiple runs of the application. This avoids prompting the user for
  # authorization every time the access token expires, by remembering the
  # refresh token.
  authorizer = Google::Auth::UserAuthorizer.new(
    client_id, [API_NAMESPACE::AUTH_DFAREPORTING], token_store
  )

  # Authorize and persist credentials to the data store.
  #
  # Note: the 'user' value below is used to identify a specific set of
  # credentials in the token store. You may provide different values here to
  # persist credentials for multiple users to the same token store.
  authorization = authorizer.get_credentials('user')
  if authorization.nil?
    puts format(
      "Open this URL in your browser and authorize the application.\n\n%s" \
      "\n\nEnter the authorization code:",
      authorizer.get_authorization_url(base_url: OAUTH_REDIRECT_URI)
    )
    code = STDIN.gets.chomp
    authorization = authorizer.get_and_store_credentials_from_code(
      base_url: OAUTH_REDIRECT_URI, code: code, user_id: 'user'
    )
  end

  authorization
end

def create_dfareporting_service_instance(authorization)
  # Create a Dfareporting service object.
  #
  # Note: application name should be replaced with a value that identifies
  # your application. Suggested format is "MyCompany-ProductName".
  service = API_NAMESPACE::DfareportingService.new
  service.authorization = authorization
  service.client_options.application_name = 'Ruby installed app sample'
  service.client_options.application_version = '1.0.0'

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
  if ARGV.empty?
    puts format('Usage: %s path_to_json_file', $PROGRAM_NAME)
    exit(-1)
  end

  # Authenticate using user account.
  authorization = authenticate_using_user_account(
    ARGV.shift,
    Google::Auth::Stores::FileTokenStore.new(file: TOKEN_STORE_DIR)
  )

  # Initialize API service,
  service = create_dfareporting_service_instance(authorization)

  get_userprofiles(service)
end
