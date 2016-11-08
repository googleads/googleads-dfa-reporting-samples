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
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.api.services.samples.dfareporting.creatives.assets.CreativeAssetUtils;
import com.google.common.collect.ImmutableList;

/**
 * This example uploads creative assets and creates an in-stream video creative associated with a
 * given advertiser.
 */
public class CreateInstreamVideoCreative {
  private static final String USER_PROFILE_ID = "INSERT_PROFILE_ID_HERE";

  // Creative values.
  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";

  // Image asset values.
  private static final String VIDEO_ASSET_NAME = "INSERT_VIDEO_ASSET_NAME_HERE";
  private static final String PATH_TO_VIDEO_ASSET_FILE = "INSERT_PATH_TO_VIDEO_ASSET_FILE_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    Creative creative = new Creative();
    creative.setAdvertiserId(advertiserId);
    creative.setName("Test in-stream video creative");
    creative.setType("INSTREAM_VIDEO");

    // Upload the video asset
    CreativeAssetId videoAssetId = CreativeAssetUtils.uploadAsset(reporting, profileId,
        advertiserId, VIDEO_ASSET_NAME, PATH_TO_VIDEO_ASSET_FILE, "VIDEO").getAssetIdentifier();

    CreativeAsset videoAsset =
        new CreativeAsset().setAssetIdentifier(videoAssetId).setRole("PARENT_VIDEO");

    // Add the creative assets.
    creative.setCreativeAssets(ImmutableList.of(videoAsset));

    Creative result = reporting.creatives().insert(profileId, creative).execute();

    // Display the new creative ID.
    System.out.printf("In-stream video creative with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
