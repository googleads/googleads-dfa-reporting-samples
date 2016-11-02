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

package com.google.api.services.samples.dfareporting.placements;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.Dfareporting.Placements.Generatetags;
import com.google.api.services.dfareporting.model.PlacementTag;
import com.google.api.services.dfareporting.model.PlacementsGenerateTagsResponse;
import com.google.api.services.dfareporting.model.TagData;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * This example downloads HTML Tags for a given campaign and placement ID. To create campaigns, run
 * CreateCampaign.java. To create placements, run CreatePlacement.java.
 */
public class DownloadPlacementTags {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String CAMPAIGN_ID = "ENTER_CAMPAIGN_ID_HERE";
  private static final String PLACEMENT_ID = "ENTER_PLACEMENT_ID_HERE";

  // The tag formats to generate
  private static final List<String> TAG_FORMATS = ImmutableList.of("PLACEMENT_TAG_STANDARD",
      "PLACEMENT_TAG_IFRAME_JAVASCRIPT", "PLACEMENT_TAG_INTERNAL_REDIRECT");

  public static void runExample(Dfareporting reporting, long profileId, long campaignId,
      long placementId, List<String> tagFormats) throws Exception {
    // Generate the placement activity tags.
    Generatetags request = reporting.placements().generatetags(profileId);
    request.setCampaignId(campaignId);
    request.setTagFormats(tagFormats);
    request.setPlacementIds(ImmutableList.of(placementId));

    PlacementsGenerateTagsResponse response = request.execute();

    // Display the placement activity tags.
    for (PlacementTag tag : response.getPlacementTags()) {
      for (TagData tagData : tag.getTagDatas()) {
        System.out.printf("%s:%n", tagData.getFormat());

        if (tagData.getImpressionTag() != null) {
          System.out.println(tagData.getImpressionTag());
        }

        if (tagData.getClickTag() != null) {
          System.out.println(tagData.getClickTag());
        }

        System.out.printf("%n");
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long campaignId = Long.parseLong(CAMPAIGN_ID);
    long placementId = Long.parseLong(PLACEMENT_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, campaignId, placementId, TAG_FORMATS);
  }
}
