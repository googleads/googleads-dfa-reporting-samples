// Copyright 2014 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.services.samples.dfareporting.creatives.assets;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.CreativeAssetId;
import com.google.api.services.dfareporting.model.CreativeAssetMetadata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Creative asset utilities used by DFA Reporting and Trafficking API creative examples.
 */
public class CreativeAssetUtils {

  /**
   * Uploads a creative asset and associates it with the specified advertiser.
   *
   * @param reporting An initialized {@link Dfareporting} service object
   * @param profileId The current user's profile ID
   * @param advertiserId The advertiser to associate this creative asset with
   * @param assetName A suggested name for the asset
   * @param assetFile The path to the asset file to be uploaded
   * @param assetType The CreativeAssetId type of the asset
   * @return A {@link CreativeAssetMetadata} populated with the name of the asset after insert.
   */
  public static CreativeAssetMetadata uploadAsset(Dfareporting reporting, long profileId,
      long advertiserId, String assetName, String assetFile, String assetType) throws Exception {
    // Open the asset file.
    File file = new File(assetFile);

    // Prepare an input stream.
    String contentType = getMimeType(assetFile);
    InputStreamContent assetContent =
        new InputStreamContent(contentType, new BufferedInputStream(new FileInputStream(file)));
    assetContent.setLength(file.length());

    // Create the creative asset ID and Metadata.
    CreativeAssetId assetId = new CreativeAssetId();
    assetId.setName(assetName);
    assetId.setType(assetType);

    CreativeAssetMetadata metaData = new CreativeAssetMetadata();
    metaData.setAssetIdentifier(assetId);

    // Insert the creative.
    CreativeAssetMetadata result = reporting.creativeAssets()
        .insert(profileId, advertiserId, metaData, assetContent).execute();

    // Display the new asset name.
    System.out.printf("Creative asset was saved with name \"%s\".%n",
        result.getAssetIdentifier().getName());

    return result;
  }

  /**
   * Attempts to determine the MIME type of the specified file. If no type is detected, falls back
   * to <code>application/octet-stream</code>.
   *
   * @param fileName The name of the file to find a MIME type for.
   * @return The MIME type to use for uploading this file.
   * @see FileNameMap#getContentTypeFor(String)
   */
  private static String getMimeType(String fileName) {
    FileNameMap fileNameMap = URLConnection.getFileNameMap();
    String mimeType = fileNameMap.getContentTypeFor(fileName);

    if (mimeType != null) return mimeType;
    return "application/octet-stream";
  }
}
