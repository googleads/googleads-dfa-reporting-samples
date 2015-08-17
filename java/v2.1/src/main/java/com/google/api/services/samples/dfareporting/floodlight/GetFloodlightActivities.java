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

package com.google.api.services.samples.dfareporting.floodlight;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.FloodlightActivitiesListResponse;
import com.google.api.services.dfareporting.model.FloodlightActivity;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays floodlight activities for a given advertiser.
 *
 * To create an advertiser, run create_advertiser.py.
 */
public class GetFloodlightActivities {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,floodlightActivities(id,name)";

    FloodlightActivitiesListResponse activities;
    String nextPageToken = null;

    do {
      // Create and execute the floodlight activities list request.
      activities = reporting.floodlightActivities().list(profileId).setAdvertiserId(advertiserId)
          .setFields(fields).setPageToken(nextPageToken).execute();

      for (FloodlightActivity activity : activities.getFloodlightActivities()) {
        System.out.printf("Floodlight activity with ID %d and name \"%s\" was found.%n",
            activity.getId(), activity.getName());
      }

      // Update the next page token.
      nextPageToken = activities.getNextPageToken();
    } while (!activities.getFloodlightActivities().isEmpty()
        && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
