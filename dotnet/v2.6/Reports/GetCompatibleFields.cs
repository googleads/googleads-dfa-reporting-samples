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
  /// This example illustrates how to get the compatible fields for a standard
  /// report.
  /// </summary>
  class GetCompatibleFields : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example illustrates how to get the compatible fields" +
            " for a standard report.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetCompatibleFields();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long reportId = long.Parse(_T("ENTER_REPORT_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Retrieve the specified report.
      Report report = service.Reports.Get(profileId, reportId).Execute();

      // Look up the compatible fields for the report.
      CompatibleFields fields =
          service.Reports.CompatibleFields.Query(report, profileId).Execute();

      // Display the compatible fields, assuming this is a standard report.
      ReportCompatibleFields reportFields = fields.ReportCompatibleFields;

      Console.WriteLine("Compatible dimensions:\n");
      printDimensionNames(reportFields.Dimensions);

      Console.WriteLine("\nCompatible metrics:\n");
      printMetricNames(reportFields.Metrics);

      Console.WriteLine("\nCompatible dimension filters:\n");
      printDimensionNames(reportFields.DimensionFilters);

      Console.WriteLine("\nCompatible pivoted activity metrics:\n");
      printMetricNames(reportFields.PivotedActivityMetrics);
    }

    private static void printDimensionNames(IList<Dimension> dimensions) {
      foreach (Dimension dimension in dimensions) {
        Console.WriteLine(dimension.Name);
      }
    }

    private static void printMetricNames(IList<Metric> metrics) {
      foreach (Metric metric in metrics) {
        Console.WriteLine(metric.Name);
      }
    }
  }
}
