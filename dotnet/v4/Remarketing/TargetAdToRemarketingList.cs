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
using Google.Apis.Dfareporting.v4;
using Google.Apis.Dfareporting.v4.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example targets an ad to a remarketing list.
  ///
  /// The first targetable remarketing list, either owned by or shared to the ad's advertiser,
  /// will be used. To create a remarketing list, see CreateRemarketingList.cs. To share a
  /// remarketing list with the ad's advertiser, see ShareRemarketingListToAdvertiser.cs.
  /// </summary>
  class TargetAdToRemarketingList : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example targets an ad to a remarketing list.\n\nThe first targetable" +
            " remarketing list, either owned by or shared to the ad's advertiser, will be used." +
            " To create a remarketing list, see CreateRemarketingList.cs. To share a remarketing" +
            " list with the ad's advertiser, see ShareRemarketingListToAdvertiser.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new TargetAdToRemarketingList();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long adId = long.Parse(_T("INSERT_AD_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      Ad ad = service.Ads.Get(profileId, adId).Execute();

      TargetableRemarketingListsResource.ListRequest listRequest =
          service.TargetableRemarketingLists.List(profileId, ad.AdvertiserId.Value);
      listRequest.MaxResults = 1;
      TargetableRemarketingListsListResponse lists = listRequest.Execute();

      if(lists.TargetableRemarketingLists.Any()) {
        // Select the first targetable remarketing list that was returned.
        TargetableRemarketingList list = lists.TargetableRemarketingLists.First();

        // Create a list targeting expression.
        ListTargetingExpression expression = new ListTargetingExpression();
        expression.Expression = list.Id.ToString();

        // Update the ad.
        ad.RemarketingListExpression = expression;
        Ad result = service.Ads.Update(ad, profileId).Execute();

        Console.WriteLine(
            "Ad with ID {0} updated to use remarketing list expression: {1}.\n",
            result.Id, result.RemarketingListExpression.Expression);
      } else {
        Console.WriteLine("No targetable remarketing lists found for ad with ID {0}.\n", adId);
      }
    }
  }
}
