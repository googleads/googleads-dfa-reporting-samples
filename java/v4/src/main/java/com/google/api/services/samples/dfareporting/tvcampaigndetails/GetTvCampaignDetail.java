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

package com.google.api.services.samples.dfareporting.tvcampaigndetails;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.TvCampaignDetail;
import com.google.api.services.dfareporting.model.TvCampaignTimepoint;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/** This example illustrates how to get a TvCampaignDetail by ID. */
public class GetTvCampaignDetail {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";
  private static final String ACCOUNT_ID = "INSERT_ACCOUNT_ID_HERE";
  private static final String ID = "INSERT_TV_CAMPAIGN_DETAIL_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long accountId, String id)
      throws Exception {

    TvCampaignDetail tvCampaignDetail = null;

    System.out.printf("Searching for TV campaign detail with ID: %s", id);
    // Create and execute the TV campaign detail get request.
    tvCampaignDetail =
        reporting.tvCampaignDetails().get(profileId, id).setAccountId(accountId).execute();

    for (TvCampaignTimepoint timepoint : tvCampaignDetail.getTimepoints()) {
      System.out.printf(
          "Found timepoint with start date %s and date window %s for TvCampaignDetail with ID %s",
          timepoint.getStartDate(), timepoint.getDateWindow(), tvCampaignDetail.getId());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long accountId = Long.parseLong(ACCOUNT_ID);

    runExample(reporting, profileId, accountId, ID);
  }
}
