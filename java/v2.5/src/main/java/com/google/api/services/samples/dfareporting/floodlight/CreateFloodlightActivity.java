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
import com.google.api.services.dfareporting.model.FloodlightActivity;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a floodlight activity in a given activity group. To create an activity
 * group, run CreateFloodlightActivityGroup.java.
 */
public class CreateFloodlightActivity {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ACTIVITY_GROUP_ID = "INSERT_ACTIVITY_GROUP_ID_HERE";
  private static final String ACTIVITY_NAME = "INSERT_ACTIVITY_NAME_HERE";
  private static final String URL = "INSERT_EXPECTED_URL_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String activityName,
      String url, long activityGroupId) throws Exception {
    // Set floodlight activity structure.
    FloodlightActivity activity = new FloodlightActivity();
    activity.setCountingMethod("STANDARD_COUNTING");
    activity.setName(activityName);
    activity.setFloodlightActivityGroupId(activityGroupId);
    activity.setExpectedUrl(url);

    // Create the floodlight tag activity.
    FloodlightActivity result =
        reporting.floodlightActivities().insert(profileId, activity).execute();

    // Display new floodlight activity ID.
    if (result != null) {
      System.out.printf("Floodlight activity with ID %d was created.%n", result.getId());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long activityGroupId = Long.parseLong(ACTIVITY_GROUP_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, ACTIVITY_NAME, URL, activityGroupId);
  }
}
