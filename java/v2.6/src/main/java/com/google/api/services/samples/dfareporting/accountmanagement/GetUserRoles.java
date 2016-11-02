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
import com.google.api.services.dfareporting.model.UserRole;
import com.google.api.services.dfareporting.model.UserRolesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all user roles.
 *
 * The output is limited to include only ID, name, account ID and subaccount ID.
 */
public class GetUserRoles {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,userRoles(accountId,id,name,subaccountId)";

    UserRolesListResponse roles;
    String nextPageToken = null;

    do {
      // Create and execute the user roles list request.
      roles = reporting.userRoles().list(profileId).setFields(fields).setPageToken(nextPageToken)
          .execute();

      for (UserRole role : roles.getUserRoles()) {
        System.out.printf("User role with ID %d and name \"%s\" was found.%n", role.getId(),
            role.getName());
      }

      // Update the next page token.
      nextPageToken = roles.getNextPageToken();
    } while (!roles.getUserRoles().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
