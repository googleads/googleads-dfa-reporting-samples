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
# Handles common tasks across all DFA Reporting API samples.

require 'google/apis/dfareporting_v5'
require 'googleauth'
require 'googleauth/stores/file_token_store'

# Utility methods used by all DFA Reporting and Trafficking API samples.
module DfareportingUtils
  API_NAME = 'dfareporting'.freeze
  API_NAMESPACE = Google::Apis::DfareportingV5
  API_SCOPES = [
    API_NAMESPACE::AUTH_DDMCONVERSIONS,
    API_NAMESPACE::AUTH_DFAREPORTING,
    API_NAMESPACE::AUTH_DFATRAFFICKING
  ].freeze

  CLIENT_SECRETS_FILE = 'client_secrets.json'.freeze
  CREDENTIAL_STORE_FILE = "#{API_NAME}-oauth2.yaml".freeze
  CREDENTIAL_STORE_PATH = File.dirname(__FILE__)

  # This redirect URI allows you to copy the token from the success screen.
  OAUTH_REDIRECT_URI = 'localhost'.freeze

  # Handles validating command line arguments and returning them as a Hash
  def self.parse_arguments(argument_values, *argument_names)
    validate_arguments(argument_values, *argument_names)
    generate_argument_map(argument_values, *argument_names)
  end

  # Validates the number of command line arguments matches what was expected
  def self.validate_arguments(argument_values, *argument_names)
    return if argument_values.length == argument_names.length

    # Format the arguments for display (ie, '<profile_id>')
    formatted_arguments = argument_names.map { |a| '<' + a.to_s + '>' }

    # Display a message to the user and exit
    puts format('Usage: %s %s', $PROGRAM_NAME, formatted_arguments.join(' '))
    exit
  end
  private_class_method :validate_arguments

  # Converts parallel arrays of argument names and values into a single map
  def self.generate_argument_map(argument_values, *argument_names)
    ret = {}
    argument_names.each_with_index do |arg, index|
      ret[arg] = argument_values[index]
    end
    ret
  end
  private_class_method :generate_argument_map

  # Handles authentication and loading of the API.
  def self.initialize_service
    # Uncomment the following lines to enable logging.
    # log_file = File.open("#{$0}.log", 'a+')
    # log_file.sync = true
    # logger = Logger.new(log_file)
    # logger.level = Logger::DEBUG
    # Google::Apis.logger = logger # Logging is set globally

    # Create an API Service object.
    service = create_service_object

    # Load application default credentials if they're available.
    authorization = authorize_application_default_credentials

    # Otherwise, load credentials from the provided client secrets file.
    authorization = authorize_installed_application if authorization.nil?

    # If no credentials could be loaded, return an error.
    if authorization.nil?
      puts 'Could not load credentials. Enter client ID and secret from ' \
           'https://console.developers.google.com/ into client_secrets.json.'
      exit
    end

    service.authorization = authorization
    service
  end

  # Returns an instance of the Dfareporting service without authentication.
  def self.create_service_object
    service = API_NAMESPACE::DfareportingService.new
    service.client_options.application_name = "Ruby #{API_NAME} samples"
    service.client_options.application_version = '1.0.0'

    service
  end
  private_class_method :create_service_object

  # Attempts to load application default credentials and return an
  # authorization object that can be used to make requests.
  def self.authorize_application_default_credentials
    Google::Auth.get_application_default(API_SCOPES)
  rescue StandardError
    # No application default credentials, continue to try other options.
    nil
  end
  private_class_method :authorize_application_default_credentials

  # Handles authorizing a user via the OAuth installed application flow and
  # returns an authorization object that can be used to make requests.
  def self.authorize_installed_application
    # Load the client secrets.
    client_id = load_client_secrets
    return nil if client_id.nil?

    # FileTokenStore stores auth credentials in a file, so they survive
    # multiple runs of the application. This avoids prompting the user for
    # authorization every time the access token expires, by remembering the
    # refresh token.
    #
    # Note: FileTokenStore is not suitable for multi-user applications.
    token_store = Google::Auth::Stores::FileTokenStore.new(
      file: File.join(CREDENTIAL_STORE_PATH, CREDENTIAL_STORE_FILE)
    )

    authorizer = Google::Auth::UserAuthorizer.new(client_id, API_SCOPES,
      token_store)

    authorization = authorizer.get_credentials('default')
    if authorization.nil?
      puts format(
        "Open this URL in your browser and authorize the application.\n\n%s" \
        "\n\nEnter the authorization code:",
        authorizer.get_authorization_url(base_url: OAUTH_REDIRECT_URI)
      )
      code = STDIN.gets.chomp
      authorization = authorizer.get_and_store_credentials_from_code(
        base_url: OAUTH_REDIRECT_URI, code: code, user_id: 'default'
      )
    end

    authorization
  end
  private_class_method :authorize_installed_application

  def self.load_client_secrets
    # Load client ID from the specified file.
    client_id = Google::Auth::ClientId.from_file(
      File.join(CREDENTIAL_STORE_PATH, CLIENT_SECRETS_FILE)
    )

    if client_id.id.start_with?('[[INSERT') ||
       client_id.secret.start_with?('[[INSERT')
      return nil
    end

    client_id
  rescue StandardError
    # Unable to load client_secrets.json.
    nil
  end
  private_class_method :load_client_secrets
end
