// Copyright 2015 Google Inc. All Rights Reserved.
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

package com.google.api.services.samples.dfareporting.remarketing;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.ListPopulationClause;
import com.google.api.services.dfareporting.model.ListPopulationRule;
import com.google.api.services.dfareporting.model.ListPopulationTerm;
import com.google.api.services.dfareporting.model.RemarketingList;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example creates a remarketing list for a given advertiser and floodlight activity, using a
 * custom rule.
 *
 * Note: this sample assumes that the floodlight activity specified has a U1 custom floodlight
 * variable.
 */
public class CreateRemarketingList {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";
  private static final String FLOODLIGHT_ACTIVITY_ID = "ENTER_FLOODLIGHT_ACTIVITY_ID_HERE";
  private static final String REMARKETING_LIST_NAME = "ENTER_REMARKETING_LIST_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String remaketingListName,
      long advertiserId, long floodlightActivityId) throws Exception {
    // Create the remarketing list.
    RemarketingList remarketingList = new RemarketingList();
    remarketingList.setActive(true);
    remarketingList.setAdvertiserId(advertiserId);
    remarketingList.setLifeSpan(30L); // Set the membership lifespan to 30 days.
    remarketingList.setName(remaketingListName);

    // Create a list population term.
    // This term matches all visitors with a U1 value exactly matching "test_value".
    ListPopulationTerm term = new ListPopulationTerm();
    term.setOperator("STRING_EQUALS");
    term.setType("CUSTOM_VARIABLE_TERM");
    term.setValue("test_value");
    term.setVariableName("U1");

    // Add the term to a list population clause.
    ListPopulationClause clause = new ListPopulationClause();
    clause.setTerms(ImmutableList.of(term));

    // Add the clause to a list population rule.
    // This rule will target all visitors who trigger the specified floodlight activity and satisfy
    // the custom rule defined in the list population term.
    ListPopulationRule rule = new ListPopulationRule();
    rule.setFloodlightActivityId(floodlightActivityId);
    rule.setListPopulationClauses(ImmutableList.of(clause));
    remarketingList.setListPopulationRule(rule);

    // Insert the remarketing list.
    RemarketingList result =
        reporting.remarketingLists().insert(profileId, remarketingList).execute();

    // Display the new remarketing list ID.
    System.out.printf("Remarketing list with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long floodlightActivityId = Long.parseLong(FLOODLIGHT_ACTIVITY_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, REMARKETING_LIST_NAME, advertiserId, floodlightActivityId);
  }
}
