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
  /// This example creates a campaign associated with a given advertiser. To
  /// create an advertiser, run CreateAdvertiser.cs.
  /// </summary>
  class CreateCampaign : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a campaign associated with a given" +
            " advertiser. To create an advertiser, run CreateAdvertiser.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateCampaign();
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

      String campaignName = _T("INSERT_CAMPAIGN_NAME_HERE");
      String landingPageName = _T("INSERT_LANDING_PAGE_NAME_HERE");
      String url = _T("INSERT_LANDING_PAGE_URL_HERE");

      // Create the campaign structure.
      Campaign campaign = new Campaign();
      campaign.Name = campaignName;
      campaign.AdvertiserId = advertiserId;
      campaign.Archived = false;

      // Set the campaign start date. This example uses today's date.
      campaign.StartDate =
          DfaReportingDateConverterUtil.convertToDateString(DateTime.Now);

      // Set the campaign end date. This example uses one month from today's date.
      campaign.EndDate =
          DfaReportingDateConverterUtil.convertToDateString(DateTime.Now.AddMonths(1));

      // Insert the campaign.
      Campaign result =
          service.Campaigns.Insert(campaign, profileId, landingPageName, url).Execute();

      // Display the new campaign ID.
      Console.WriteLine("Campaign with ID {0} was created.", result.Id);
    }
  }
}
