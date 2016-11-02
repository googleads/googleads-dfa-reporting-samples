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
  /// This example downloads HTML Tags for a given campaign and placement ID.
  /// To create campaigns, run CreateCampaign.cs. To create placements, run
  /// CreatePlacement.cs.
  /// </summary>
  class DownloadPlacementTags : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example downloads HTML Tags for a given campaign and" +
            " placement ID. To create campaigns, run CreateCampaign.cs. To" +
            " create placements, run CreatePlacement.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new DownloadPlacementTags();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long campaignId = long.Parse(_T("ENTER_CAMPAIGN_ID_HERE"));
      long placementId = long.Parse(_T("ENTER_PLACEMENT_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Generate the placement activity tags.
      PlacementsResource.GeneratetagsRequest request =
          service.Placements.Generatetags(profileId);
      request.CampaignId = campaignId;
      request.TagFormats =
          PlacementsResource.GeneratetagsRequest.TagFormatsEnum.PLACEMENTTAGSTANDARD;
      request.PlacementIds = placementId.ToString();

      PlacementsGenerateTagsResponse response = request.Execute();

      // Display the placement activity tags.
      foreach (PlacementTag tag in response.PlacementTags) {
        foreach (TagData tagData in tag.TagDatas) {
          Console.WriteLine("{0}:\n", tagData.Format);

          if (tagData.ImpressionTag != null) {
            Console.WriteLine(tagData.ImpressionTag);
          }

          if (tagData.ClickTag != null) {
            Console.WriteLine(tagData.ClickTag);
          }

          Console.WriteLine();
        }
      }
    }
  }
}
