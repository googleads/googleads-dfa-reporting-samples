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

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.Report;
import com.google.api.services.dfareporting.model.ReportList;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example illustrates how to get a list of all reports.
 */
public class GetReports {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,items(id,name,type)";

    ReportList reports;
    String nextPageToken = null;

    do {
      // Create and execute the report list request.
      reports = reporting.reports().list(profileId).setFields(fields).setPageToken(nextPageToken)
          .execute();

      for (Report report : reports.getItems()) {
        System.out.printf("%s report with ID %d and name \"%s\" was found.%n", report.getType(),
            report.getId(), report.getName());
      }

      // Update the next page token.
      nextPageToken = reports.getNextPageToken();
    } while (!reports.getItems().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
