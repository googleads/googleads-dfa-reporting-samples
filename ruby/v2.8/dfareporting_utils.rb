#!/usr/bin/env ruby
# Encoding: utf-8
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
# Handles common tasks across all DFA Reporting API samples.

require 'google/apis/dfareporting_v2_8'
require 'googleauth'
require 'googleauth/stores/file_token_store'

module DfareportingUtils
  API_NAME = 'dfareporting'
  API_NAMESPACE = Google::Apis::DfareportingV2_8
  API_SCOPES = [
    API_NAMESPACE::AUTH_DDMCONVERSIONS,
    API_NAMESPACE::AUTH_DFAREPORTING,
    API_NAMESPACE::AUTH_DFATRAFFICKING
  ]

  CLIENT_SECRETS_FILE = "client_secrets.json"
  CREDENTIAL_STORE_FILE = "#{API_NAME}-oauth2.yaml"
  CREDENTIAL_STORE_PATH = File.dirname(__FILE__)

  # This redirect URI allows you to copy the token from the success screen.
  OAUTH_REDIRECT_URI = 'urn:ietf:wg:oauth:2.0:oob'

  # Handles validating command line arguments and returning them as a Hash
  def self.get_arguments(argument_values, *argument_names)
    validate_arguments(argument_values, *argument_names)
    return generate_argument_map(argument_values, *argument_names)
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
  private_class_method :validate_arguments

  # Converts parallel arrays of argument names and values into a single map
  def self.generate_argument_map(argument_values, *argument_names)
    ret = {}
    argument_names.each_with_index do |arg,index|
      ret[arg] = argument_values[index]
    end
    return ret
  end
  private_class_method :generate_argument_map

  # Handles authentication and loading of the API.
  def self.get_service()
    # Uncomment the following lines to enable logging.
    # log_file = File.open("#{$0}.log", 'a+')
    # log_file.sync = true
    # logger = Logger.new(log_file)
    # logger.level = Logger::DEBUG
    # Google::Apis.logger = logger # Logging is set globally

    # Initialize API Service.
    service = get_dfareporting_service_instance()
    service.authorization = authorize_installed_application()

    return service
  end

  # Returns an instance of the Dfareporting service without authentication.
  def self.get_dfareporting_service_instance()
    service = API_NAMESPACE::DfareportingService.new
    service.client_options.application_name = "Ruby #{API_NAME} samples"
    service.client_options.application_version = '1.0.0'

    return service
  end
  private_class_method :get_dfareporting_service_instance

  # Handles authorizing a user via the OAuth installed application flow and
  # returns an authorization object that can be used to make requests.
  def self.authorize_installed_application()
    # Load client ID from the specified file.
    client_id = Google::Auth::ClientId.from_file(
        File.join(CREDENTIAL_STORE_PATH, CLIENT_SECRETS_FILE))

    # FileTokenStore stores auth credentials in a file, so they survive
    # multiple runs of the application. This avoids prompting the user for
    # authorization every time the access token expires, by remembering the
    # refresh token.
    #
    # Note: FileTokenStore is not suitable for multi-user applications.
    token_store = Google::Auth::Stores::FileTokenStore.new(
        file: File.join(CREDENTIAL_STORE_PATH, CREDENTIAL_STORE_FILE))

    authorizer = Google::Auth::UserAuthorizer.new(client_id, API_SCOPES,
        token_store)

    authorization = authorizer.get_credentials('default')
    if authorization.nil?
      puts "Open this URL in your browser and authorize the application."
      puts
      puts authorizer.get_authorization_url(base_url: OAUTH_REDIRECT_URI)
      puts
      puts "Enter the authorization code:"
      code = STDIN.gets.chomp
      authorization = authorizer.get_and_store_credentials_from_code(
          base_url: OAUTH_REDIRECT_URI, code: code, user_id: 'default')
    end

    return authorization
  end
  private_class_method :authorize_installed_application
end
