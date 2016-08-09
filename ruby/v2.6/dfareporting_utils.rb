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
# Handles common tasks across all DFA Reporting API samples.

require 'google/api_client'
require 'google/api_client/client_secrets'
require 'google/api_client/service'
require 'google/api_client/auth/installed_app'
require 'google/api_client/auth/file_storage'

API_NAME = 'dfareporting'
API_VERSION = 'v2.6'
API_SCOPES = [
  'https://www.googleapis.com/auth/dfareporting',
  'https://www.googleapis.com/auth/dfatrafficking',
  'https://www.googleapis.com/auth/ddmconversions'
]
CREDENTIAL_STORE_FILE = "#{API_NAME}-oauth2.json"

class DfaReportingUtils

  # Handles validating command line arguments and returning them as a Hash
  def self.get_arguments(argument_values, *argument_names)
    self.validate_arguments(argument_values, *argument_names)
    return self.generate_argument_map(argument_values, *argument_names)
  end

  # Validates the number of command line arguments matches what was expected
  def self.validate_arguments(argument_values, *argument_names)
    unless argument_values.length == argument_names.length
      # Format the arguments for display (ie, '<profile_id>')
      formatted_arguments = argument_names.map { |a| '<' + a.to_s + '>' }

      # Display a message to the user and exit
      puts 'Usage: %s %s' % [$0, formatted_arguments.join(' ')]
      exit
    end
  end

  # Converts parallel arrays of argument names and values into a single map
  def self.generate_argument_map(argument_values, *argument_names)
    ret = {}
    argument_names.each_with_index do |arg,index|
      ret[arg] = argument_values[index]
    end
    return ret
  end

  # Handles authentication and loading of the API.
  def self.setup()
    # Uncomment the following lines to enable logging.
    # log_file = File.open("#{$0}.log", 'a+')
    # log_file.sync = true
    # logger = Logger.new(log_file)
    # logger.level = Logger::DEBUG
    # Google::APIClient.logger = logger # Logging is set globally

    authorization = nil
    # FileStorage stores auth credentials in a file, so they survive multiple
    # runs of the application. This avoids prompting the user for authorization
    # every time the access token expires, by remembering the refresh token.
    #
    # Note: FileStorage is not suitable for multi-user applications.
    file_storage = Google::APIClient::FileStorage.new(CREDENTIAL_STORE_FILE)
    if file_storage.authorization.nil?
      client_secrets = Google::APIClient::ClientSecrets.load
      # The InstalledAppFlow is a helper class to handle the OAuth 2.0 installed
      # application flow, which ties in with FileStorage to store credentials
      # between runs.
      flow = Google::APIClient::InstalledAppFlow.new(
        :client_id => client_secrets.client_id,
        :client_secret => client_secrets.client_secret,
        :scope => API_SCOPES
      )
      authorization = flow.authorize(file_storage)
    else
      authorization = file_storage.authorization
    end

    # Initialize API Service.
    #
    # Note: the client library automatically creates a cache file for discovery
    # documents, to avoid calling the discovery service on every invocation.
    # To set this to an ActiveSupport cache store, use the :cache_store
    # parameter (or, alternatively, set it to nil if you want to disable
    # caching).
    service = Google::APIClient::Service.new(API_NAME, API_VERSION,
        {
          :application_name => "Ruby #{API_NAME} samples: #{$0}",
          :application_version => '1.0.0',
          :authorization => authorization
        }
    )

    return service
  end
end
