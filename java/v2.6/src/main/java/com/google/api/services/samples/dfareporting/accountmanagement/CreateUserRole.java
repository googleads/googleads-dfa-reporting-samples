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
import com.google.api.services.dfareporting.model.UserRole;
import com.google.api.services.dfareporting.model.UserRolePermission;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * This example creates a user role in a given DoubleClick Campaign Manager subaccount. To get the
 * subaccount ID, run GetSubaccounts.java. To get the available permissions, run
 * GetUserRolePermissions.java. To get the parent user role ID, run GetUserRoles.java.
 */
public class CreateUserRole {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String PARENT_USER_ROLE_ID = "INSERT_PARENT_USER_ROLE_ID_HERE";
  private static final String PERMISSION_ID_ONE = "INSERT_FIRST_PERMISSION_ID_HERE";
  private static final String PERMISSION_ID_TWO = "INSERT_SECOND_PERMISSION_ID_HERE";
  private static final String SUBACCOUNT_ID = "INSERT_SUBACCOUNT_ID_HERE";
  private static final String USER_ROLE_NAME = "INSERT_USER_ROLE_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String userRoleName,
      long subaccountId, long parentUserRoleId, long permission1Id, long permission2Id)
      throws Exception {
    // Create user role structure.
    UserRole userRole = new UserRole();
    userRole.setName(userRoleName);
    userRole.setSubaccountId(subaccountId);
    userRole.setParentUserRoleId(parentUserRoleId);

    // Create a permission object to represent each permission this user role
    // has.
    UserRolePermission permission1 = new UserRolePermission();
    permission1.setId(permission1Id);
    UserRolePermission permission2 = new UserRolePermission();
    permission2.setId(permission2Id);
    List<UserRolePermission> permissions = ImmutableList.of(permission1, permission2);

    // Add the permissions to the user role.
    userRole.setPermissions(permissions);

    // Create user role.
    UserRole result = reporting.userRoles().insert(profileId, userRole).execute();

    // Display user role ID.
    System.out.printf("User role with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long parentUserRoleId = Long.parseLong(PARENT_USER_ROLE_ID);
    long permission1Id = Long.parseLong(PERMISSION_ID_ONE);
    long permission2Id = Long.parseLong(PERMISSION_ID_TWO);
    long profileId = Long.parseLong(USER_PROFILE_ID);
    long subaccountId = Long.parseLong(SUBACCOUNT_ID);

    runExample(reporting, profileId, USER_ROLE_NAME, subaccountId, parentUserRoleId, permission1Id,
        permission2Id);
  }
}
