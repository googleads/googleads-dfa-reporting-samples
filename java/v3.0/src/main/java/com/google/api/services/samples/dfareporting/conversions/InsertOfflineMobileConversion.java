/*
 * Copyright (c) 2016 Google Inc.
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
import com.google.api.services.dfareporting.model.ConversionsBatchInsertRequest;
import com.google.api.services.dfareporting.model.ConversionsBatchInsertResponse;
import com.google.api.services.dfareporting.model.FloodlightActivity;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example inserts an offline conversion attributed to a mobile device ID, and associated
 * with the specified Floodlight activity. To create an activity, run CreateFloodlightActivity.java.
 */
public class InsertOfflineMobileConversion {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CONVERSION_MOBILE_ID = "INSERT_CONVERSION_MOBILE_ID_HERE";
  private static final String FLOODLIGHT_ACTIVITY_ID = "INSERT_FLOODLIGHT_ACTIVITY_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long floodlightActivityId,
      String mobileDeviceId) throws Exception {
    long currentTimeInMilliseconds = System.currentTimeMillis();

    // Find Floodlight configuration ID based on the provided activity ID.
    FloodlightActivity floodlightActivity = reporting.floodlightActivities()
        .get(profileId, floodlightActivityId).execute();
    long floodlightConfigurationId = floodlightActivity.getFloodlightConfigurationId();

    Conversion conversion = new Conversion();
    conversion.setMobileDeviceId(mobileDeviceId);
    conversion.setFloodlightActivityId(floodlightActivityId);
    conversion.setFloodlightConfigurationId(floodlightConfigurationId);
    conversion.setOrdinal(String.valueOf(currentTimeInMilliseconds));
    conversion.setTimestampMicros(currentTimeInMilliseconds * 1000);

    ConversionsBatchInsertRequest request = new ConversionsBatchInsertRequest();
    request.setConversions(ImmutableList.of(conversion));

    ConversionsBatchInsertResponse response = reporting.conversions()
        .batchinsert(profileId, request).execute();

    if(!response.getHasFailures()) {
      System.out.printf("Successfully inserted conversion for mobile device ID %s.%n",
          mobileDeviceId);
    } else {
      System.out.printf("Error(s) inserting conversion for mobile device ID %s:%n",
          mobileDeviceId);

      // Retrieve the conversion status and report any errors found. If multiple conversions
      // were included in the original request, the response would contain a status for each.
      ConversionStatus status = response.getStatus().get(0);
      for(ConversionError error : status.getErrors()) {
        System.out.printf("\t[%s]: %s.%n", error.getCode(), error.getMessage());
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long floodlightActivityId = Long.parseLong(FLOODLIGHT_ACTIVITY_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, floodlightActivityId, CONVERSION_MOBILE_ID);
  }
}
