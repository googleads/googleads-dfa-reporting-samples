// Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.api.services.samples.dfareporting.targeting;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Creative;
import com.google.api.services.dfareporting.model.CreativeAsset;
import com.google.api.services.dfareporting.model.CreativeAssetMetadata;
import com.google.api.services.dfareporting.model.CreativeAssetSelection;
import com.google.api.services.dfareporting.model.Rule;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.api.services.samples.dfareporting.creatives.assets.CreativeAssetUtils;

import java.util.ArrayList;

/**
 * This example uploads a new video asset to an existing in-stream video creative and configures
 * dynamic asset selection, using a specified targeting template. To get an in-stream video
 * creative, run CreateInstreamVideoCreative.java. To get a targeting template, run
 * CreateTargetingTemplate.java.
 */
public class ConfigureDynamicAssetSelection {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String INSTREAM_VIDEO_CREATIVE_ID = "INSERT_INSTREAM_VIDEO_ASSET_ID_HERE";
  private static final String TARGETING_TEMPLATE_ID = "INSERT_TARGETING_TEMPLATE_ID_HERE";

  // Video asset values
  private static final String VIDEO_ASSET_NAME = "INSERT_VIDEO_ASSET_NAME_HERE";
  private static final String PATH_TO_VIDEO_ASSET_FILE = "INSERT_PATH_TO_VIDEO_ASSET_FILE_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long creativeId,
      long templateId, String videoAssetName, String pathToVideoAsset) throws Exception {
    // Retrieve the specified creative.
    Creative creative = reporting.creatives().get(profileId, creativeId).execute();
    if(creative == null || !"INSTREAM_VIDEO".equals(creative.getType())) {
      System.out.println("Invalid creative specified.");
      return;
    }

    CreativeAssetSelection selection = creative.getCreativeAssetSelection();
    if(!creative.getDynamicAssetSelection()) {
      // Locate an existing video asset to use as a default.
      // This example uses the first PARENT_VIDEO asset found.
      long defaultVideoAssetId = findDefaultVideoAssetId(creative);
      if(defaultVideoAssetId < 0) {
        System.out.println("Default video asset could not be found.");
        return;
      }

      // Create a new selection using the existing asset as a default.
      selection = new CreativeAssetSelection();
      selection.setDefaultAssetId(defaultVideoAssetId);
      selection.setRules(new ArrayList<Rule>());

      // Enable dynamic asset selection for the creative.
      creative.setDynamicAssetSelection(true);
      creative.setCreativeAssetSelection(selection);
    }

    // Upload the new video asset and add it to the creative.
    CreativeAssetMetadata videoAssetMetadata = CreativeAssetUtils.uploadAsset(reporting, profileId,
        creative.getAdvertiserId(), videoAssetName, pathToVideoAsset, "VIDEO");
    creative.getCreativeAssets().add(new CreativeAsset()
        .setAssetIdentifier(videoAssetMetadata.getAssetIdentifier()).setRole("PARENT_VIDEO"));

    // Create a rule targeting the new video asset and add it to the selection.
    Rule rule = new Rule();
    rule.setAssetId(videoAssetMetadata.getId());
    rule.setName("Test rule for asset " + videoAssetMetadata.getId());
    rule.setTargetingTemplateId(templateId);
    selection.getRules().add(rule);

    // Update the creative.
    Creative result = reporting.creatives().update(profileId, creative).execute();
    System.out.printf("Dynamic asset selection enabled for creative with ID %d.%n",
        result.getId());
  }

  private static long findDefaultVideoAssetId(Creative creative) {
    for(CreativeAsset asset : creative.getCreativeAssets()) {
      if("PARENT_VIDEO".equals(asset.getRole())) {
        return asset.getId();
      }
    }

    return -1;
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long creativeId = Long.parseLong(INSTREAM_VIDEO_CREATIVE_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);
    long templateId = Long.parseLong(TARGETING_TEMPLATE_ID);

    runExample(reporting, profileId, creativeId, templateId, VIDEO_ASSET_NAME,
        PATH_TO_VIDEO_ASSET_FILE);
  }
}
