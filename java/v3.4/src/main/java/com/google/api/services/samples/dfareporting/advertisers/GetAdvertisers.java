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

package com.google.api.services.samples.dfareporting.advertisers;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Advertiser;
import com.google.api.services.dfareporting.model.AdvertisersListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays the name, ID and spotlight configuration ID for every advertiser your DFA
 * user profile can see.
 */
public class GetAdvertisers {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,advertisers(id,floodlightConfigurationId,name)";

    AdvertisersListResponse result;
    String nextPageToken = null;

    do {
      // Create and execute the advertiser list request.
      result = reporting.advertisers().list(profileId).setFields(fields).setPageToken(nextPageToken)
          .execute();

      for (Advertiser advertiser : result.getAdvertisers()) {
        System.out.printf("Advertiser with ID %d, name \"%s\", and floodlight configuration ID %d "
            + "was found.%n", advertiser.getId(), advertiser.getName(),
            advertiser.getFloodlightConfigurationId());
      }

      // Update the next page token.
      nextPageToken = result.getNextPageToken();
    } while (!result.getAdvertisers().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
