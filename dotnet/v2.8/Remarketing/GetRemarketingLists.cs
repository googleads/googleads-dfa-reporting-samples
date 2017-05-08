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
using System.Linq;
using Google.Apis.Dfareporting.v2_8;
using Google.Apis.Dfareporting.v2_8.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example displays all remarketing lists owned by the specified advertiser.
  ///
  /// Note: the RemarketingLists resource will only return lists owned by the specified advertiser.
  /// To see all lists that can be used for targeting ads (including those shared from other
  /// accounts or advertisers), use the TargetableRemarketingLists resource instead.
  /// </summary>
  class GetRemarketingLists : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example displays all remarketing lists owned by the specified advertiser." +
            "\n\nNote: the RemarketingLists resource will only return lists owned by the" +
            " specified advertiser. To see all lists that can be used for targeting ads" +
            " including those shared from other accounts or advertisers), use the" +
            " TargetableRemarketingLists resource instead.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetRemarketingLists();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long advertiserId = long.Parse(_T("INSERT_ADVERTISER_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Limit the fields returned.
      String fields = "nextPageToken,remarketingLists(accountId,advertiserId,id,name)";

      RemarketingListsListResponse lists;
      String nextPageToken = null;

      do {
        // Create and execute the remaketing lists list request
        RemarketingListsResource.ListRequest request =
            service.RemarketingLists.List(profileId, advertiserId);
        request.Fields = fields;
        request.PageToken = nextPageToken;
        lists = request.Execute();

        foreach (RemarketingList list in lists.RemarketingLists) {
          Console.WriteLine(
              "Remarketing list with ID {0} and name \"{1}\" was found.",
              list.Id, list.Name);
        }

        // Update the next page token
        nextPageToken = lists.NextPageToken;
      } while (lists.RemarketingLists.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
