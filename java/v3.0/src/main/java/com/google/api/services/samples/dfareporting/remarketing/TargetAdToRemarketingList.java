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
import com.google.api.services.dfareporting.model.Ad;
import com.google.api.services.dfareporting.model.ListTargetingExpression;
import com.google.api.services.dfareporting.model.TargetableRemarketingList;
import com.google.api.services.dfareporting.model.TargetableRemarketingListsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example targets an ad to a remarketing list.
 *
 * The first targetable remarketing list, either owned by or shared to the ad's advertiser, will be
 * used. To create a remarketing list, see CreateRemarketingList.java. To share a remarketing list
 * with the ad's advertiser, see ShareRemarketingListToAdvertiser.java.
 */
public class TargetAdToRemarketingList {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String AD_ID = "ENTER_AD_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long adId)
      throws Exception {
    Ad ad = reporting.ads().get(profileId, adId).execute();

    TargetableRemarketingListsListResponse lists = reporting.targetableRemarketingLists()
        .list(profileId, ad.getAdvertiserId()).setMaxResults(1).execute();

    if(!lists.getTargetableRemarketingLists().isEmpty()) {
      // Select the first targetable remarketing list that was returned.
      TargetableRemarketingList list = lists.getTargetableRemarketingLists().get(0);

      // Create a list targeting expression.
      ListTargetingExpression expression = new ListTargetingExpression();
      expression.setExpression(list.getId().toString());

      // Update the ad.
      ad.setRemarketingListExpression(expression);
      Ad result = reporting.ads().update(profileId, ad).execute();

      System.out.printf("Ad with ID %d updated to use remarketing list expression: \"%s\".%n",
          result.getId(), result.getRemarketingListExpression().getExpression());
    } else {
      System.out.printf("No targetable remarketing lists found for ad with ID %d", adId);
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long adId = Long.parseLong(AD_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, adId);
  }
}
