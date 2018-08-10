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
import com.google.api.services.dfareporting.model.File;
import com.google.api.services.dfareporting.model.FileList;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example illustrates how to get a list of all the files for a report.
 */
public class GetReportFiles {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String REPORT_ID = "ENTER_REPORT_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long reportId)
      throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,items(fileName,id,status)";

    FileList files;
    String nextPageToken = null;

    do {
      // Create and execute the report files list request.
      files = reporting.reports().files().list(profileId, reportId).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (File file : files.getItems()) {
        System.out.printf("Report file with ID %d and file name \"%s\" has status \"%s\".%n",
            file.getId(), file.getFileName(), file.getStatus());
      }

      // Update the next page token.
      nextPageToken = files.getNextPageToken();
    } while (!files.getItems().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long reportId = Long.parseLong(REPORT_ID);

    runExample(reporting, profileId, reportId);
  }
}
