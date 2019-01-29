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
# This example inserts an offline conversion attributed to an encrypted user
# ID.

require_relative '../dfareporting_utils'
require 'date'

# Inserts an offline user conversions with the specified values.
#
# @param profile_id [Number] The ID of the DCM user issuing this request.
# @param encrypted_user_id [String] The encrypted user ID to which the
#   conversion should be attributed.
# @param floodlight_activity_id [Number] The Floodlight activity ID to which
#   the conversion should be attributed.
# @param encryption [Object] A hash containing the values used to encrypt the
#   specified user ID. The expected format is:
#      {
#        source: <The encryption source>,
#        entity_id: <The encryption entity ID>,
#        entity_type: <The encryption entity type>
#      }
def insert_offline_user_conversion(profile_id, encrypted_user_id,
  floodlight_activity_id, encryption = {})
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Look up the Floodlight configuration ID based on activity ID.
  floodlight_activity = service.get_floodlight_activity(profile_id,
    floodlight_activity_id)
  floodlight_config_id = floodlight_activity.floodlight_configuration_id

  current_time_in_micros = DateTime.now.strftime('%Q').to_i * 1000

  # Construct the conversion.
  conversion = DfareportingUtils::API_NAMESPACE::Conversion.new(
    encrypted_user_id: encrypted_user_id,
    floodlight_activity_id: floodlight_activity_id,
    floodlight_configuration_id: floodlight_config_id,
    ordinal: current_time_in_micros,
    timestamp_micros: current_time_in_micros
  )

  # Construct the encryption info.
  encryption_info = DfareportingUtils::API_NAMESPACE::EncryptionInfo.new(
    encryption_entity_id: encryption[:entity_id],
    encryption_entity_type: encryption[:entity_type],
    encryption_source: encryption[:source]
  )

  # Construct the batch insert request.
  batch_insert_request =
    DfareportingUtils::API_NAMESPACE::ConversionsBatchInsertRequest.new(
      conversions: [conversion],
      encryption_info: encryption_info
    )

  # Insert the conversion.
  result = service.batchinsert_conversion(profile_id, batch_insert_request)

  process_response(result)
end

def process_response(result)
  if result.has_failures
    puts format('Error(s) inserting conversion for encrypted user ID %s.',
      encrypted_user_id)

    status = result.status[0]
    status.errors.each do |error|
      puts format("\t[%s]: %s", error.code, error.message)
    end
  else
    puts format('Successfully inserted conversion for encrypted user ID %s.',
      encrypted_user_id)
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :encrypted_user_id,
    :encryption_source, :encryption_entity_id, :encryption_entity_type,
    :floodlight_activity_id)

  insert_offline_user_conversion(args[:profile_id], args[:encrypted_user_id],
    args[:floodlight_activity_id],
    source: args[:encryption_source],
    entity_id: args[:encryption_entity_id],
    entity_type: args[:encryption_entity_type])
end
