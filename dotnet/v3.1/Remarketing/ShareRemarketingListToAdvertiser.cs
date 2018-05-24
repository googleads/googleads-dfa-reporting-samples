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
using System.Linq;
using Google.Apis.Dfareporting.v3_1;
using Google.Apis.Dfareporting.v3_1.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example shares an existing remarketing list with the specified advertiser.
  /// </summary>
  class ShareRemarketingListToAdvertiser : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example shares an existing remarketing list with the specified" +
            " advertiser.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new ShareRemarketingListToAdvertiser();
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
      long remarketingListId = long.Parse(_T("INSERT_REMARKETING_LIST_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Load the existing share info.
      RemarketingListShare share =
          service.RemarketingListShares.Get(profileId, remarketingListId).Execute();

      if(share.SharedAdvertiserIds == null) {
        share.SharedAdvertiserIds = new List<long?> {};
      }

      if(!share.SharedAdvertiserIds.Contains(advertiserId)) {
        share.SharedAdvertiserIds.Add(advertiserId);

        // Update the share info with the newly added advertiser ID.
        RemarketingListShare result =
            service.RemarketingListShares.Update(share, profileId).Execute();

        Console.WriteLine(
            "Remarketing list with ID {0} is now shared to advertiser ID(s): {1}.\n",
            result.RemarketingListId, string.Join(", ", result.SharedAdvertiserIds));
      } else {
        Console.WriteLine(
            "Remarketing list with ID {0} is already shared to advertiser ID {1}.\n",
            remarketingListId, advertiserId);
      }
    }
  }
}
