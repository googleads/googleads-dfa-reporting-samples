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
  /// This example creates a rotation group ad in a given campaign. Start and
  /// end date for the ad must be within campaign start and end dates. To
  /// create creatives, run one of the Create*Creative.cs examples. To get
  /// available placements, run GetPlacements.cs.
  /// </summary>
  class CreateRotationGroup : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a rotation group ad in a given campaign." +
            " Start and end date for the ad must be within campaign start and" +
            " end dates. To create creatives, run one of the Create*Creative.cs" +
            " examples. To get available placements, run GetPlacements.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateRotationGroup();
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
      long creativeId = long.Parse(_T("INSERT_CREATIVE_ID_HERE"));
      long placementId = long.Parse(_T("INSERT_PLACEMENT_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));

      String adName = _T("INSERT_AD_NAME_HERE");

      // Retrieve the campaign.
      Campaign campaign = service.Campaigns.Get(profileId, campaignId).Execute();

      // Create a click-through URL.
      ClickThroughUrl clickThroughUrl = new ClickThroughUrl();
      clickThroughUrl.DefaultLandingPage = true;

      // Create a creative assignment.
      CreativeAssignment creativeAssignment = new CreativeAssignment();
      creativeAssignment.Active = true;
      creativeAssignment.CreativeId = creativeId;
      creativeAssignment.ClickThroughUrl = clickThroughUrl;

      // Create a placement assignment.
      PlacementAssignment placementAssignment = new PlacementAssignment();
      placementAssignment.Active = true;
      placementAssignment.PlacementId = placementId;

      // Create a creative rotation.
      CreativeRotation creativeRotation = new CreativeRotation();
      creativeRotation.CreativeAssignments = new List<CreativeAssignment>() {
          creativeAssignment
      };

      // Create a delivery schedule.
      DeliverySchedule deliverySchedule = new DeliverySchedule();
      deliverySchedule.ImpressionRatio = 1;
      deliverySchedule.Priority = "AD_PRIORITY_01";

      DateTime startDate = DateTime.Now;
      DateTime endDate = Convert.ToDateTime(campaign.EndDate);

      // Create a rotation group.
      Ad rotationGroup = new Ad();
      rotationGroup.Active = true;
      rotationGroup.CampaignId = campaignId;
      rotationGroup.CreativeRotation = creativeRotation;
      rotationGroup.DeliverySchedule = deliverySchedule;
      rotationGroup.StartTime = startDate;
      rotationGroup.EndTime = endDate;
      rotationGroup.Name = adName;
      rotationGroup.PlacementAssignments = new List<PlacementAssignment>() {
          placementAssignment
      };
      rotationGroup.Type = "AD_SERVING_STANDARD_AD";

      // Insert the rotation group.
      Ad result = service.Ads.Insert(rotationGroup, profileId).Execute();

      // Display the new ad ID.
      Console.WriteLine("Ad with ID {0} was created.", result.Id);
    }
  }
}
