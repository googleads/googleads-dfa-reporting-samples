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

package com.google.api.services.samples.dfareporting.ads;

import com.google.api.client.util.DateTime;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Ad;
import com.google.api.services.dfareporting.model.Campaign;
import com.google.api.services.dfareporting.model.ClickThroughUrl;
import com.google.api.services.dfareporting.model.CreativeAssignment;
import com.google.api.services.dfareporting.model.CreativeRotation;
import com.google.api.services.dfareporting.model.DeliverySchedule;
import com.google.api.services.dfareporting.model.PlacementAssignment;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

import java.util.Date;

/**
 * This example creates a rotation group ad in a given campaign. Start and end date for the ad must
 * be within campaign start and end dates. To create creatives, run one of the Create*Creative.java
 * examples. To get available placements, run GetPlacements.java.
 */
public class CreateRotationGroup {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  // Set the parameters for the new ad rotation group.
  private static final String AD_NAME = "ENTER_AD_NAME_HERE";
  private static final String CAMPAIGN_ID = "ENTER_CAMPAIGN_ID_HERE";
  private static final String CREATIVE_ID = "ENTER_CREATIVE_ID_HERE";
  private static final String PLACEMENT_ID = "ENTER_PLACEMENT_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String adName,
      long campaignId, long creativeId, long placementId) throws Exception {
    // Retrieve the campaign.
    Campaign campaign = reporting.campaigns().get(profileId, campaignId).execute();

    // Create a click-through URL.
    ClickThroughUrl clickThroughUrl = new ClickThroughUrl();
    clickThroughUrl.setDefaultLandingPage(true);

    // Create a creative assignment.
    CreativeAssignment creativeAssignment = new CreativeAssignment();
    creativeAssignment.setActive(true);
    creativeAssignment.setCreativeId(creativeId);
    creativeAssignment.setClickThroughUrl(clickThroughUrl);

    // Create a placement assignment.
    PlacementAssignment placementAssignment = new PlacementAssignment();
    placementAssignment.setActive(true);
    placementAssignment.setPlacementId(placementId);

    // Create a creative rotation.
    CreativeRotation creativeRotation = new CreativeRotation();
    creativeRotation.setCreativeAssignments(ImmutableList.of(creativeAssignment));

    // Create a delivery schedule.
    DeliverySchedule deliverySchedule = new DeliverySchedule();
    deliverySchedule.setImpressionRatio(1L);
    deliverySchedule.setPriority("AD_PRIORITY_01");

    DateTime startDate = new DateTime(new Date());
    DateTime endDate = campaign.getEndDate();

    // Create a rotation group.
    Ad rotationGroup = new Ad();
    rotationGroup.setActive(true);
    rotationGroup.setCampaignId(campaignId);
    rotationGroup.setCreativeRotation(creativeRotation);
    rotationGroup.setDeliverySchedule(deliverySchedule);
    rotationGroup.setStartTime(startDate);
    rotationGroup.setEndTime(endDate);
    rotationGroup.setName(adName);
    rotationGroup.setPlacementAssignments(ImmutableList.of(placementAssignment));
    rotationGroup.setType("AD_SERVING_STANDARD_AD");

    // Insert the rotation group.
    Ad result = reporting.ads().insert(profileId, rotationGroup).execute();

    // Display the new ad ID.
    System.out.printf("Ad with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long campaignId = Long.parseLong(CAMPAIGN_ID);
    long creativeId = Long.parseLong(CREATIVE_ID);
    long placementId = Long.parseLong(PLACEMENT_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, AD_NAME, campaignId, creativeId, placementId);
  }
}
