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
 * This example creates an advertiser in a given DFA account. To get advertisers, see
 * GetAdvertisers.java.
 */
public class CreateAdvertiser {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_NAME = "INSERT_ADVERTISER_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String advertiserName)
      throws Exception {
    // Create the advertiser structure.
    Advertiser advertiser = new Advertiser();
    advertiser.setName(advertiserName);
    advertiser.setStatus("APPROVED");

    // Create the advertiser.
    Advertiser result = reporting.advertisers().insert(profileId, advertiser).execute();

    // Display the advertiser ID.
    System.out.printf("Advertiser with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, ADVERTISER_NAME);
  }
}
