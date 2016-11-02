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
  /// This example creates a creative group associated with a given advertiser.
  /// To get an advertiser ID, run getAdvertisers.cs. Valid group numbers are
  /// limited to 1 or 2.
  /// </summary>
  class CreateCreativeGroup : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a creative group associated with a given" +
            " advertiser. To get an advertiser ID, run getAdvertisers.cs. Valid" +
            " group numbers are limited to 1 or 2.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateCreativeGroup();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      int groupNumber = int.Parse(_T("INSERT_GROUP_NUMBER_HERE"));
      long advertiserId = long.Parse(_T("INSERT_ADVERTISER_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string creativeGroupName = _T("INSERT_CREATIVE_GROUP_NAME_HERE");

      // Create the creative group.
      CreativeGroup creativeGroup = new CreativeGroup();
      creativeGroup.Name = creativeGroupName;
      creativeGroup.GroupNumber = groupNumber;
      creativeGroup.AdvertiserId = advertiserId;

      // Insert the creative group.
      CreativeGroup result =
          service.CreativeGroups.Insert(creativeGroup, profileId).Execute();

      // Display the new creative group ID.
      Console.WriteLine("Creative group with ID {0} was created.", result.Id);
    }
  }
}
