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
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * This example creates a subaccount in a given DoubleClick Campaign Manager account. To get the
 * available permissions, run GetUserRolePermissions.java.
 */
public class CreateSubaccount {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ACCOUNT_ID = "INSERT_ACCOUNT_ID_HERE";
  private static final String PERMISSION_ONE = "INSERT_FIRST_PERMISSION_ID_HERE";
  private static final String PERMISSION_TWO = "INSERT_SECOND_PERMISSION_ID_HERE";
  private static final String SUBACCOUNT_NAME = "INSERT_SUBACCOUNT_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String subaccountName,
      long accountId, long permissionOneId, long permissionTwoId) throws Exception {
    // Create subaccount structure.
    Subaccount subaccount = new Subaccount();
    subaccount.setName(subaccountName);
    subaccount.setAccountId(accountId);

    // Create a collection of all permissions assigned to this subaccount and add it to the
    // subaccount structure. To get list of available permissions, run GetUserRolePermissions.java.
    List<Long> availablePermissionIds = ImmutableList.of(permissionOneId, permissionTwoId);
    subaccount.setAvailablePermissionIds(availablePermissionIds);

    // Create subaccount.
    Subaccount result = reporting.subaccounts().insert(profileId, subaccount).execute();

    // Display subaccount ID.
    System.out.printf("Subaccount with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long accountId = Long.parseLong(ACCOUNT_ID);
    long permissionOneId = Long.parseLong(PERMISSION_ONE);
    long permissionTwoId = Long.parseLong(PERMISSION_TWO);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, SUBACCOUNT_NAME, accountId, permissionOneId, permissionTwoId);
  }
}
