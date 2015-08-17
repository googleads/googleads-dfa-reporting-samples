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

package com.google.api.services.samples.dfareporting.accountmanagement;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Subaccount;
import com.google.api.services.dfareporting.model.SubaccountsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all subaccounts.
 *
 * Note that the permissions assigned to a subaccount are not returned in a human-readable format
 * with this example. Run GetUserRolePermissions.java to see what permissions are available on a
 * subaccount.
 */
public class GetSubaccounts {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,subaccounts(id,name)";

    SubaccountsListResponse subaccounts;
    String nextPageToken = null;

    do {
      // Create and execute the subaccounts list request.
      subaccounts = reporting.subaccounts().list(profileId).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (Subaccount subaccount : subaccounts.getSubaccounts()) {
        System.out.printf("Subaccount with ID %d and name \"%s\" was found.%n", subaccount.getId(),
            subaccount.getName());
      }

      // Update the next page token.
      nextPageToken = subaccounts.getNextPageToken();
    } while (!subaccounts.getSubaccounts().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
