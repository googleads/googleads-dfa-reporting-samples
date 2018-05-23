// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
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
 * This example uploads creative assets and creates an in-stream audio creative associated with a
 * given advertiser.
 */
public class CreateInstreamAudioCreative {
  private static final String USER_PROFILE_ID = "INSERT_PROFILE_ID_HERE";

  // Creative values.
  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";

  // Image asset values.
  private static final String AUDIO_ASSET_NAME = "INSERT_AUDIO_ASSET_NAME_HERE";
  private static final String PATH_TO_AUDIO_ASSET_FILE = "INSERT_PATH_TO_AUDIO_ASSET_FILE_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    Creative creative = new Creative();
    creative.setAdvertiserId(advertiserId);
    creative.setName("Test in-stream audio creative");
    creative.setType("INSTREAM_AUDIO");

    // Upload the audio asset
    CreativeAssetId audioAssetId = CreativeAssetUtils.uploadAsset(reporting, profileId,
        advertiserId, AUDIO_ASSET_NAME, PATH_TO_AUDIO_ASSET_FILE, "AUDIO").getAssetIdentifier();

    CreativeAsset audioAsset =
        new CreativeAsset().setAssetIdentifier(audioAssetId).setRole("PARENT_AUDIO");

    // Add the creative assets.
    creative.setCreativeAssets(ImmutableList.of(audioAsset));

    Creative result = reporting.creatives().insert(profileId, creative).execute();

    // Display the new creative ID.
    System.out.printf("In-stream audio creative with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
