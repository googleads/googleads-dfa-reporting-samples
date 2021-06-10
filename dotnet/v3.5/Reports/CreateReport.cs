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
using System.Linq;
using Google.Apis.Dfareporting.v3_5;
using Google.Apis.Dfareporting.v3_5.Data;
using Newtonsoft.Json;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example provides an end-to-end example of how to create and
  /// configure a standard report.
  /// </summary>
  class CreateReport : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example provides an end-to-end example of how to create " +
               "and configure a standard report.\n";
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

      // 1. Create a report resource.
      Report report = CreateReportResource();

      // 2. Define the report criteria.
      DefineReportCriteria(report);

      // 3. (optional) Look up compatible fields.
      FindCompatibleFields(service, profileId, report);

      // 4. Add dimension filters to the report criteria.
      AddDimensionFilters(service, profileId, report);

      // 5. Save the report resource.
      InsertReportResource(service, profileId, report);
    }

    private Report CreateReportResource() {
      Report report = new Report();

      // Set the required fields "name" and "type".
      report.Name = "Example standard report";
      report.Type = "STANDARD";

      // Set optional fields.
      report.FileName = "example_report";
      report.Format = "CSV";

      Console.WriteLine("Creating {0} report resource with name \"{1}\".",
          report.Type, report.Name);

      return report;
    }

    private void DefineReportCriteria(Report report) {
      // Define a date range to report on. This example uses explicit start and
      // end dates to mimic the "LAST_30_DAYS" relative date range.
      DateRange dateRange = new DateRange();
      dateRange.EndDate = DateTime.Now.ToString("yyyy-MM-dd");
      dateRange.StartDate = DateTime.Now.AddDays(-30).ToString("yyyy-MM-dd");

      // Create a report criteria.
      SortedDimension dimension = new SortedDimension();
      dimension.Name = "dfa:advertiser";

      Report.CriteriaData criteria = new Report.CriteriaData();
      criteria.DateRange = dateRange;
      criteria.Dimensions = new List<SortedDimension>() { dimension };
      criteria.MetricNames = new List<string>() {
        "dfa:clicks",
        "dfa:impressions"
      };

      // Add the criteria to the report resource.
      report.Criteria = criteria;

      Console.WriteLine("\nAdded report criteria:\n{0}",
          JsonConvert.SerializeObject(criteria));
    }

    private void FindCompatibleFields(DfareportingService service,
        long profileId, Report report) {
        CompatibleFields fields =
            service.Reports.CompatibleFields.Query(report, profileId).Execute();

        ReportCompatibleFields reportFields = fields.ReportCompatibleFields;

        if(reportFields.Dimensions.Any()) {
          // Add a compatible dimension to the report.
          Dimension dimension = reportFields.Dimensions[0];
          SortedDimension sortedDimension = new SortedDimension();
          sortedDimension.Name = dimension.Name;
          report.Criteria.Dimensions.Add(sortedDimension);
        } else if (reportFields.Metrics.Any()) {
          // Add a compatible metric to the report.
          Metric metric = reportFields.Metrics[0];
          report.Criteria.MetricNames.Add(metric.Name);
        }

        Console.WriteLine(
            "\nUpdated report criteria (with compatible fields):\n{0}",
            JsonConvert.SerializeObject(report.Criteria));
    }

    private void AddDimensionFilters(DfareportingService service,
        long profileId, Report report) {
      // Query advertiser dimension values for report run dates.
      DimensionValueRequest request = new DimensionValueRequest();
      request.StartDate = report.Criteria.DateRange.StartDate;
      request.EndDate = report.Criteria.DateRange.EndDate;
      request.DimensionName = "dfa:advertiser";

      DimensionValueList values =
          service.DimensionValues.Query(request, profileId).Execute();

      if (values.Items.Any()) {
        // Add a value as a filter to the report criteria.
        report.Criteria.DimensionFilters = new List<DimensionValue>() {
          values.Items[0]
        };
      }

      Console.WriteLine(
        "\nUpdated report criteria (with valid dimension filters):\n{0}",
        JsonConvert.SerializeObject(report.Criteria));
    }

    private void InsertReportResource(DfareportingService service,
        long profileId, Report report) {
      Report insertedReport =
          service.Reports.Insert(report, profileId).Execute();

      Console.WriteLine("\nSuccessfully inserted new report with ID {0}.",
          insertedReport.Id);
    }
  }
}
