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
import com.google.api.services.dfareporting.model.CreativeFieldValue;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a creative field value associated with a given creative field. To get the
 * creative field ID, run GetCreativeFields.java.
 */
public class CreateCreativeFieldValue {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CREATIVE_FIELD_ID = "INSERT_CREATIVE_FIELD_ID_HERE";
  private static final String CREATIVE_FIELD_VALUE_NAME = "INSERT_CREATIVE_FIELD_VALUE_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId,
      String creativeFieldValueName, long creativeFieldId) throws Exception {
    // Create the creative field value.
    CreativeFieldValue creativeFieldValue = new CreativeFieldValue();
    creativeFieldValue.setValue(creativeFieldValueName);

    // Insert the creative field value.
    CreativeFieldValue result = reporting.creativeFieldValues()
        .insert(profileId, creativeFieldId, creativeFieldValue).execute();

    // Display the new creative field value ID.
    System.out.printf("Creative field value with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long creativeFieldId = Long.parseLong(CREATIVE_FIELD_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, CREATIVE_FIELD_VALUE_NAME, creativeFieldId);
  }
}
