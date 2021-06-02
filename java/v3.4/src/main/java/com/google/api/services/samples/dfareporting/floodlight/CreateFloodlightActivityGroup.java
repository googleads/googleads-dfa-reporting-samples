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
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a new activity group for a given floodlight configuration. To get a
 * floodlight tag configuration ID, run GetAdvertisers.java.
 */
public class CreateFloodlightActivityGroup {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String GROUP_NAME = "INSERT_GROUP_NAME_HERE";
  private static final String FLOODLIGHT_CONFIGURATION_ID =
      "INSERT_FLOODLIGHT_CONFIGURATION_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String groupName,
      long floodlightConfigurationId) throws Exception {
    // Create the floodlight activity group.
    FloodlightActivityGroup floodlightActivityGroup = new FloodlightActivityGroup();
    floodlightActivityGroup.setName(groupName);
    floodlightActivityGroup.setFloodlightConfigurationId(floodlightConfigurationId);
    floodlightActivityGroup.setType("COUNTER");

    // Insert the activity group.
    FloodlightActivityGroup result =
        reporting.floodlightActivityGroups().insert(profileId, floodlightActivityGroup).execute();

    // Display the new activity group ID.
    if (result != null) {
      System.out.printf("Activity group with ID %d was created.%n", result.getId());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long floodlightConfigurationId = Long.parseLong(FLOODLIGHT_CONFIGURATION_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, GROUP_NAME, floodlightConfigurationId);
  }
}
