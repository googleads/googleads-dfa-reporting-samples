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
using System.Linq;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example lists all existing active creatives for a given advertiser.
  /// To get an advertiser ID, run GetAdvertisers.cs.
  /// </summary>
  class GetCreatives : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example lists all existing active creatives for a given" +
            " advertiser. To get an advertiser ID, run GetAdvertisers.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetCreatives();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long advertiserId = long.Parse(_T("ENTER_ADVERTISER_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));

      // Limit the fields returned.
      String fields = "nextPageToken,creatives(id,name,type)";

      CreativesListResponse creatives;
      String nextPageToken = null;

      do {
        // Create and execute the campaigns list request.
        CreativesResource.ListRequest request = service.Creatives.List(profileId);
        request.Active = true;
        request.AdvertiserId = advertiserId;
        request.Fields = fields;
        request.PageToken = nextPageToken;
        creatives = request.Execute();

        foreach (Creative creative in creatives.Creatives) {
          Console.WriteLine("Found {0} creative with ID {1} and name \"{2}\".",
              creative.Type, creative.Id, creative.Name);
        }

        // Update the next page token.
        nextPageToken = creatives.NextPageToken;
      } while (creatives.Creatives.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
