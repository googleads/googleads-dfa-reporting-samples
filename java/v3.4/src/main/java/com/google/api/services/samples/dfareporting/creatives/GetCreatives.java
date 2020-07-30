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
import com.google.api.services.dfareporting.model.Creative;
import com.google.api.services.dfareporting.model.CreativesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example lists all existing active creatives for a given advertiser. To get an advertiser ID,
 * run GetAdvertisers.java.
 */
public class GetCreatives {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,creatives(id,name,type)";

    CreativesListResponse groups;
    String nextPageToken = null;

    do {
      // Create and execute the creatives list request.
      groups = reporting.creatives().list(profileId).setActive(true).setAdvertiserId(advertiserId)
          .setFields(fields).setPageToken(nextPageToken).execute();

      for (Creative group : groups.getCreatives()) {
        System.out.printf("Found %s creative with ID %d and name \"%s\".%n", group.getType(),
            group.getId(), group.getName());
      }

      // Update the next page token.
      nextPageToken = groups.getNextPageToken();
    } while (!groups.getCreatives().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
