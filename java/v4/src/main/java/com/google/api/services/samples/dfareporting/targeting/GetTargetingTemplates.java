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

package com.google.api.services.samples.dfareporting.targeting;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.TargetingTemplate;
import com.google.api.services.dfareporting.model.TargetingTemplatesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays the name, ID and advertiser ID for every targeting template your DCM user
 * profile can see.
 */
public class GetTargetingTemplates {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,targetingTemplates(advertiserId,id,name)";

    TargetingTemplatesListResponse result;
    String nextPageToken = null;

    do {
      // Create and execute the targeting templates list request.
      result = reporting.targetingTemplates().list(profileId).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (TargetingTemplate template : result.getTargetingTemplates()) {
        System.out.printf(
            "Targeting template with ID %d and name \"%s\" is associated with advertiser ID %d.%n",
            template.getId(), template.getName(), template.getAdvertiserId());
      }

      // Update the next page token.
      nextPageToken = result.getNextPageToken();
    } while (!result.getTargetingTemplates().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
