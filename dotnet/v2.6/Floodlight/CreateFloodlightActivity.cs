/*
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
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a floodlight activity in a given activity group. To
  /// create an activity group, run CreateFloodlightActivityGroup.cs.
  /// </summary>
  class CreateFloodlightActivity : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a floodlight activity in a given" +
            " activity group. To create an activity group, run" +
            " CreateFloodlightActivityGroup.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateFloodlightActivity();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long activityGroupId = long.Parse(_T("INSERT_ACTIVITY_GROUP_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      String activityName = _T("INSERT_ACTIVITY_NAME_HERE");
      String url = _T("INSERT_EXPECTED_URL_HERE");

      // Set floodlight activity structure.
      FloodlightActivity activity = new FloodlightActivity();
      activity.CountingMethod = "STANDARD_COUNTING";
      activity.Name = activityName;
      activity.FloodlightActivityGroupId = activityGroupId;
      activity.ExpectedUrl = url;

      // Create the floodlight tag activity.
      FloodlightActivity result =
          service.FloodlightActivities.Insert(activity, profileId).Execute();

      // Display new floodlight activity ID.
      if (result != null) {
        Console.WriteLine("Floodlight activity with ID {0} was created.", result.Id);
      }
    }
  }
}
