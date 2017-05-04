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

package com.google.api.services.samples.dfareporting.misc;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Size;
import com.google.api.services.dfareporting.model.SizesListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example displays all sizes for a given width and height.
 */
public class GetSize {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String HEIGHT = "ENTER_HEIGHT_HERE";
  private static final String WIDTH = "ENTER_WIDTH_HERE";

  public static void runExample(Dfareporting reporting, long profileId, int height, int width)
      throws Exception {
    // Create and execute the sizes list request.
    SizesListResponse sizes =
        reporting.sizes().list(profileId).setHeight(height).setWidth(width).execute();

    for (Size size : sizes.getSizes()) {
      System.out.printf("Found size with ID %d.%n", size.getId());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    int height = Integer.parseInt(HEIGHT);
    int width = Integer.parseInt(WIDTH);

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, height, width);
  }
}
