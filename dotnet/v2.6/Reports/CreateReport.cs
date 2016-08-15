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
using System.Collections.Generic;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example illustrates how to create a report.
  /// </summary>
  class CreateReport : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example illustrates how to create a report.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateReport();
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

      string reportName = _T("INSERT_REPORT_NAME_HERE");

      // Create a date range to report on.
      DateRange dateRange = new DateRange();
      dateRange.RelativeDateRange = "YESTERDAY";

      // Create a dimension to report on.
      SortedDimension dimension = new SortedDimension();
      dimension.Name = "dfa:campaign";

      // Create the criteria for the report.
      Report.CriteriaData criteria = new Report.CriteriaData();
      criteria.DateRange = dateRange;
      criteria.Dimensions = new List<SortedDimension>() { dimension };
      criteria.MetricNames = new List<string>() { "dfa:clicks" };

      // Create the report.
      Report report = new Report();
      report.Criteria = criteria;
      report.Name = reportName;
      report.Type = "STANDARD";

      // Insert the report.
      Report result = service.Reports.Insert(report, profileId).Execute();

      // Display the new report ID.
      Console.WriteLine("Standard report with ID {0} was created.", result.Id);
    }
  }
}
