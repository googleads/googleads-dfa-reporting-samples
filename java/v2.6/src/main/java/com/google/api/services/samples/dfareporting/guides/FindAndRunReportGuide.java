/*
 * Copyright (c) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.dfareporting.guides;

import com.google.api.client.util.BackOff;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.File;
import com.google.api.services.dfareporting.model.Report;
import com.google.api.services.dfareporting.model.ReportList;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.base.Strings;

import java.io.IOException;

/**
 * This example provides an end-to-end example of how to find and run a report.
 */
public class FindAndRunReportGuide {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    // 1. Find a report to run
    Report report = findReport(reporting, profileId);

    if (report != null) {
      // 2. Run the report
      File file = runReport(reporting, profileId, report.getId());

      // 3. Wait for the report file to be ready
      file = waitForReportFile(reporting, profileId, report.getId(), file.getId());
    } else {
      System.out.printf("No report found for profile ID %d.%n", profileId);
    }
  }

  private static Report findReport(Dfareporting reporting, long profileId)
      throws IOException {
    // [START find_report]
    Report target = null;
    ReportList reports;
    String nextPageToken = null;

    do {
      // Create and execute the reports list request.
      reports = reporting.reports().list(profileId).setPageToken(nextPageToken).execute();

      for (Report report : reports.getItems()) {
        if (isTargetReport(report)) {
          target = report;
          break;
        }
      }

      // Update the next page token.
      nextPageToken = reports.getNextPageToken();
    } while (!reports.getItems().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
    // [END find_report]

    if (target != null) {
      System.out.printf("Found report %d with name \"%s\".%n", target.getId(), target.getName());
      return target;
    }

    System.out.printf("Unable to find report for profile ID %d.%n", profileId);
    return null;
  }

  private static boolean isTargetReport(Report report) {
    // Provide custom validation logic here.
    // For example purposes, any report is considered valid.
    return true;
  }

  private static File runReport(Dfareporting reporting, long profileId, long reportId)
      throws IOException {
    // [START run_report]
    // Run the report.
    File file = reporting.reports().run(profileId, reportId).execute();
    // [END run_report]

    System.out.printf("Running report %d, current file status is %s.%n", reportId,
        file.getStatus());
    return file;
  }

  private static File waitForReportFile(Dfareporting reporting, long profileId, long reportId,
      long fileId) throws IOException, InterruptedException {
    // [START wait_for_report]
    BackOff backOff = new ExponentialBackOff.Builder()
        .setInitialIntervalMillis(10 * 1000)     // 10 second initial retry
        .setMaxIntervalMillis(10 * 60 * 1000)    // 10 minute maximum retry
        .setMaxElapsedTimeMillis(60 * 60 * 1000) // 1 hour total retry
        .build();

    while (true) {
      File file = reporting.files().get(reportId, fileId).execute();

      // Check to see if the report has finished processing
      if ("REPORT_AVAILABLE".equals(file.getStatus())) {
        System.out.printf("File status is %s, processing finished.%n", file.getStatus());
        return file;
      }

      // If the file isn't available yet, wait before checking again.
      long retryInterval = backOff.nextBackOffMillis();
      if (retryInterval == BackOff.STOP) {
        System.out.println("File processing deadline exceeded.%n");
        return null;
      }

      System.out.printf("File status is %s, sleeping for %dms.%n", file.getStatus(), retryInterval);
      Thread.sleep(retryInterval);
    }
    // [END wait_for_report]
  }
}

