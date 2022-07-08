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
using Google.Apis.Dfareporting.v4;
using Google.Apis.Dfareporting.v4.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example uploads creative assets and creates an in-stream audio
  /// creative associated with a given advertiser.
  /// </summary>
  class CreateInstreamAudioCreative : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example uploads creative assets and creates an" +
            " in-stream audio creative associated with a given advertiser.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateInstreamAudioCreative();
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

      string audioAssetName = _T("INSERT_AUDIO_ASSET_NAME_HERE");
      string pathToAudioAssetFile = _T("INSERT_PATH_TO_AUDIO_ASSET_FILE_HERE");

      Creative creative = new Creative();
      creative.AdvertiserId = advertiserId;
      creative.Name = "Test in-stream audio creative";
      creative.Type = "INSTREAM_AUDIO";

      // Upload the audio asset.
      CreativeAssetUtils assetUtils = new CreativeAssetUtils(service, profileId, advertiserId);
      CreativeAssetId audioAssetId =
          assetUtils.uploadAsset(pathToAudioAssetFile, "AUDIO").AssetIdentifier;

      CreativeAsset audioAsset = new CreativeAsset();
      audioAsset.AssetIdentifier = audioAssetId;
      audioAsset.Role = "PARENT_AUDIO";

      // Add the creative assets.
      creative.CreativeAssets = new List<CreativeAsset>() { audioAsset };

      Creative result = service.Creatives.Insert(creative, profileId).Execute();

      // Display the new creative ID.
      Console.WriteLine("In-stream audio creative with ID {0} was created.", result.Id);
    }
  }
}
