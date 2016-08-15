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
  /// This example uploads creative assets and creates a display image
  /// gallery creative associated with a given advertiser. To get a size ID,
  /// run GetSize.cs.
  /// </summary>
  class CreateDisplayImageGalleryCreative : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example uploads creative assets and creates a display" +
            " image gallery creative associated with a given advertiser. To" +
            " get a size ID, run GetSize.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateDisplayImageGalleryCreative();
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
      long sizeId = long.Parse(_T("INSERT_SIZE_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string pathToImageAssetFile = _T("INSERT_PATH_TO_IMAGE_ASSET_FILE_HERE");
      string pathToImageAsset2File = _T("INSERT_PATH_TO_IMAGE_ASSET_FILE_HERE");

      Creative creative = new Creative();
      creative.AdvertiserId = advertiserId;
      creative.AutoAdvanceImages = true;
      creative.Name = "Test display image gallery creative";
      creative.Size = new Size() { Id = sizeId };
      creative.Type = "DISPLAY_IMAGE_GALLERY";

      // Upload the first image asset.
      CreativeAssetUtils assetUtils = new CreativeAssetUtils(service, profileId, advertiserId);
      CreativeAssetId imageAsset1Id =
          assetUtils.uploadAsset(pathToImageAssetFile, "HTML_IMAGE").AssetIdentifier;

      CreativeAsset imageAsset1 = new CreativeAsset();
      imageAsset1.AssetIdentifier = imageAsset1Id;
      imageAsset1.Role = "PRIMARY";

      // Upload the second image asset.
      CreativeAssetId imageAsset2Id =
          assetUtils.uploadAsset(pathToImageAsset2File, "HTML_IMAGE").AssetIdentifier;

      CreativeAsset imageAsset2 = new CreativeAsset();
      imageAsset2.AssetIdentifier = imageAsset2Id;
      imageAsset2.Role = "PRIMARY";

      // Add the creative assets.
      creative.CreativeAssets = new List<CreativeAsset>() { imageAsset1, imageAsset2 };

      // Create a click tag for the first image asset.
      ClickTag clickTag1 = new ClickTag();
      clickTag1.Name = imageAsset1Id.Name;
      clickTag1.EventName = imageAsset1Id.Name;

      // Create a click tag for the second image asset.
      ClickTag clickTag2 = new ClickTag();
      clickTag2.Name = imageAsset2Id.Name;
      clickTag2.EventName = imageAsset2Id.Name;

      // Add the click tags.
      creative.ClickTags = new List<ClickTag>() { clickTag1, clickTag2 };

      Creative result = service.Creatives.Insert(creative, profileId).Execute();

      // Display the new creative ID.
      Console.WriteLine("Display image gallery creative with ID {0} was created.", result.Id);
    }
  }
}
