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
  /// This example creates an advertiser group. To get advertiser groups, see
  /// GetAdvertiserGroups.cs.
  /// </summary>
  class CreateAdvertiserGroup : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates an advertiser group. To get advertiser" +
            " groups, see GetAdvertiserGroups.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateAdvertiserGroup();
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

      String advertiserGroupName = _T("INSERT_ADVERTISER_GROUP_NAME_HERE");

      // Create advertiser group structure.
      AdvertiserGroup advertiserGroup = new AdvertiserGroup();
      advertiserGroup.Name = advertiserGroupName;

      // Create advertiser group.
      AdvertiserGroup result =
          service.AdvertiserGroups.Insert(advertiserGroup, profileId).Execute();

      // Display advertiser group ID.
      Console.WriteLine("Advertiser Group with ID {0} was created.", result.Id);
    }
  }
}
