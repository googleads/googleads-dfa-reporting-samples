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
import com.google.api.services.dfareporting.model.Advertiser;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example assigns an advertiser to an advertiser group.
 *
 * CAUTION: An advertiser that has campaigns associated with it cannot be removed from an
 * advertiser group once assigned.
 */
public class AssignAdvertisersToAdvertiserGroup {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String ADVERTISER_GROUP_ID = "INSERT_ADVERTISER_GROUP_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId,
      long advertiserGroupId) throws Exception {
    // Create the Advertiser and populate with the group ID to patch.
    Advertiser advertiser = new Advertiser();
    advertiser.setAdvertiserGroupId(advertiserGroupId);

    // Patch the existing advertiser.
    Advertiser result =
        reporting.advertisers().patch(profileId, advertiserId, advertiser).execute();

    // Print out the advertiser and advertiser group ID.
    System.out.printf("Advertiser with ID %d was assigned to advertiser group \"%s\".%n",
        result.getId(), result.getAdvertiserGroupId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long advertiserGroupId = Long.parseLong(ADVERTISER_GROUP_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId, advertiserGroupId);
  }
}
