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

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Subaccount;
import com.google.api.services.dfareporting.model.UserRolePermission;
import com.google.api.services.dfareporting.model.UserRolePermissionsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all of the available subaccount permissions.
 *
 * To get a subaccount ID, run get_subaccounts.py.
 */
public class GetUserRolePermissions {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String SUBACCOUNT_ID = "ENTER_SUBACCOUNT_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long subaccountId)
      throws Exception {
    // Limit the fields returned.
    String fields = "userRolePermissions(id,name)";

    // Retrieve the subaccount.
    Subaccount subaccount = reporting.subaccounts().get(profileId, subaccountId).execute();

    // Retrieve the subaccount permissions.
    UserRolePermissionsListResponse permissions = reporting.userRolePermissions().list(profileId)
        .setIds(subaccount.getAvailablePermissionIds()).setFields(fields).execute();

    // Display the subaccount permissions.
    for (UserRolePermission permission : permissions.getUserRolePermissions()) {
      System.out.printf("User role permission with ID %d and name \"%s\" was found.%n",
          permission.getId(), permission.getName());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long subaccountId = Long.parseLong(SUBACCOUNT_ID);

    runExample(reporting, profileId, subaccountId);
  }
}
