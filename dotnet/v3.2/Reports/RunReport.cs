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
using System.Threading;
using Google.Apis.Dfareporting.v3_2;
using Google.Apis.Dfareporting.v3_2.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example illustrates how to run a report.
  /// </summary>
  class RunReport : SampleBase {
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
        return "This example illustrates how to run a report.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new RunReport();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long reportId = long.Parse(_T("INSERT_REPORT_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Run the report.
      File file = service.Reports.Run(profileId, reportId).Execute();
      Console.WriteLine("File with ID {0} has been created.", file.Id);

      // Wait for the report file to finish processing.
      // An exponential backoff policy is used to limit retries and conserve quota.
      int sleep = 0;
      int startTime = GetCurrentTimeInSeconds();
      do {
        file = service.Files.Get(file.ReportId.Value, file.Id.Value).Execute();

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
