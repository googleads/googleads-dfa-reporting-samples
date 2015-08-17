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
# This example shows how to authenticate using a service account.

require_relative 'dfareporting_utils'


def authenticate_using_service_account(args)
  key = Google::APIClient::KeyUtils.load_from_pkcs12(
      args[:path_to_p12_file], 'notasecret')

  authorization = Signet::OAuth2::Client.new(
    :token_credential_uri => 'https://accounts.google.com/o/oauth2/token',
    :audience => 'https://accounts.google.com/o/oauth2/token',
    :scope => 'https://www.googleapis.com/auth/dfareporting',
    :issuer => args[:service_account_email],
    :signing_key => key,
    :sub => args[:impersonation_user_email]
  )

  authorization.fetch_access_token!

  return Google::APIClient::Service.new(API_NAME, API_VERSION,
      {
        :application_name => "Ruby %s samples: %s" % [API_NAME, $0],
        :application_version => '1.0.0',
        :authorization => authorization
      }
  )
end

def get_userprofiles(dfareporting)
  # Get all user profiles
  result = dfareporting.user_profiles.list.execute()

  # Display results
  result.data.items.each do |profile|
    puts 'User profile with ID %d and name "%s" was found for account %d.' %
        [profile.profileId, profile.userName, profile.accountId]
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :service_account_email, :impersonation_user_email,
      :path_to_p12_file)

  # Authenticate and initialize API service using service account
  dfareporting = authenticate_using_service_account(args)

  get_userprofiles(dfareporting)
end
