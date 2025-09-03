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

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.Dfareporting.Files.Get;
import com.google.api.services.dfareporting.model.File;
import com.google.api.services.dfareporting.model.FileList;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This example provides an end-to-end example of how to find and download a report file.
 */
public class DownloadReportGuide {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String REPORT_ID = "INSERT_REPORT_ID_HERE";

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long reportId = Long.parseLong(REPORT_ID);

    // 1. Find a file to download
    File file = findFile(reporting, profileId, reportId);

    if (file != null) {
      // 2. (optional) Generate browser URL
      generateBrowserUrl(reporting, reportId, file.getId());

      // 3. Directly download the file
      directDownloadFile(reporting, reportId, file.getId());
    } else {
      System.out.printf("No file found for profile ID %s and report ID %d.%n", profileId, reportId);
    }
  }

  private static File findFile(Dfareporting reporting, long profileId, long reportId)
      throws IOException {
    File target = null;
    FileList files;
    String nextPageToken = null;

    do {
      // Create and execute the files list request.
      files = reporting.reports().files().list(profileId, reportId).setPageToken(nextPageToken)
          .execute();

      for (File file : files.getItems()) {
        if (isTargetFile(file)) {
          target = file;
          break;
        }
      }

      // Update the next page token.
      nextPageToken = files.getNextPageToken();
    } while (target == null
        && !files.getItems().isEmpty()
        && !Strings.isNullOrEmpty(nextPageToken));

    if (target != null) {
      System.out.printf("Found file %d with filename \"%s\".%n", target.getId(),
          target.getFileName());
      return target;
    }

    System.out.printf("Unable to find file for profile ID %d and report ID %d.%n", profileId,
        reportId);
    return null;
  }

  private static boolean isTargetFile(File file) {
    // Provide custom validation logic here.
    // For example purposes, any file with REPORT_AVAILABLE status is considered valid.
    return "REPORT_AVAILABLE".equals(file.getStatus());
  }

  private static String generateBrowserUrl(Dfareporting reporting, long reportId, long fileId)
      throws IOException {
    File file = reporting.files().get(reportId, fileId).execute();
    String browserUrl = file.getUrls().getBrowserUrl();

    System.out.printf("File %d has browser URL: %s.%n", file.getId(), browserUrl);
    return browserUrl;
  }

  private static void directDownloadFile(Dfareporting reporting, long reportId, long fileId)
      throws IOException {
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
}

