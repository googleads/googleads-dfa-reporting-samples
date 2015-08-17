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

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Charsets;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This example illustrates how to download a file.
 */
public class DownloadFile {
  private static final String FILE_ID = "ENTER_FILE_ID_HERE";
  private static final String REPORT_ID = "ENTER_REPORT_ID_HERE";

  public static void runExample(Dfareporting reporting, long reportId, long fileId)
      throws Exception {
    // Download the file.
    HttpResponse fileContents = reporting.files().get(reportId, fileId).executeMedia();

    // Display the file contents.
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(fileContents.getContent(), Charsets.UTF_8));

      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } finally {
      fileContents.disconnect();
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long fileId = Long.parseLong(FILE_ID);
    long reportId = Long.parseLong(REPORT_ID);

    runExample(reporting, reportId, fileId);
  }
}
