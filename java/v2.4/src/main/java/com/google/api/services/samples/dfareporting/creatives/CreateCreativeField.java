// Copyright 2014 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.services.samples.dfareporting.creatives;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.CreativeField;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a creative field associated with a given advertiser. To get an advertiser
 * ID, run GetAdvertisers.java.
 */
public class CreateCreativeField {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String CREATIVE_FIELD_NAME = "INSERT_CREATIVE_FIELD_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String creativeFieldName,
      long advertiserId) throws Exception {
    // Create the creative field.
    CreativeField creativeField = new CreativeField();
    creativeField.setName(creativeFieldName);
    creativeField.setAdvertiserId(advertiserId);

    // Insert the creative field.
    CreativeField result = reporting.creativeFields().insert(profileId, creativeField).execute();

    // Display the new creative field ID.
    System.out.printf("Creative field with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, CREATIVE_FIELD_NAME, advertiserId);
  }
}
