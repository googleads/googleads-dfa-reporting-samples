/*
 * Copyright (c) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.dfareporting.conversions;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Conversion;
import com.google.api.services.dfareporting.model.ConversionError;
import com.google.api.services.dfareporting.model.ConversionStatus;
import com.google.api.services.dfareporting.model.ConversionsBatchUpdateRequest;
import com.google.api.services.dfareporting.model.ConversionsBatchUpdateResponse;
import com.google.api.services.dfareporting.model.FloodlightActivity;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example updates the quantity and value of a conversion attributed to a mobile device ID. To
 * create a conversion attributed to a mobile device ID, run InsertOfflineMobileConversion.java.
 */
public class UpdateOfflineMobileConversion {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  // Values that identify an existing conversion.
  private static final String CONVERSION_MOBILE_ID = "INSERT_CONVERSION_MOBILE_ID_HERE";
  private static final String FLOODLIGHT_ACTIVITY_ID = "INSERT_FLOODLIGHT_ACTIVITY_ID_HERE";
  private static final String ORDINAL = "INSERT_ORDINAL_VALUE_HERE";
  private static final String TIMESTAMP = "INSERT_TIMESTAMP_IN_MICROSECONDS_HERE";

  // Values to update for the specified conversion.
  private static final String NEW_QUANTITY = "INSERT_NEW_CONVERSION_QUANTITY_HERE";
  private static final String NEW_VALUE = "INSERT_NEW_CONVERSION_VALUE_HERE";

  public static void runExample(
      Dfareporting reporting, long profileId, long floodlightActivityId, String mobileDeviceId,
      String ordinal, long timestampMicros, long newQuantity, double newValue) throws Exception {
    // Find Floodlight configuration ID based on the provided activity ID.
    FloodlightActivity floodlightActivity = reporting.floodlightActivities()
        .get(profileId, floodlightActivityId).execute();
    long floodlightConfigurationId = floodlightActivity.getFloodlightConfigurationId();

    // Create a conversion object populated with values that identify the conversion to update.
    Conversion conversion = new Conversion();
    conversion.setMobileDeviceId(mobileDeviceId);
    conversion.setFloodlightActivityId(floodlightActivityId);
    conversion.setFloodlightConfigurationId(floodlightConfigurationId);
    conversion.setOrdinal(ordinal);
    conversion.setTimestampMicros(timestampMicros);

    // Set the fields to be updated. These fields are required; to preserve a value from the
    // existing conversion, it must be copied over manually.
    conversion.setQuantity(newQuantity);
    conversion.setValue(newValue);

    ConversionsBatchUpdateRequest request = new ConversionsBatchUpdateRequest();
    request.setConversions(ImmutableList.of(conversion));

    ConversionsBatchUpdateResponse response = reporting.conversions()
        .batchupdate(profileId, request).execute();

    if (!response.getHasFailures()) {
      System.out.printf("Successfully updated conversion for mobile device ID %s.%n",
          mobileDeviceId);
    } else {
      System.out.printf("Error(s) updating conversion for mobile device ID %s:%n",
          mobileDeviceId);

      // Retrieve the conversion status and report any errors found. If multiple conversions
      // were included in the original request, the response would contain a status for each.
      ConversionStatus status = response.getStatus().get(0);
      for (ConversionError error : status.getErrors()) {
        System.out.printf("\t[%s]: %s.%n", error.getCode(), error.getMessage());
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long floodlightActivityId = Long.parseLong(FLOODLIGHT_ACTIVITY_ID);
    long newQuantity = Long.parseLong(NEW_QUANTITY);
    long profileId = Long.parseLong(USER_PROFILE_ID);
    long timestampMicros = Long.parseLong(TIMESTAMP);

    double newValue = Double.parseDouble(NEW_VALUE);

    runExample(reporting, profileId, floodlightActivityId, CONVERSION_MOBILE_ID,
        ORDINAL, timestampMicros, newQuantity, newValue);
  }
}
