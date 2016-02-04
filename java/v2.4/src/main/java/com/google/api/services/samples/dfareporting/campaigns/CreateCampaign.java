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

package com.google.api.services.samples.dfareporting.campaigns;

import com.google.api.client.util.DateTime;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Campaign;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

import java.util.Calendar;

/**
 * This example creates a campaign associated with a given advertiser. To create an advertiser, run
 * CreateAdvertiser.java.
 */
public class CreateCampaign {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String CAMPAIGN_NAME = "INSERT_CAMPAIGN_NAME_HERE";
  private static final String LANDING_PAGE_NAME = "INSERT_LANDING_PAGE_NAME_HERE";
  private static final String URL = "INSERT_LANDING_PAGE_URL_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String campaignName,
      long advertiserId, String url, String landingPageName) throws Exception {
    // Create the campaign structure.
    Campaign campaign = new Campaign();
    campaign.setName(campaignName);
    campaign.setAdvertiserId(advertiserId);
    campaign.setArchived(false);

    // Set the campaign start date. This example uses today's date.
    Calendar today = Calendar.getInstance();
    DateTime startDate = new DateTime(true, today.getTimeInMillis(), null);
    campaign.setStartDate(startDate);

    // Set the campaign end date. This example uses one month from today's date.
    Calendar nextMonth = Calendar.getInstance();
    nextMonth.add(Calendar.MONTH, 1);
    DateTime endDate = new DateTime(true, nextMonth.getTimeInMillis(), null);
    campaign.setEndDate(endDate);

    // Insert the campaign.
    Campaign result =
        reporting.campaigns().insert(profileId, landingPageName, url, campaign).execute();

    // Display the new campaign ID.
    System.out.printf("Campaign with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, CAMPAIGN_NAME, advertiserId, URL, LANDING_PAGE_NAME);
  }
}
