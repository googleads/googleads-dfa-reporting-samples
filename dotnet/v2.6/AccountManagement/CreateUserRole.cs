/*
 * Copyright 2015 Google Inc
 *
 * Licensed under the Apache License, Version 2.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

using System;
using System.Collections.Generic;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a user role in a given DoubleClick Campaign Manager
  /// subaccount. To get the subaccount ID, run GetSubaccounts.cs. To get the
  /// available permissions, run GetUserRolePermissions.cs. To get the parent
  /// user role ID, run GetUserRoles.cs.
  /// </summary>
  class CreateUserRole : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a user role in a given DoubleClick" +
            " Campaign Manager subaccount. To get the subaccount ID, run" +
            " GetSubaccounts.cs. To get the available permissions, run" +
            " GetUserRolePermissions.cs. To get the parent user role ID, run" +
            " GetUserRoles.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateUserRole();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long parentUserRoleId = long.Parse(_T("INSERT_PARENT_USER_ROLE_ID_HERE"));
      long permission1Id = long.Parse(_T("INSERT_FIRST_PERMISSION_ID_HERE"));
      long permission2Id = long.Parse(_T("INSERT_SECOND_PERMISSIONS_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));
      long subaccountId = long.Parse(_T("INSERT_SUBACCOUNT_ID_HERE"));

      String userRoleName = _T("INSERT_USER_ROLE_NAME_HERE");

      // Create user role structure.
      UserRole userRole = new UserRole();
      userRole.Name = userRoleName;
      userRole.SubaccountId = subaccountId;
      userRole.ParentUserRoleId = parentUserRoleId;

      // Create a permission object to represent each permission this user role
      // has.
      UserRolePermission permission1 = new UserRolePermission();
      permission1.Id = permission1Id;
      UserRolePermission permission2 = new UserRolePermission();
      permission2.Id = permission2Id;
      List<UserRolePermission> permissions =
          new List<UserRolePermission> { permission1, permission2 };

      // Add the permissions to the user role.
      userRole.Permissions = permissions;

      // Create user role.
      UserRole result = service.UserRoles.Insert(userRole, profileId).Execute();

      // Display user role ID.
      Console.WriteLine("User role with ID {0} was created.", result.Id);
    }
  }
}
