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
using System.IO;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;
using Google.Apis.Upload;

namespace DfaReporting.Samples {
  class CreativeAssetUtils {
    private static DfareportingService Service;
    private static long ProfileId;
    private static long AdvertiserId;

    public CreativeAssetUtils(DfareportingService service, long profileId, long advertiserId) {
      AdvertiserId = advertiserId;
      ProfileId = profileId;
      Service = service;
    }

    /// <summary>
    /// Uploads a creative asset and associates it with the specified advertiser.
    /// </summary>
    /// <param name="assetFile">The path to the asset file to be uploaded</param>
    /// <param name="assetType">The CreativeAssetId type of the asset</param>
    /// <returns>
    /// A CreativeAssetMetadata object populated with the name of the asset after insert.
    /// </returns>
    public CreativeAssetMetadata uploadAsset(String assetFile, String assetType) {
      // Prepare an input stream.
      FileStream assetContent = new FileStream(assetFile, FileMode.Open, FileAccess.Read);

      // Create the creative asset ID and Metadata.
      CreativeAssetId assetId = new CreativeAssetId();
      assetId.Name = Path.GetFileName(assetFile);
      assetId.Type = assetType;

      CreativeAssetMetadata metaData = new CreativeAssetMetadata();
      metaData.AssetIdentifier = assetId;

      // Insert the creative.
      String mimeType = determineMimeType(assetFile, assetType);
      CreativeAssetsResource.InsertMediaUpload request =
          Service.CreativeAssets.Insert(metaData, ProfileId, AdvertiserId, assetContent, mimeType);

      IUploadProgress progress = request.Upload();
      if (UploadStatus.Failed.Equals(progress.Status)) {
          throw progress.Exception;
      }

      // Display the new asset name.
      Console.WriteLine("Creative asset was saved with name \"{0}\".",
          request.ResponseBody.AssetIdentifier.Name);
      return request.ResponseBody;
    }

    private String determineMimeType(String assetFile, String assetType) {
      switch (assetType) {
        case "IMAGE":
        case "HTML_IMAGE":
          return "image/" + Path.GetExtension(assetFile);
        case "VIDEO":
          return "video/" + Path.GetExtension(assetFile);
        default:
          return "application/octet-stream";
      }
    }
  }
}
