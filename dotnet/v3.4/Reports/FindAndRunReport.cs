/*
 * Copyright 2018 Google Inc
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
using System.Threading;
using Google.Apis.Dfareporting.v3_4;
using Google.Apis.Dfareporting.v3_4.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example provides an end-to-end example of how to find and run a
  /// report.
  /// </summary>
  class FindAndRunReport : SampleBase {
    // The following values control retry behavior while the report is processing.
    // Minimum amount of time between polling requests. Defaults to 10 seconds.
    private static int MIN_RETRY_INTERVAL = 10;
    // Maximum amount of time between polling requests. Defaults to 10 minutes.
    private static int MAX_RETRY_INTERVAL = 10 * 60;
    // Maximum amount of time to spend polling. Defaults to 1 hour.
    private static int MAX_RETRY_ELAPSED_TIME = 60 * 60;

    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example provides an end-to-end example of how to find " +
               "and run a report.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new FindAndRunReport();
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

      // 1. Find a report to run.
      Report report = FindReport(service, profileId);

      if (report != null) {
        // 2. Run the report.
        File file = RunReport(service, profileId, report.Id.Value);

        // 3. Wait for the report file to be ready.
        WaitForReportFile(service, report.Id.Value, file.Id.Value);
      } else {
        Console.WriteLine("No report found for profile ID {0}.", profileId);
      }
    }

    private Report FindReport(DfareportingService service, long profileId) {
      Report target = null;
      ReportList reports;
      String nextPageToken = null;

      do {
        // Create and execute the reports list request.
        ReportsResource.ListRequest request = service.Reports.List(profileId);
        request.PageToken = nextPageToken;
        reports = request.Execute();

        foreach (Report report in reports.Items) {
          if (IsTargetReport(report)) {
            target = report;
            break;
          }
        }

        // Update the next page token.
        nextPageToken = reports.NextPageToken;
      } while (target == null
          && reports.Items.Any()
          && !String.IsNullOrEmpty(nextPageToken));

      if (target != null) {
        Console.WriteLine("Found report {0} with name \"{1}\".",
            target.Id, target.Name);
        return target;
      }

      Console.WriteLine("Unable to find report for profile ID {0}.", profileId);
      return null;
    }

    private bool IsTargetReport(Report report) {
      // Provide custom validation logic here.
      // For example purposes, any report is considered valid.
      return true;
    }

    private File RunReport(DfareportingService service, long profileId,
        long reportId) {
      // Run the report.
      File file = service.Reports.Run(profileId, reportId).Execute();

      Console.WriteLine("Running report {0}, current file status is {1}.",
          reportId, file.Status);
      return file;
    }

    private void WaitForReportFile(DfareportingService service, long reportId,
        long fileId) {
      // Wait for the report file to finish processing.
      // An exponential backoff policy is used to limit retries and conserve quota.
      int sleep = 0;
      int startTime = GetCurrentTimeInSeconds();
      do {
        File file = service.Files.Get(reportId, fileId).Execute();

        if ("REPORT_AVAILABLE".Equals(file.Status)) {
          Console.WriteLine("File status is {0}, ready to download.", file.Status);
          return;
        } else if (!"PROCESSING".Equals(file.Status)) {
          Console.WriteLine("File status is {0}, processing failed.", file.Status);
          return;
        } else if (GetCurrentTimeInSeconds() - startTime > MAX_RETRY_ELAPSED_TIME) {
          Console.WriteLine("File processing deadline exceeded.");
          return;
        }

        sleep = GetNextSleepInterval(sleep);
        Console.WriteLine("File status is {0}, sleeping for {1} seconds.", file.Status, sleep);
        Thread.Sleep(sleep * 1000);
      } while (true);
    }

    private int GetCurrentTimeInSeconds() {
      return (int) (DateTime.Now.Ticks / TimeSpan.TicksPerSecond);
    }

    private int GetNextSleepInterval(int previousSleepInterval) {
      int minInterval = Math.Max(MIN_RETRY_INTERVAL, previousSleepInterval);
      int maxInterval = Math.Max(MIN_RETRY_INTERVAL, previousSleepInterval * 3);
      return Math.Min(MAX_RETRY_INTERVAL, new Random().Next(minInterval, maxInterval));
    }
  }
}
