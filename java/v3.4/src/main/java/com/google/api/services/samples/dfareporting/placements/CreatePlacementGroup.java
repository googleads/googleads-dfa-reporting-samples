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

package com.google.api.services.samples.dfareporting.placements;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Campaign;
import com.google.api.services.dfareporting.model.PlacementGroup;
import com.google.api.services.dfareporting.model.PricingSchedule;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a placement group in a given campaign. Requires the DFA site ID and campaign
 * ID in which the placement group will be created into. To create a campaign, run
 * CreateCampaign.java. To get DFA site ID, run GetSite.java.
 */
public class CreatePlacementGroup {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CAMPAIGN_ID = "INSERT_CAMPAIGN_ID_HERE";
  private static final String DFA_SITE_ID = "INSERT_DFA_SITE_ID_HERE";
  private static final String PLACEMENT_GROUP_NAME = "INSERT_PLACEMENT_GROUP_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long dfaSiteId,
      long campaignId, String placementGroupName) throws Exception {
    // Retrieve the campaign.
    Campaign campaign = reporting.campaigns().get(profileId, campaignId).execute();

    // Create a pricing schedule.
    PricingSchedule pricingSchedule = new PricingSchedule();
    pricingSchedule.setEndDate(campaign.getEndDate());
    pricingSchedule.setPricingType("PRICING_TYPE_CPM");
    pricingSchedule.setStartDate(campaign.getStartDate());

    // Create the placement group.
    PlacementGroup placementGroup = new PlacementGroup();
    placementGroup.setCampaignId(campaignId);
    placementGroup.setName(placementGroupName);
    placementGroup.setPlacementGroupType("PLACEMENT_PACKAGE");
    placementGroup.setPricingSchedule(pricingSchedule);
    placementGroup.setSiteId(dfaSiteId);

    // Insert the placement.
    PlacementGroup result = reporting.placementGroups().insert(profileId, placementGroup).execute();

    // Display the new placement ID.
    System.out.printf("Placement group with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long campaignId = Long.parseLong(CAMPAIGN_ID);
    long dfaSiteId = Long.parseLong(DFA_SITE_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, dfaSiteId, campaignId, PLACEMENT_GROUP_NAME);
  }
}
