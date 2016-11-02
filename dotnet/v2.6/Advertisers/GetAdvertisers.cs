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
  /// This example displays the name, ID and spotlight configuration ID for
  /// every advertiser your DCM user profile can see.
  /// </summary>
  class GetAdvertisers : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example displays the name, ID and spotlight" +
            " configuration ID for every advertiser your DCM user profile can" +
            " see.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetAdvertisers();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));

      // Limit the fields returned.
      String fields = "nextPageToken,advertisers(id,floodlightConfigurationId,name)";

      AdvertisersListResponse result;
      String nextPageToken = null;

      do {
        // Create and execute the advertiser list request.
        AdvertisersResource.ListRequest request = service.Advertisers.List(profileId);
        request.Fields = fields;
        request.PageToken = nextPageToken;
        result = request.Execute();

        foreach (Advertiser advertiser in result.Advertisers) {
          Console.WriteLine("Advertiser with ID {0}, name \"{1}\", and" +
              " floodlight configuration ID {2} was found.", advertiser.Id,
              advertiser.Name, advertiser.FloodlightConfigurationId);
        }

        // Update the next page token.
        nextPageToken = result.NextPageToken;
      } while (result.Advertisers.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
