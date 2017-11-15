#!/usr/bin/env ruby
# Encoding: utf-8
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
# This example updates an offline conversion attributed to an encrypted user
# ID.
#
# To create a conversion attributed to an encrypted user ID, run
# insert_offline_user_conversion.rb.

require_relative '../dfareporting_utils'

def update_offline_user_conversion(profile_id, encrypted_user_id,
    encryption_source, encryption_entity_id, encryption_entity_type,
    floodlight_activity_id, ordinal, timestamp, new_quantity, new_value)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  # Look up the Floodlight configuration ID based on activity ID.
  floodlight_activity = service.get_floodlight_activity(profile_id,
      floodlight_activity_id)
  floodlight_config_id = floodlight_activity.floodlight_configuration_id

  # Construct the conversion with values that identify the conversion to
  # update.
  conversion = DfareportingUtils::API_NAMESPACE::Conversion.new({
    :encrypted_user_id => encrypted_user_id,
    :floodlight_activity_id => floodlight_activity_id,
    :floodlight_configuration_id => floodlight_config_id,
    :ordinal => ordinal,
    :timestamp_micros => timestamp
  })

  # Set the fields to be updated. These fields are required; to preserve a
  # value from the existing conversion, it must be copied over manually.
  conversion.quantity = new_quantity
  conversion.value = new_value

  # Construct the encryption info.
  encryption_info = DfareportingUtils::API_NAMESPACE::EncryptionInfo.new({
    :encryption_entity_id => encryption_entity_id,
    :encryption_entity_type => encryption_entity_type,
    :encryption_source => encryption_source
  })

  # Construct the batch update request.
  batch_update_request =
      DfareportingUtils::API_NAMESPACE::ConversionsBatchUpdateRequest.new({
        :conversions => [conversion],
        :encryption_info => encryption_info
      })

  # Update the conversion.
  result = service.batchupdate_conversion(profile_id, batch_update_request)

  unless result.has_failures
    puts 'Successfully updated conversion for encrypted user ID %s.' %
        encrypted_user_id
  else
    puts 'Error(s) updating conversion for encrypted user ID %s.' %
        encrypted_user_id

    status = result.status[0]
    status.errors.each do |error|
      puts "\t[%s]: %s" % [error.code, error.message]
    end
  end
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :encrypted_user_id,
      :encryption_source, :encryption_entity_id, :encryption_entity_type,
      :floodlight_activity_id, :ordinal, :timestamp, :new_quantity, :new_value)

  update_offline_user_conversion(args[:profile_id], args[:encrypted_user_id],
      args[:encryption_source], args[:encryption_entity_id],
      args[:encryption_entity_type], args[:floodlight_activity_id],
      args[:ordinal], args[:timestamp], args[:new_quantity], args[:new_value])
end
