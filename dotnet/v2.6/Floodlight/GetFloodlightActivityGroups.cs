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
  /// This example displays floodlight activity groups for a given advertiser.
  /// To create an advertiser, run CreateAdvertiser.cs.
  /// </summary>
  class GetFloodlightActivityGroups : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example displays floodlight activity groups for a given" +
            " advertiser. To create an advertiser, run CreateAdvertiser.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetFloodlightActivityGroups();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long advertiserId = long.Parse(_T("INSERT_ADVERTISER_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));

      // Limit the fields returned.
      String fields = "floodlightActivityGroups(id,name)";

      // Create and execute the floodlight activity groups list request.
      FloodlightActivityGroupsResource.ListRequest request =
          service.FloodlightActivityGroups.List(profileId);
      request.AdvertiserId = advertiserId;
      request.Fields = fields;
      FloodlightActivityGroupsListResponse groups = request.Execute();

      foreach (FloodlightActivityGroup group in groups.FloodlightActivityGroups) {
        Console.WriteLine("Floodlight activity group with ID {0} and name" +
            " \"{1}\" was found.", group.Id, group.Name);
      }
    }
  }
}
