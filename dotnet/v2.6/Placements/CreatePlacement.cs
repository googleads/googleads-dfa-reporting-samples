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
  /// This example creates a placement in a given campaign. Requires a DFA site
  /// ID and ID of the campaign in which the placement will be created. To
  /// create a campaign, run CreateCampaign.cs. To get a DFA site ID, run
  /// GetSite.cs. To get a size ID, run GetSize.cs.
  /// </summary>
  class CreatePlacement : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a placement in a given campaign." +
            " Requires a DFA site ID and ID of the campaign in which the" +
            " placement will be created. To create a campaign, run" +
            " CreateCampaign.cs. To get a DFA site ID, run GetSite.cs. To get" +
            " a size ID, run GetSize.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreatePlacement();
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
      long sizeId = long.Parse(_T("INSERT_SIZE_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string placementName = _T("INSERT_PLACEMENT_NAME_HERE");

      // Retrieve the campaign.
      Campaign campaign = service.Campaigns.Get(profileId, campaignId).Execute();

      // Create the placement.
      Placement placement = new Placement();
      placement.Name = placementName;
      placement.CampaignId = campaignId;
      placement.Compatibility = "DISPLAY";
      placement.PaymentSource = "PLACEMENT_AGENCY_PAID";
      placement.SiteId = dfaSiteId;
      placement.TagFormats = new List<string>() { "PLACEMENT_TAG_STANDARD" };

      // Set the size of the placement.
      Size size = new Size();
      size.Id = sizeId;
      placement.Size = size;

      // Set the pricing schedule for the placement.
      PricingSchedule pricingSchedule = new PricingSchedule();
      pricingSchedule.EndDate = campaign.EndDate;
      pricingSchedule.PricingType = "PRICING_TYPE_CPM";
      pricingSchedule.StartDate = campaign.StartDate;
      placement.PricingSchedule = pricingSchedule;

      // Insert the placement.
      Placement result = service.Placements.Insert(placement, profileId).Execute();

      // Display the new placement ID.
      Console.WriteLine("Placement with ID {0} was created.", result.Id);
    }
  }
}
