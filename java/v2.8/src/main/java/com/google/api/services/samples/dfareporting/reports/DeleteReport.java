// Copyright 2014 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.services.samples.dfareporting.reports;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example illustrates how to delete a report.
 */
public class DeleteReport {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String REPORT_ID = "INSERT_REPORT_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long reportId)
      throws Exception {
    // Delete the report.
    reporting.reports().delete(profileId, reportId).execute();

    // Display the new report ID.
    System.out.printf("Report with ID %d was successfully deleted.%n", reportId);
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long reportId = Long.parseLong(REPORT_ID);

    runExample(reporting, profileId, reportId);
  }
}
