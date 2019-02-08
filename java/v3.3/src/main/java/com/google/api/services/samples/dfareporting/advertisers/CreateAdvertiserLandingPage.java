// Copyright 2017 Google Inc. All Rights Reserved.
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
import com.google.api.services.dfareporting.model.LandingPage;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates an advertiser landing page.
 */
public class CreateAdvertiserLandingPage {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String LANDING_PAGE_NAME = "INSERT_LANDING_PAGE_NAME_HERE";
  private static final String LANDING_PAGE_URL = "INSERT_LANDING_PAGE_URL_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId,
      String landingPageName, String landingPageUrl) throws Exception {
    // Create the landing page structure.
    LandingPage landingPage = new LandingPage();
    landingPage.setAdvertiserId(advertiserId);
    landingPage.setArchived(false);
    landingPage.setName(landingPageName);
    landingPage.setUrl(landingPageUrl);

    // Create the landing page
    LandingPage result =
        reporting.advertiserLandingPages().insert(profileId, landingPage).execute();

    // Display the landing page ID.
    System.out.printf("Advertiser landing page with ID %d and name \"%s\" was created.%n",
        result.getId(), result.getName());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long advertiserId = Long.parseLong(ADVERTISER_ID);

    runExample(reporting, profileId, advertiserId, LANDING_PAGE_NAME, LANDING_PAGE_URL);
  }
}
