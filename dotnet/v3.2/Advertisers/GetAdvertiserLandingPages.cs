/*
 * Copyright 2017 Google Inc
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
using Google.Apis.Dfareporting.v3_2;
using Google.Apis.Dfareporting.v3_2.Data;
using System.Collections.Generic;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example lists all landing pages for a specified advertiser.
  /// </summary>
  class GetAdvertiserLandingPages : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example lists all landing pages for a specified advertiser.";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetAdvertiserLandingPages();
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
      String fields = "nextPageToken,landingPages(id,name,url)";

      AdvertiserLandingPagesListResponse result;
      String nextPageToken = null;

      do {
        // Create and execute the advertiser landing pages list request.
        AdvertiserLandingPagesResource.ListRequest request =
            service.AdvertiserLandingPages.List(profileId);
        request.AdvertiserIds = new List<string>() { advertiserId.ToString() };
        request.Fields = fields;
        request.PageToken = nextPageToken;
        result = request.Execute();

        foreach (LandingPage landingPage in result.LandingPages) {
          Console.WriteLine("Advertiser landing page with ID {0}, name \"{1}\", and " +
              "URL {2} was found.", landingPage.Id, landingPage.Name, landingPage.Url);
        }

        // Update the next page token.
        nextPageToken = result.NextPageToken;
      } while (result.LandingPages.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
