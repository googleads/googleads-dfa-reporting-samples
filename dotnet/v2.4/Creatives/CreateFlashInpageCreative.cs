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
using Google.Apis.Dfareporting.v2_4;
using Google.Apis.Dfareporting.v2_4.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example uploads creative assets and creates a flash display creative
  /// associated with a given advertiser. To get a size ID, run GetSize.cs.
  /// </summary>
  class CreateFlashInpageCreative : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example uploads creative assets and creates a flash" +
            " display creative associated with a given advertiser. To get a" +
            " size ID, run GetSize.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateFlashInpageCreative();
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

      string pathToFlashAssetFile = _T("INSERT_PATH_TO_FLASH_ASSET_FILE_HERE");
      string pathToImageAssetFile = _T("INSERT_PATH_TO_IMAGE_ASSET_FILE_HERE");

      Creative creative = new Creative();
      creative.AdvertiserId = advertiserId;
      creative.Name = "Test flash display creative";
      creative.Size = new Size() { Id = sizeId };
      creative.Type = "ENHANCED_BANNER";

      // Upload the flash asset.
      CreativeAssetUtils assetUtils = new CreativeAssetUtils(service, profileId, advertiserId);
      CreativeAssetId flashAssetId = assetUtils.uploadAsset(pathToFlashAssetFile, "FLASH");

      CreativeAsset flashAsset = new CreativeAsset();
      flashAsset.AssetIdentifier = flashAssetId;
      flashAsset.Role = "PRIMARY";
      flashAsset.WindowMode = "TRANSPARENT";

      // Upload the backup image asset.
      CreativeAssetId imageAssetId = assetUtils.uploadAsset(pathToImageAssetFile, "HTML_IMAGE");

      CreativeAsset imageAsset = new CreativeAsset();
      imageAsset.AssetIdentifier = imageAssetId;
      imageAsset.Role = "BACKUP_IMAGE";

      // Add the creative assets.
      creative.CreativeAssets = new List<CreativeAsset>() { flashAsset, imageAsset };

      // Configure the backup image.
      creative.BackupImageClickThroughUrl = "https://www.google.com";
      creative.BackupImageReportingLabel = "backup";
      creative.BackupImageTargetWindow = new TargetWindow() { TargetWindowOption = "NEW_WINDOW"};

      Creative result = service.Creatives.Insert(creative, profileId).Execute();

      // Display the new creative ID.
      Console.WriteLine("Flash display creative with ID {0} was created.", result.Id);
    }
  }
}
