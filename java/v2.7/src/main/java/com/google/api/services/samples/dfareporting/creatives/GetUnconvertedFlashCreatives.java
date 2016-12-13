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

package com.google.api.services.samples.dfareporting.creatives;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Creative;
import com.google.api.services.dfareporting.model.CreativeAsset;
import com.google.api.services.dfareporting.model.CreativesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * This example lists all active creatives in a given account which:
 * <ol>
 * <li>Have a primary Flash asset.</li>
 * <li>Lack a converted HTML5 asset.</li>
 * </ol>
 * Creatives in this state will be deactivated on January 2, 2017. See the
 * <a href="https://support.google.com/dcm/answer/6353522">DCM help center</a> for more
 * information.
 */
public class GetUnconvertedFlashCreatives {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  // A list of the creative types that support primary Flash assets.
  private static final List<String> affectedCreativeTypes = ImmutableList.of(
      "CUSTOM_DISPLAY", 
      "CUSTOM_DISPLAY_INTERSTITIAL",
      "DISPLAY",
      "FLASH_INPAGE",
      "RICH_MEDIA_DISPLAY_BANNER",
      "RICH_MEDIA_DISPLAY_EXPANDING",
      "RICH_MEDIA_DISPLAY_INTERSTITIAL",
      "RICH_MEDIA_DISPLAY_MULTI_FLOATING_INTERSTITIAL",
      "RICH_MEDIA_IM_EXPAND",
      "RICH_MEDIA_INPAGE_FLOATING",
      "RICH_MEDIA_MOBILE_IN_APP",
      "RICH_MEDIA_PEEL_DOWN");

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields ="nextPageToken," +
        "creatives(artworkType,creativeAssets(assetIdentifier/type,role),id,name,type)";

    CreativesListResponse creatives;
    String nextPageToken = null;

    do {
      // Only retrieve active creatives with an affected type.
      creatives = reporting.creatives().list(profileId).setActive(true).setFields(fields)
          .setTypes(affectedCreativeTypes).setPageToken(nextPageToken).execute();

      for (Creative creative : creatives.getCreatives()) {
        if (isUnconvertedFlashCreative(creative)) {
          System.out.printf("Unconverted Flash creative found: %s (ID: %d, type: %s)%n",
              creative.getName(), creative.getId(), creative.getType());
        }
      }

      nextPageToken = creatives.getNextPageToken();
    } while (!creatives.getCreatives().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  private static boolean isUnconvertedFlashCreative(Creative creative) {
    if ("ARTWORK_TYPE_FLASH".equals(creative.getArtworkType())) {
      return true;
    } else if (creative.getCreativeAssets() != null) {
      boolean hasPrimaryFlash = false;
      boolean hasPrimaryHtml5 = false;
      boolean hasOtherHtml5 = false;

      for (CreativeAsset asset : creative.getCreativeAssets()) {
        if ("PRIMARY".equals(asset.getRole())) {
          hasPrimaryFlash |= "FLASH".equals(asset.getAssetIdentifier().getType());
          hasPrimaryHtml5 |= "HTML".equals(asset.getAssetIdentifier().getType());
        } else {
          hasOtherHtml5 |= "HTML".equals(asset.getAssetIdentifier().getType());
        }
      }

      if(creative.getType().startsWith("CUSTOM")) {
        return hasPrimaryFlash;
      } else if("FLASH_INPAGE".equals(creative.getType())) {
        return hasPrimaryFlash && !hasPrimaryHtml5 && !hasOtherHtml5;
      } else {
        return hasPrimaryFlash && !hasPrimaryHtml5;
      }
    }

    return false;
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
