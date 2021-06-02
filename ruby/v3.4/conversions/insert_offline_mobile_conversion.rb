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
# This example inserts an offline conversion attributed to a mobile device ID.

require_relative '../dfareporting_utils'
require 'date'

def insert_offline_mobile_conversion(profile_id, mobile_device_id,
  floodlight_activity_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Look up the Floodlight configuration ID based on activity ID.
  floodlight_activity = service.get_floodlight_activity(profile_id,
    floodlight_activity_id)
  floodlight_config_id = floodlight_activity.floodlight_configuration_id

  current_time_in_micros = DateTime.now.strftime('%Q').to_i * 1000

  # Construct the conversion.
  conversion = DfareportingUtils::API_NAMESPACE::Conversion.new(
    floodlight_activity_id: floodlight_activity_id,
    floodlight_configuration_id: floodlight_config_id,
    ordinal: current_time_in_micros,
    mobile_device_id: mobile_device_id,
    timestamp_micros: current_time_in_micros
  )

  # Construct the batch insert request.
  batch_insert_request =
    DfareportingUtils::API_NAMESPACE::ConversionsBatchInsertRequest.new(
      conversions: [conversion]
    )

  # Insert the conversion.
  result = service.batchinsert_conversion(profile_id, batch_insert_request)

  process_response(result)
end

def process_response(result)
  if result.has_failures
    puts format('Error(s) inserting conversion for mobile device ID %s.',
      mobile_device_id)

    status = result.status[0]
    status.errors.each do |error|
      puts format("\t[%s]: %s", error.code, error.message)
    end
  else
    puts format('Successfully inserted conversion for mobile device ID %s.',
      mobile_device_id)
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :mobile_device_id,
    :floodlight_activity_id)

  insert_offline_mobile_conversion(args[:profile_id], args[:mobile_device_id],
    args[:floodlight_activity_id])
end
