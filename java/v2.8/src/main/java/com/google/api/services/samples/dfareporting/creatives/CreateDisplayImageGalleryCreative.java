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
import com.google.api.services.dfareporting.model.ClickTag;
import com.google.api.services.dfareporting.model.Creative;
import com.google.api.services.dfareporting.model.CreativeAsset;
import com.google.api.services.dfareporting.model.CreativeAssetId;
import com.google.api.services.dfareporting.model.Size;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.api.services.samples.dfareporting.creatives.assets.CreativeAssetUtils;
import com.google.common.collect.ImmutableList;

/**
 * This example uploads creative assets and creates a display image gallery creative associated
 * with a given advertiser. To get a size ID, run GetSize.java.
 */
public class CreateDisplayImageGalleryCreative {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  // Creative values.
  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";
  private static final String SIZE_ID = "INSERT_SIZE_ID_HERE";

  // First image asset values.
  private static final String IMAGE_ASSET1_NAME = "INSERT_IMAGE_ASSET_NAME_HERE";
  private static final String PATH_TO_IMAGE_ASSET1_FILE = "INSERT_PATH_TO_IMAGE_ASSET_FILE_HERE";

  // Second image asset values.
  private static final String IMAGE_ASSET2_NAME = "INSERT_IMAGE_ASSET_NAME_HERE";
  private static final String PATH_TO_IMAGE_ASSET2_FILE = "INSERT_PATH_TO_IMAGE_ASSET_FILE_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId,
      long sizeId) throws Exception {
    Creative creative = new Creative();
    creative.setAdvertiserId(advertiserId);
    creative.setAutoAdvanceImages(true);
    creative.setName("Test display image gallery creative");
    creative.setSize(new Size().setId(sizeId));
    creative.setType("DISPLAY_IMAGE_GALLERY");

    // Upload the first image asset.
    CreativeAssetId imageAsset1Id = CreativeAssetUtils.uploadAsset(reporting, profileId,
        advertiserId, IMAGE_ASSET1_NAME, PATH_TO_IMAGE_ASSET1_FILE, "HTML_IMAGE")
        .getAssetIdentifier();

    CreativeAsset imageAsset1 =
        new CreativeAsset().setAssetIdentifier(imageAsset1Id).setRole("PRIMARY");

    // Upload the second image asset.
    CreativeAssetId imageAsset2Id = CreativeAssetUtils.uploadAsset(reporting, profileId,
        advertiserId, IMAGE_ASSET2_NAME, PATH_TO_IMAGE_ASSET2_FILE, "HTML_IMAGE")
        .getAssetIdentifier();

    CreativeAsset imageAsset2 =
        new CreativeAsset().setAssetIdentifier(imageAsset2Id).setRole("PRIMARY");

    // Add the creative assets.
    creative.setCreativeAssets(ImmutableList.of(imageAsset1, imageAsset2));

    // Create a click tag for the first image asset.
    ClickTag clickTag1 =
        new ClickTag().setName(imageAsset1Id.getName()).setEventName(imageAsset1Id.getName());

    // Create a click tag for the second image asset.
    ClickTag clickTag2 =
        new ClickTag().setName(imageAsset2Id.getName()).setEventName(imageAsset2Id.getName());

    // Add the click tags.
    creative.setClickTags(ImmutableList.of(clickTag1, clickTag2));

    Creative result = reporting.creatives().insert(profileId, creative).execute();

    // Display the new creative ID.
    System.out.printf("Display image gallery creative with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long sizeId = Long.parseLong(SIZE_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId, sizeId);
  }
}
