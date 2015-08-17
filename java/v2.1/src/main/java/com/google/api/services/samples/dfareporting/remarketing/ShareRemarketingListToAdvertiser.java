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
import com.google.api.services.dfareporting.model.RemarketingListShare;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.base.Joiner;

import java.util.ArrayList;

/**
 * This example shares an existing remarketing list with the specified advertiser.
 */
public class ShareRemarketingListToAdvertiser {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "ENTER_ADVERTISER_ID_HERE";
  private static final String REMARKETING_LIST_ID = "ENTER_REMARKETING_LIST_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId,
      long remarketingListId) throws Exception {
    // Load the existing share info.
    RemarketingListShare share =
        reporting.remarketingListShares().get(profileId, remarketingListId).execute();

    if(share.getSharedAdvertiserIds() == null) {
      share.setSharedAdvertiserIds(new ArrayList<Long>());
    }

    if(!share.getSharedAdvertiserIds().contains(advertiserId)) {
      share.getSharedAdvertiserIds().add(advertiserId);

      // Update the share info with the newly added advertiser ID.
      RemarketingListShare result =
          reporting.remarketingListShares().update(profileId, share).execute();

      String sharedAdvertiserIds = Joiner.on(", ").join(result.getSharedAdvertiserIds());
      System.out.printf("Remarketing list with ID %s is now shared to advertiser ID(s): %s.%n",
          result.getRemarketingListId(), sharedAdvertiserIds);
    } else {
      System.out.printf("Remarketing list with ID %d is already shared to advertiser ID %d.%n",
          remarketingListId, advertiserId);
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);
    long remarketingListId = Long.parseLong(REMARKETING_LIST_ID);

    runExample(reporting, profileId, advertiserId, remarketingListId);
  }
}
