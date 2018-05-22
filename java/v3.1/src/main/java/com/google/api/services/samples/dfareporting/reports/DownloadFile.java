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
import com.google.api.services.dfareporting.Dfareporting.Files.Get;
import com.google.api.services.dfareporting.model.File;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * This example illustrates how to download a file.
 */
public class DownloadFile {
  private static final String FILE_ID = "ENTER_FILE_ID_HERE";
  private static final String REPORT_ID = "ENTER_REPORT_ID_HERE";

  public static void runExample(Dfareporting reporting, long reportId, long fileId)
      throws Exception {
    // Retrieve the file metadata.
    File file = reporting.files().get(reportId, fileId).execute();

    if ("REPORT_AVAILABLE".equals(file.getStatus())) {
      // Prepare a local file to download the report contents to.
      java.io.File outFile = new java.io.File(Files.createTempDir(), generateFileName(file));

      // Create a get request.
      Get getRequest = reporting.files().get(reportId, fileId);

      // Optional: adjust the chunk size used when downloading the file.
      // getRequest.getMediaHttpDownloader().setChunkSize(MediaHttpDownloader.MAXIMUM_CHUNK_SIZE);

      // Execute the get request and download the file.
      try (OutputStream stream = new FileOutputStream(outFile)) {
        getRequest.executeMediaAndDownloadTo(stream);
      }

      System.out.printf("File %d downloaded to %s%n", file.getId(), outFile.getAbsolutePath());
    }
  }

  private static String generateFileName(File file) {
    // If no filename is specified, use the file ID instead.
    String fileName = file.getFileName();
    if (Strings.isNullOrEmpty(fileName)) {
      fileName = file.getId().toString();
    }

    String extension = "CSV".equals(file.getFormat()) ? ".csv" : ".xls";

    return fileName + extension;
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long fileId = Long.parseLong(FILE_ID);
    long reportId = Long.parseLong(REPORT_ID);

    runExample(reporting, reportId, fileId);
  }
}
