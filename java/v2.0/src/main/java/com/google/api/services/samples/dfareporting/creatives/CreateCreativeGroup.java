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
import com.google.api.services.dfareporting.model.CreativeGroup;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a creative group associated with a given advertiser. To get an advertiser
 * ID, run getAdvertisers.java. Valid group numbers are limited to 1 or 2.
 */
public class CreateCreativeGroup {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String CREATIVE_GROUP_NAME = "INSERT_CREATIVE_GROUP_NAME_HERE";
  private static final String GROUP_NUMBER = "INSERT_GROUP_NUMBER_HERE";


  public static void runExample(Dfareporting reporting, long profileId, String creativeGroupName,
      int groupNumber, long advertiserId) throws Exception {
    // Create the creative group.
    CreativeGroup creativeGroup = new CreativeGroup();
    creativeGroup.setName(creativeGroupName);
    creativeGroup.setGroupNumber(groupNumber);
    creativeGroup.setAdvertiserId(advertiserId);

    // Insert the creative group.
    CreativeGroup result = reporting.creativeGroups().insert(profileId, creativeGroup).execute();

    // Display the new creative group ID.
    System.out.printf("Creative group with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    int groupNumber = Integer.parseInt(GROUP_NUMBER);
    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, CREATIVE_GROUP_NAME, groupNumber, advertiserId);
  }
}
