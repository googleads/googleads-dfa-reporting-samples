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
import com.google.api.services.dfareporting.Dfareporting.FloodlightActivities.Generatetag;
import com.google.api.services.dfareporting.model.FloodlightActivitiesGenerateTagResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example downloads activity tags for a given floodlight activity.
 */
public class DownloadFloodlightTags {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ACTIVITY_ID = "ENTER_ACTIVITY_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long activityId)
      throws Exception {
    // Generate the floodlight activity tag.
    Generatetag request = reporting.floodlightActivities().generatetag(profileId);
    request.setFloodlightActivityId(activityId);

    FloodlightActivitiesGenerateTagResponse response = request.execute();

    // Display the floodlight activity tag.
    System.out.printf(response.getFloodlightActivityTag());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long activityId = Long.parseLong(ACTIVITY_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, activityId);
  }
}
