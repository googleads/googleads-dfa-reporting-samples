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

package com.google.api.services.samples.dfareporting.placements;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Placement;
import com.google.api.services.dfareporting.model.PlacementsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all available placements.
 */
public class GetPlacements {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,placements(campaignId,id,name)";

    PlacementsListResponse placements;
    String nextPageToken = null;

    do {
      // Create and execute the placements list request.
      placements = reporting.placements().list(profileId).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (Placement placement : placements.getPlacements()) {
        System.out.printf(
            "Placement with ID %d and name \"%s\" is associated with campaign ID %d.%n",
            placement.getId(), placement.getName(), placement.getCampaignId());
      }

      // Update the next page token.
      nextPageToken = placements.getNextPageToken();
    } while (!placements.getPlacements().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
