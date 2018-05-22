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

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.AdvertiserLandingPagesListResponse;
import com.google.api.services.dfareporting.model.LandingPage;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example lists all landing pages for a specified advertiser.
 */
public class GetAdvertiserLandingPages {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,landingPages(id,name,url)";

    AdvertiserLandingPagesListResponse result;
    String nextPageToken = null;

    do {
      // Create and execute the advertiser landing pages list request.
      result = reporting.advertiserLandingPages().list(profileId).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (LandingPage landingPage : result.getLandingPages()) {
        System.out.printf(
            "Advertiser landing page with ID %d, name \"%s\", and URL %s was found.%n",
            landingPage.getId(), landingPage.getName(), landingPage.getUrl());
      }

      // Update the next page token.
      nextPageToken = result.getNextPageToken();
    } while (!result.getLandingPages().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long advertiserId = Long.parseLong(ADVERTISER_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
