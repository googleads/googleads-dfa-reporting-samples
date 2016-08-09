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
# This example inserts an offline conversion attributed to an encrypted user
# ID.

require 'date'
require_relative 'dfareporting_utils'

def insert_offline_user_conversion(dfareporting, args)
  # Look up the Floodlight configuration ID based on activity ID.
  floodlight_activity = dfareporting.floodlight_activities.get({
    :profileId => args[:profile_id],
    :id => args[:floodlight_activity_id]
  }).execute()
  floodlight_config_id = floodlight_activity.data.floodlight_configuration_id

  current_time_in_micros = DateTime.now.strftime('%Q').to_i * 1000

  # Construct the conversion.
  conversion = {
    :encryptedUserId => args[:encrypted_user_id],
    :floodlightActivityId => args[:floodlight_activity_id],
    :floodlightConfigurationId => floodlight_config_id,
    :ordinal => current_time_in_micros,
    :timestampMicros => current_time_in_micros
  }

  # Construct the encryption info.
  encryption_info = {
    :encryptionEntityId => args[:encryption_entity_id],
    :encryptionEntityType => args[:encryption_entity_type],
    :encryptionSource => args[:encryption_source]
  }

  # Insert the conversion.
  result = dfareporting.conversions.batchinsert({
    :profileId => args[:profile_id]
  }).body({
    :conversions => [conversion],
    :encryptionInfo => encryption_info
  }).execute()

  if not result.data.has_failures
    puts 'Successfully inserted conversion for encrypted user ID %s.' %
        args[:encrypted_user_id]
  else
    puts 'Error(s) inserting conversion for encrypted user ID %s.' %
        args[:encrypted_user_id]

    status = result.data.status[0]
    status.errors.each do |error|
      puts "\t[%s]: %s" % [error.code, error.message]
    end
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(
      ARGV, :profile_id, :encrypted_user_id, :encryption_source,
      :encryption_entity_id, :encryption_entity_type, :floodlight_activity_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  insert_offline_user_conversion(dfareporting, args)
end
