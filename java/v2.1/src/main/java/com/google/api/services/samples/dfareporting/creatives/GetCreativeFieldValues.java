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

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.CreativeFieldValue;
import com.google.api.services.dfareporting.model.CreativeFieldValuesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example retrieves available creative field values for a given string and displays the names
 * and IDs.
 */
public class GetCreativeFieldValues {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String CREATIVE_FIELD_ID = "ENTER_CREATIVE_FIELD_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long creativeFieldId)
      throws Exception {
    CreativeFieldValuesListResponse values;
    String nextPageToken = null;

    do {
      // Create and execute the creative field values list request.
      values = reporting.creativeFieldValues().list(profileId, creativeFieldId)
          .setPageToken(nextPageToken).execute();

      for (CreativeFieldValue value : values.getCreativeFieldValues()) {
        System.out.printf("Found creative field value with ID %d and value \"%s\".%n",
            value.getId(), value.getValue());
      }

      // Update the next page token.
      nextPageToken = values.getNextPageToken();
    } while (!values.getCreativeFieldValues().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long creativeFieldId = Long.parseLong(CREATIVE_FIELD_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, creativeFieldId);
  }
}
