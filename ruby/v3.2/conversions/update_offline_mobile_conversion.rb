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
# This example updates an offline conversion attributed to a mobile device ID.
#
# To create a conversion attributed to a mobile device ID, run
# insert_offline_mobile_conversion.rb.

require_relative '../dfareporting_utils'

# Updates an offline mobile conversion with the specified values.
#
# @param profile_id [Number] The ID of the DCM user issuing this request.
# @param new_quantity [Number] The new quantity value to assign to the
#   specified conversion.
# @param new_value [Number] The new value to assign to the specified
#   conversion.
# @param existing_conversion [Object] A hash containing values that identify
#   an existing offline mobile conversion. The expected format is:
#      {
#        mobile_device_id: <The mobile device ID>,
#        floodlight_activity_id: <The Floodlight activity ID>,
#        ordinal: <The conversion ordinal value>,
#        timestamp: <The conversion timestamp>
#      }
def update_offline_mobile_conversion(profile_id, new_quantity, new_value,
  existing_conversion = {})
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Look up the Floodlight configuration ID based on activity ID.
  floodlight_activity = service.get_floodlight_activity(profile_id,
    existing_conversion[:floodlight_activity_id])
  floodlight_config_id = floodlight_activity.floodlight_configuration_id

  # Construct the conversion with values that identify the conversion to
  # update
  conversion = DfareportingUtils::API_NAMESPACE::Conversion.new(
    floodlight_activity_id: existing_conversion[:floodlight_activity_id],
    floodlight_configuration_id: floodlight_config_id,
    ordinal: existing_conversion[:ordinal],
    mobile_device_id: existing_conversion[:mobile_device_id],
    timestamp_micros: existing_conversion[:timestamp]
  )

  # Set the fields to be updated. These fields are required; to preserve a
  # value from the existing conversion, it must be copied over manually.
  conversion.quantity = new_quantity
  conversion.value = new_value

  # Construct the batch update request.
  batch_update_request =
    DfareportingUtils::API_NAMESPACE::ConversionsBatchUpdateRequest.new(
      conversions: [conversion]
    )

  # Update the conversion.
  result = service.batchupdate_conversion(profile_id, batch_update_request)

  if result.has_failures
    puts format('Error(s) updating conversion for mobile device ID %s.',
      existing_conversion[:mobile_device_id])

    status = result.status[0]
    status.errors.each do |error|
      puts format("\t[%s]: %s", error.code, error.message)
    end
  else
    puts format('Successfully updated conversion for mobile device ID %s.',
      existing_conversion[:mobile_device_id])
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :mobile_device_id,
    :floodlight_activity_id, :ordinal, :timestamp, :new_quantity, :new_value)

  update_offline_mobile_conversion(
    args[:profile_id], args[:new_quantity], args[:new_value],
    mobile_device_id: args[:mobile_device_id],
    floodlight_activity_id: args[:floodlight_activity_id],
    ordinal: args[:ordinal],
    timestamp: args[:timestamp]
  )
end
