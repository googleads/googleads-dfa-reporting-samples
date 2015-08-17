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

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.RemarketingList;
import com.google.api.services.dfareporting.model.RemarketingListsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all remarketing lists owned by the specified advertiser.
 *
 * Note: the RemarketingLists resource will only return lists owned by the specified advertiser. To
 * see all lists that can be used for targeting ads (including those shared from other accounts or
 * advertisers), use the TargetableRemarketingLists resource instead.
 */
public class GetRemarketingLists {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,remarketingLists(accountId,advertiserId,id,name)";

    RemarketingListsListResponse lists;
    String nextPageToken = null;

    do {
      // Create and execute the remarketing list request.
      lists = reporting.remarketingLists().list(profileId, advertiserId).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (RemarketingList list : lists.getRemarketingLists()) {
        System.out.printf("Remarketing list with ID %d and name \"%s\" was found.%n",
            list.getId(), list.getName());
      }

      // Update the next page token.
      nextPageToken = lists.getNextPageToken();
    } while (!lists.getRemarketingLists().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
