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

package com.google.api.services.samples.dfareporting.misc;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Site;
import com.google.api.services.dfareporting.model.SitesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example lists all existing sites.
 */
public class GetSites {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,sites(id,keyName)";

    SitesListResponse sites;
    String nextPageToken = null;

    do {
      // Create and execute the sites list request.
      sites =
          reporting.sites().list(profileId).setFields(fields).setPageToken(nextPageToken).execute();

      for (Site site : sites.getSites()) {
        System.out.printf("Found site with ID %d and key name \"%s\".%n", site.getId(),
            site.getKeyName());
      }

      // Update the next page token.
      nextPageToken = sites.getNextPageToken();
    } while (!sites.getSites().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
