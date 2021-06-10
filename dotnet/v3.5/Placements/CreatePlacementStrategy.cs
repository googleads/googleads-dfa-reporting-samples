﻿/*
 * Copyright 2015 Google Inc
 *
 * Licensed under the Apache License, Version 2.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

using System;
using Google.Apis.Dfareporting.v3_5;
using Google.Apis.Dfareporting.v3_5.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a placement strategy with the given name.
  /// </summary>
  class CreatePlacementStrategy : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a placement strategy with the given name.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreatePlacementStrategy();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string placementStrategyName = _T("INSERT_PLACEMENT_STRATEGY_NAME_HERE");

      // Create the placement strategy.
      PlacementStrategy placementStrategy = new PlacementStrategy();
      placementStrategy.Name = placementStrategyName;

      // Insert the placement strategy.
      PlacementStrategy result =
          service.PlacementStrategies.Insert(placementStrategy, profileId).Execute();

      // Display the new placement strategy ID.
      Console.WriteLine("Placement strategy with ID {0} was created.", result.Id);
    }
  }
}
