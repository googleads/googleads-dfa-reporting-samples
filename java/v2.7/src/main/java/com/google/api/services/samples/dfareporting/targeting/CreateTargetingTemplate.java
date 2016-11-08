// Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.api.services.samples.dfareporting.targeting;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.DayPartTargeting;
import com.google.api.services.dfareporting.model.TargetingTemplate;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example creates a basic targeting template associated with a given advertiser. To get an
 * advertiser ID, run GetAdvertisers.java.
 */
public class CreateTargetingTemplate {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String TARGETING_TEMPLATE_NAME = "INSERT_TARGETING_TEMPLATE_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String templateName,
      long advertiserId) throws Exception {
    // Create the targeting template.
    TargetingTemplate template = new TargetingTemplate();
    template.setAdvertiserId(advertiserId);
    template.setName(templateName);

    // Configure the template to serve ads on Monday, Wednesday, and Friday from 9-10am and 3-5pm.
    DayPartTargeting dayTargeting = new DayPartTargeting();
    dayTargeting.setDaysOfWeek(ImmutableList.of("MONDAY", "WEDNESDAY", "FRIDAY"));
    dayTargeting.setHoursOfDay(ImmutableList.of(9, 15, 16));
    dayTargeting.setUserLocalTime(true);
    template.setDayPartTargeting(dayTargeting);

    // Insert the targeting template.
    TargetingTemplate result =
        reporting.targetingTemplates().insert(profileId, template).execute();

    // Display the new targeting template ID.
    System.out.printf("Targeting template with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, TARGETING_TEMPLATE_NAME, advertiserId);
  }
}
