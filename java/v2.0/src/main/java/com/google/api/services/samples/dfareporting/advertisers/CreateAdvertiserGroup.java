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
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates an advertiser group. To get advertiser groups, see GetAdvertiserGroups.java.
 */
public class CreateAdvertiserGroup {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_GROUP_NAME = "INSERT_ADVERTISER_GROUP_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String advertiserGroupName)
      throws Exception {
    // Create advertiser group structure.
    AdvertiserGroup advertiserGroup = new AdvertiserGroup();
    advertiserGroup.setName(advertiserGroupName);

    // Create advertiser group.
    AdvertiserGroup result =
        reporting.advertiserGroups().insert(profileId, advertiserGroup).execute();

    // Display advertiser group ID.
    System.out.printf("Advertiser Group with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, ADVERTISER_GROUP_NAME);
  }
}
