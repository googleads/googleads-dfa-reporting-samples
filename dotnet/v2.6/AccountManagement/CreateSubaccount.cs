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
  /// This example creates a subaccount in a given DoubleClick Campaign Manager
  /// account. To get the available permissions, run
  /// GetUserRolePermissions.cs.
  /// </summary>
  class CreateSubaccount : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a subaccount in a given DoubleClick" +
            " Campaign Manager account. To get the available permissions, run" +
            " GetUserRolePermissions.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateSubaccount();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long accountId = long.Parse(_T("INSERT_ACCOUNT_ID_HERE"));
      long permissionOneId = long.Parse(_T("INSERT_FIRST_PERMISSION_ID_HERE"));
      long permissionTwoId = long.Parse(_T("INSERT_SECOND_PERMISSION_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      String subaccountName = _T("INSERT_SUBACCOUNT_NAME_HERE");

      // Create subaccount structure.
      Subaccount subaccount = new Subaccount();
      subaccount.Name = subaccountName;
      subaccount.AccountId = accountId;

      // Create a collection of all permissions assigned to this subaccount and add it to the
      // subaccount structure. To get list of available permissions, run GetUserRolePermissions.cs.
      subaccount.AvailablePermissionIds = new List<long?> { permissionOneId, permissionTwoId };

      // Create subaccount.
      Subaccount result = service.Subaccounts.Insert(subaccount, profileId).Execute();

      // Display subaccount ID.
      Console.WriteLine("Subaccount with ID {0} was created.", result.Id);
    }
  }
}
