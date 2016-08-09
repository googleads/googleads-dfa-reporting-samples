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
import com.google.api.services.dfareporting.model.EncryptionInfo;
import com.google.api.services.dfareporting.model.FloodlightActivity;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example inserts an offline conversion attributed to an encrypted user ID, and associated
 * with the specified Floodlight activity. To create an activity, run CreateFloodlightActivity.java.
 */
public class InsertOfflineUserConversion {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CONVERSION_USER_ID = "INSERT_CONVERSION_USER_ID_HERE";
  private static final String ENCRYPTION_ENTITY_ID = "INSERT_ENCRYPTION_ENTITY_ID_HERE";
  private static final String ENCRYPTION_ENTITY_TYPE = "INSERT_ENCRYPTION_ENTITY_TYPE_HERE";
  private static final String ENCRYPTION_SOURCE = "INSERT_ENCRYPTION_SOURCE_HERE";
  private static final String FLOODLIGHT_ACTIVITY_ID = "INSERT_FLOODLIGHT_ACTIVITY_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long floodlightActivityId,
      String encryptedUserId, long encryptionEntityId, String encryptionEntityType,
      String encryptionSource) throws Exception {
    long currentTimeInMilliseconds = System.currentTimeMillis();

    // Find Floodlight configuration ID based on the provided activity ID.
    FloodlightActivity floodlightActivity = reporting.floodlightActivities()
        .get(profileId, floodlightActivityId).execute();
    long floodlightConfigurationId = floodlightActivity.getFloodlightConfigurationId();

    Conversion conversion = new Conversion();
    conversion.setEncryptedUserId(encryptedUserId);
    conversion.setFloodlightActivityId(floodlightActivityId);
    conversion.setFloodlightConfigurationId(floodlightConfigurationId);
    conversion.setOrdinal(String.valueOf(currentTimeInMilliseconds));
    conversion.setTimestampMicros(currentTimeInMilliseconds * 1000);

    EncryptionInfo encryptionInfo = new EncryptionInfo();
    encryptionInfo.setEncryptionEntityId(encryptionEntityId);
    encryptionInfo.setEncryptionEntityType(encryptionEntityType);
    encryptionInfo.setEncryptionSource(encryptionSource);

    ConversionsBatchInsertRequest request = new ConversionsBatchInsertRequest();
    request.setConversions(ImmutableList.of(conversion));
    request.setEncryptionInfo(encryptionInfo);

    ConversionsBatchInsertResponse response = reporting.conversions()
        .batchinsert(profileId, request).execute();

    if(!response.getHasFailures()) {
      System.out.printf("Successfully inserted conversion for encrypted user ID %s.%n",
          encryptedUserId);
    } else {
      System.out.printf("Error(s) inserting conversion for encrypted user ID %s:%n",
          encryptedUserId);

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

    long encryptionEntityId = Long.parseLong(ENCRYPTION_ENTITY_ID);
    long floodlightActivityId = Long.parseLong(FLOODLIGHT_ACTIVITY_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, floodlightActivityId, CONVERSION_USER_ID, encryptionEntityId,
        ENCRYPTION_ENTITY_TYPE, ENCRYPTION_SOURCE);
  }
}

