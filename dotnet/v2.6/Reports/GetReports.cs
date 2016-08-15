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
  /// This example illustrates how to get a list of all reports.
  /// </summary>
  class GetReports : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example illustrates how to get a list of all reports.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetReports();
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

      // Limit the fields returned.
      String fields = "nextPageToken,items(id,name,type)";

      ReportList reports;
      String nextPageToken = null;

      do {
        // Create and execute the report list request.
        ReportsResource.ListRequest request = service.Reports.List(profileId);
        request.Fields = fields;
        request.PageToken = nextPageToken;
        reports = request.Execute();

        foreach (Report report in reports.Items) {
          Console.WriteLine("{0} report with ID {1} and name \"{2}\" was found.",
              report.Type, report.Id, report.Name);
        }

        // Update the next page token.
        nextPageToken = reports.NextPageToken;
      } while (reports.Items.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
