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

package com.google.api.services.samples.dfareporting.creatives;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Creative;
import com.google.api.services.dfareporting.model.CreativeAsset;
import com.google.api.services.dfareporting.model.CreativeAssetId;
import com.google.api.services.dfareporting.model.Size;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.api.services.samples.dfareporting.creatives.assets.CreativeAssetUtils;
import com.google.common.collect.ImmutableList;

/**
 * This example uploads creative assets and creates an image display creative associated with a
 * given advertiser. To get a size ID, run GetSize.java.
 */
public class CreateImageDisplayCreative {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  // Creative values.
  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String SIZE_ID = "INSERT_SIZE_ID_HERE";

  // Image asset values.
  private static final String IMAGE_ASSET_NAME = "INSERT_IMAGE_ASSET_NAME_HERE";
  private static final String PATH_TO_IMAGE_ASSET_FILE = "INSERT_PATH_TO_IMAGE_ASSET_FILE_HERE";


  public static void runExample(Dfareporting reporting, long profileId, long advertiserId,
      long sizeId) throws Exception {
    Creative creative = new Creative();
    creative.setAdvertiserId(advertiserId);
    creative.setName("Test image display creative");
    creative.setSize(new Size().setId(sizeId));
    creative.setType("DISPLAY");

    // Upload the image asset.
    CreativeAssetId imageAssetId = CreativeAssetUtils.uploadAsset(reporting, profileId,
        advertiserId, IMAGE_ASSET_NAME, PATH_TO_IMAGE_ASSET_FILE, "HTML_IMAGE")
        .getAssetIdentifier();

    CreativeAsset asset = new CreativeAsset().setAssetIdentifier(imageAssetId).setRole("PRIMARY");

    // Add the creative asset.
    creative.setCreativeAssets(ImmutableList.of(asset));

    Creative result = reporting.creatives().insert(profileId, creative).execute();

    // Display the new creative ID.
    System.out.printf("Image display creative with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long sizeId = Long.parseLong(SIZE_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId, sizeId);
  }
}
