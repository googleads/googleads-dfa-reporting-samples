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

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.FloodlightActivityGroup;
import com.google.api.services.dfareporting.model.FloodlightActivityGroupsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays floodlight activity groups for a given advertiser.
 *
 * To create an advertiser, run create_advertiser.py.
 */
public class GetFloodlightActivityGroups {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    // Limit the fields returned.
    String fields = "floodlightActivityGroups(id,name)";

    // Create and execute the floodlight activity groups list request.
    FloodlightActivityGroupsListResponse groups = reporting.floodlightActivityGroups()
        .list(profileId).setAdvertiserId(advertiserId).setFields(fields).execute();

    for (FloodlightActivityGroup group : groups.getFloodlightActivityGroups()) {
      System.out.printf("Floodlight activity group with ID %d and name \"%s\" was found.%n",
          group.getId(), group.getName());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
