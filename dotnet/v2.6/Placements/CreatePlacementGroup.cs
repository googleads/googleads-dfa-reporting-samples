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
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a placement group in a given campaign. Requires the
  /// DFA site ID and campaign ID in which the placement group will be created
  /// into. To create a campaign, run CreateCampaign.cs. To get DFA site ID,
  /// run GetSite.cs.
  /// </summary>
  class CreatePlacementGroup : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a placement group in a given campaign." +
            " Requires the DFA site ID and campaign ID in which the placement" +
            " group will be created into. To create a campaign, run" +
            " CreateCampaign.cs. To get DFA site ID, run GetSite.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreatePlacementGroup();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long campaignId = long.Parse(_T("INSERT_CAMPAIGN_ID_HERE"));
      long dfaSiteId = long.Parse(_T("INSERT_SITE_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string placementGroupName = _T("INSERT_PLACEMENT_GROUP_NAME_HERE");

      // Retrieve the campaign.
      Campaign campaign = service.Campaigns.Get(profileId, campaignId).Execute();

      // Create a pricing schedule.
      PricingSchedule pricingSchedule = new PricingSchedule();
      pricingSchedule.EndDate = campaign.EndDate;
      pricingSchedule.PricingType = "PRICING_TYPE_CPM";
      pricingSchedule.StartDate = campaign.StartDate;

      // Create the placement group.
      PlacementGroup placementGroup = new PlacementGroup();
      placementGroup.CampaignId = campaignId;
      placementGroup.Name = placementGroupName;
      placementGroup.PlacementGroupType = "PLACEMENT_PACKAGE";
      placementGroup.PricingSchedule = pricingSchedule;
      placementGroup.SiteId = dfaSiteId;

      // Insert the placement.
      PlacementGroup result =
          service.PlacementGroups.Insert(placementGroup, profileId).Execute();

      // Display the new placement ID.
      Console.WriteLine("Placement group with ID {0} was created.", result.Id);
    }
  }
}
