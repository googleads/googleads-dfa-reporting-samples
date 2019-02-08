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
import com.google.api.services.dfareporting.model.Placement;
import com.google.api.services.dfareporting.model.PricingSchedule;
import com.google.api.services.dfareporting.model.Size;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example creates a placement in a given campaign. Requires a DFA site ID and ID of the
 * campaign in which the placement will be created. To create a campaign, run CreateCampaign.java.
 * To get a DFA site ID, run GetSite.java. To get a size ID, run GetSize.java.
 */
public class CreatePlacement {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CAMPAIGN_ID = "INSERT_CAMPAIGN_ID_HERE";
  private static final String DFA_SITE_ID = "INSERT_SITE_ID_HERE";
  private static final String PLACEMENT_NAME = "INSERT_PLACEMENT_NAME_HERE";
  private static final String SIZE_ID = "INSERT_SIZE_ID_HERE";


  public static void runExample(Dfareporting reporting, long profileId, String placementName,
      long dfaSiteId, long campaignId, long sizeId) throws Exception {
    // Retrieve the campaign.
    Campaign campaign = reporting.campaigns().get(profileId, campaignId).execute();

    // Create the placement.
    Placement placement = new Placement();
    placement.setName(placementName);
    placement.setCampaignId(campaignId);
    placement.setCompatibility("DISPLAY");
    placement.setPaymentSource("PLACEMENT_AGENCY_PAID");
    placement.setSiteId(dfaSiteId);
    placement.setTagFormats(ImmutableList.of("PLACEMENT_TAG_STANDARD"));

    // Set the size of the placement.
    Size size = new Size();
    size.setId(sizeId);
    placement.setSize(size);

    // Set the pricing schedule for the placement.
    PricingSchedule pricingSchedule = new PricingSchedule();
    pricingSchedule.setEndDate(campaign.getEndDate());
    pricingSchedule.setPricingType("PRICING_TYPE_CPM");
    pricingSchedule.setStartDate(campaign.getStartDate());
    placement.setPricingSchedule(pricingSchedule);

    // Insert the placement.
    Placement result = reporting.placements().insert(profileId, placement).execute();

    // Display the new placement ID.
    System.out.printf("Placement with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long campaignId = Long.parseLong(CAMPAIGN_ID);
    long dfaSiteId = Long.parseLong(DFA_SITE_ID);
    long sizeId = Long.parseLong(SIZE_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, PLACEMENT_NAME, dfaSiteId, campaignId, sizeId);
  }
}
