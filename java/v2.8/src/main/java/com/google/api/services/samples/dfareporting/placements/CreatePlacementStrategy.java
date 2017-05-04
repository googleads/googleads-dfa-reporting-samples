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
import com.google.api.services.dfareporting.model.PlacementStrategy;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a placement strategy with the given name.
 */
public class CreatePlacementStrategy {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String PLACEMENT_STRATEGY_NAME = "INSERT_PLACEMENT_STRATEGY_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId,
      String placementStrategyName) throws Exception {
    // Create the placement strategy.
    PlacementStrategy placementStrategy = new PlacementStrategy();
    placementStrategy.setName(placementStrategyName);

    // Insert the placement strategy.
    PlacementStrategy result =
        reporting.placementStrategies().insert(profileId, placementStrategy).execute();

    // Display the new placement strategy ID.
    System.out.printf("Placement strategy with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, PLACEMENT_STRATEGY_NAME);
  }
}
