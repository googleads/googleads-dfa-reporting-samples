// Copyright 2024 Google Inc. All Rights Reserved.
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

package com.google.api.services.samples.dfareporting.tvcampaignsummaries;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.TvCampaignSummariesListResponse;
import com.google.api.services.dfareporting.model.TvCampaignSummary;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/** This example illustrates how to get a list of all TvCampaignSummaries. */
public class ListTvCampaignSummaries {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";
  private static final String ACCOUNT_ID = "INSERT_ACCOUNT_ID_HERE";
  private static final String NAME = "INSERT_TV_CAMPAIGN_SUMMARY_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long accountId, String name)
      throws Exception {

    TvCampaignSummariesListResponse tvCampaignSummaries = null;

    System.out.printf("Searching for TV campaign summaries with name: %s", name);
    // Create and execute the TV campaign summaries list request.
    tvCampaignSummaries =
        reporting
            .tvCampaignSummaries()
            .list(profileId)
            .setAccountId(accountId)
            .setName(name)
            .execute();
    for (TvCampaignSummary tvCampaignSummary : tvCampaignSummaries.getTvCampaignSummaries()) {
      System.out.printf(
          "TV campaign summary with ID %s and name \"%s\" found",
          tvCampaignSummary.getId(), tvCampaignSummary.getName());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long accountId = Long.parseLong(ACCOUNT_ID);

    runExample(reporting, profileId, accountId, NAME);
  }
}
