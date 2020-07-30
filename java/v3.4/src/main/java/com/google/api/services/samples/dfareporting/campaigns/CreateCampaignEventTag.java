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

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.EventTag;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates an event tag for the specified campaign.
 */
public class CreateCampaignEventTag {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CAMPAIGN_ID = "INSERT_CAMPAIGN_ID_HERE";
  private static final String EVENT_TAG_NAME = "INSERT_EVENT_TAG_NAME_HERE";
  private static final String EVENT_TAG_URL = "INSERT_EVENT_TAG_URL_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long campaignId,
      String eventTagName, String eventTagUrl) throws Exception {
    // Create the event tag structure.
    EventTag eventTag = new EventTag();
    eventTag.setCampaignId(campaignId);
    eventTag.setName(eventTagName);
    eventTag.setStatus("ENABLED");
    eventTag.setType("CLICK_THROUGH_EVENT_TAG");
    eventTag.setUrl(eventTagUrl);

    // Insert the campaign.
    EventTag result = reporting.eventTags().insert(profileId, eventTag).execute();

    // Display the new campaign ID.
    System.out.printf("Event Tag with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long campaignId = Long.parseLong(CAMPAIGN_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, campaignId, EVENT_TAG_NAME, EVENT_TAG_URL);
  }
}
