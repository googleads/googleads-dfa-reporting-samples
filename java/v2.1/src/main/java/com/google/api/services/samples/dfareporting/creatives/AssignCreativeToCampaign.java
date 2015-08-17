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

package com.google.api.services.samples.dfareporting.creatives;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.CampaignCreativeAssociation;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example assigns a given creative to a given campaign. Note that both the creative and
 * campaign must be associated with the same advertiser.
 */
public class AssignCreativeToCampaign {
  private static final String USER_PROFILE_ID = "INSERT_PROFILE_ID_HERE";

  private static final String CAMPAIGN_ID = "INSERT_CAMPAIGN_ID_HERE";
  private static final String CREATIVE_ID = "INSERT_CREATIVE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long campaignId,
      long creativeId) throws Exception {
    // Create the campaign creative association structure.
    CampaignCreativeAssociation association = new CampaignCreativeAssociation();
    association.setCreativeId(creativeId);

    // Insert the association.
    CampaignCreativeAssociation result = reporting
        .campaignCreativeAssociations().insert(profileId, campaignId, association)
        .execute();

    // Display a success message.
    System.out.printf("Creative with ID %d is now associated with campaign %d.%n",
        result.getCreativeId(), campaignId);
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long campaignId = Long.parseLong(CAMPAIGN_ID);
    long creativeId = Long.parseLong(CREATIVE_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, campaignId, creativeId);
  }
}
