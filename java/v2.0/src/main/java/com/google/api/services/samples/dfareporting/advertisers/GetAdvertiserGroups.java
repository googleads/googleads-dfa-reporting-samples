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

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.AdvertiserGroup;
import com.google.api.services.dfareporting.model.AdvertiserGroupsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all advertiser groups for the specified user profile.
 */
public class GetAdvertiserGroups {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,advertiserGroups(id,name)";

    // List all advertiser groups
    AdvertiserGroupsListResponse result =
        reporting.advertiserGroups().list(profileId).setFields(fields).execute();

    // Display advertiser group names and IDs
    if (!result.getAdvertiserGroups().isEmpty()) {
      for (AdvertiserGroup group : result.getAdvertiserGroups()) {
        System.out.printf("Advertiser Group with ID %d and name \"%s\" was found.%n",
            group.getId(), group.getName());
      }
    } else {
      System.out.print("No advertiser groups found for your criteria.");
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
