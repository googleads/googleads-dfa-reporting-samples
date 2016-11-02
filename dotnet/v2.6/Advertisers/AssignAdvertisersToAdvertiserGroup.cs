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
  /// This example assigns an advertiser to an advertiser group.
  ///
  /// CAUTION: An advertiser that has campaigns associated with it cannot be removed from an
  /// advertiser group once assigned.
  /// </summary>
  class AssignAdvertisersToAdvertiserGroup : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example assigns an advertiser to an advertiser group." +
            "\n\nCAUTION: An advertiser that has campaigns associated with it" +
            " cannot be removed from an advertiser group once assigned.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new AssignAdvertisersToAdvertiserGroup();
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
      long advertiserGroupId = long.Parse(_T("INSERT_ADVERTISER_GROUP_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Create the Advertiser and populate with the group ID to patch.
      Advertiser advertiser = new Advertiser();
      advertiser.AdvertiserGroupId = advertiserGroupId;

      // Patch the existing advertiser.
      Advertiser result =
        service.Advertisers.Patch(advertiser, profileId, advertiserId).Execute();

      // Print out the advertiser and advertiser group ID.
      Console.WriteLine("Advertiser with ID {0} was assigned to advertiser" +
          " group \"{1}\".", result.Id, result.AdvertiserGroupId);
    }
  }
}
