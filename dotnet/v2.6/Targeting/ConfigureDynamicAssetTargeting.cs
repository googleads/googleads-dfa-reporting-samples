/*
 * Copyright 2016 Google Inc
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
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example uploads a new video asset to an existing in-stream video creative and configures
  /// dynamic asset selection, using a specified targeting template. To get an in-stream video
  /// creative, run CreateInstreamVideoCreative.cs. To get a targeting template, run
  /// CreateTargetingTemplate.cs.
  /// </summary>
  class ConfigureDynamicAssetTargeting : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example uploads a new video asset to an existing in-stream video creative " +
            "and configures dynamic asset selection, using a specified targeting template. To " +
            "get an in-stream video creative, run CreateInstreamVideoCreative.cs. To get a " +
            "targeting template, run CreateTargetingTemplate.cs.";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new ConfigureDynamicAssetTargeting();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long instreamVideoCreativeId = long.Parse(_T("INSERT_INSTREAM_VIDEO_CREATIVE_ID_HERE"));
      long targetingTemplateId = long.Parse(_T("INSERT_TARGETING_TEMPLATE_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string videoAssetName = _T("INSERT_VIDEO_ASSET_NAME_HERE");
      string pathToVideoAssetFile = _T("INSERT_PATH_TO_VIDEO_ASSET_FILE_HERE");

      // Retrieve the specified creative.
      Creative creative = service.Creatives.Get(profileId, instreamVideoCreativeId).Execute();
      if (creative == null || !"INSTREAM_VIDEO".Equals(creative.Type)) {
        Console.Error.WriteLine("Invalid creative specified.");
        return;
      }

      CreativeAssetSelection selection = creative.CreativeAssetSelection;
      if (!creative.DynamicAssetSelection.Value) {
        // Locate an existing video asset to use as a default.
        // This example uses the first PARENT_VIDEO asset found.
        CreativeAsset defaultAsset =
            creative.CreativeAssets.First(asset => "PARENT_VIDEO".Equals(asset.Role));
        if (defaultAsset == null) {
          Console.Error.WriteLine("Default video asset could not be found.");
          return;
        }

        // Create a new selection using the existing asset as a default.
        selection = new CreativeAssetSelection();
        selection.DefaultAssetId = defaultAsset.Id;
        selection.Rules = new List<Rule>();

        // Enable dynamic asset selection for the creative.
        creative.DynamicAssetSelection = true;
        creative.CreativeAssetSelection = selection;
      }

      // Upload the new video asset and add it to the creative.
      CreativeAssetUtils assetUtils = new CreativeAssetUtils(service, profileId,
          creative.AdvertiserId.Value);
      CreativeAssetMetadata videoMetadata = assetUtils.uploadAsset(pathToVideoAssetFile, "VIDEO");
      creative.CreativeAssets.Add(new CreativeAsset() {
        AssetIdentifier = videoMetadata.AssetIdentifier,
        Role = "PARENT_VIDEO"
      });

      // Create a rule targeting the new video asset and add it to the selection.
      Rule rule = new Rule();
      rule.AssetId = videoMetadata.Id;
      rule.Name = "Test rule for asset " + videoMetadata.Id;
      rule.TargetingTemplateId = targetingTemplateId;
      selection.Rules.Add(rule);

      // Update the creative.
      Creative result = service.Creatives.Update(creative, profileId).Execute();
      Console.WriteLine("Dynamic asset selection enabled for creative with ID {0}.", result.Id);
    }
  }
}
