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
  /// This example lists all creative groups.
  /// </summary>
  class GetCreativeGroups : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example lists all creative groups.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetCreativeGroups();
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

      CreativeGroupsListResponse groups;
      String nextPageToken = null;

      do {
        // Create and execute the creative field values list request.
        CreativeGroupsResource.ListRequest request = service.CreativeGroups.List(profileId);
        request.PageToken = nextPageToken;
        groups = request.Execute();

        foreach (CreativeGroup group in groups.CreativeGroups) {
          Console.WriteLine("Found creative group with ID {0} and value \"{1}\".",
              group.Id, group.Name);
        }

        // Update the next page token.
        nextPageToken = groups.NextPageToken;
      } while (groups.CreativeGroups.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
