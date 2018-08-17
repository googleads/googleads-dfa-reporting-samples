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
using Google.Apis.Dfareporting.v3_2;
using Google.Apis.Dfareporting.v3_2.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example provides an end-to-end example of how to find and download a
  /// report file.
  /// </summary>
  class FindAndDownloadFile: SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example provides an end-to-end example of how to find " +
                "and download a report file.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new FindAndDownloadFile();
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
      long reportId = long.Parse(_T("INSERT_REPORT_ID_HERE"));

      // 1. Find a file to download.
      File file = FindFile(service, profileId, reportId);

      if (file != null) {
        // 2. (optional) Generate browser URL.
        GenerateBrowserUrl(service, reportId, file.Id.Value);

        // 3. Directly download the file
        DirectDownloadFile(service, reportId, file.Id.Value);
      } else {
        Console.WriteLine(
            "No file found for profile ID {0} and report ID {1}.",
            profileId, reportId);
      }
    }

    private File FindFile(DfareportingService service, long profileId,
        long reportId) {
      File target = null;
      FileList files;
      String nextPageToken = null;

      do {
        // Create and execute the files list request.
        ReportsResource.FilesResource.ListRequest request =
            service.Reports.Files.List(profileId, reportId);
        request.PageToken = nextPageToken;
        files = request.Execute();

        foreach (File file in files.Items) {
          if (IsTargetFile(file)) {
            target = file;
            break;
          }
        }

        // Update the next page token.
        nextPageToken = files.NextPageToken;
      } while (target == null
          && files.Items.Any()
          && !String.IsNullOrEmpty(nextPageToken));

      if (target != null) {
        Console.WriteLine("Found file {0} with filename \"{1}\".",
            target.Id, target.FileName);
        return target;
      }

      Console.WriteLine(
          "Unable to find file for profile ID {0} and report ID {1}.",
          profileId, reportId);
      return null;
    }

    private bool IsTargetFile(File file) {
      // Provide custom validation logic here.
      // For example purposes, any file with REPORT_AVAILABLE status is
      // considered valid.
      return "REPORT_AVAILABLE".Equals(file.Status);
    }

    private void GenerateBrowserUrl(DfareportingService service, long reportId,
        long fileId) {
      File file = service.Files.Get(reportId, fileId).Execute();
      String browserUrl = file.Urls.BrowserUrl;

      Console.WriteLine("File {0} has browser URL: {1}.", file.Id, browserUrl);
    }

    private void DirectDownloadFile(DfareportingService service, long reportId,
        long fileId) {
      // Retrieve the file metadata.
      File file = service.Files.Get(reportId, fileId).Execute();

      if ("REPORT_AVAILABLE".Equals(file.Status)) {
        // Create a get request.
        FilesResource.GetRequest getRequest = service.Files.Get(reportId, fileId);

        // Optional: adjust the chunk size used when downloading the file.
        // getRequest.MediaDownloader.ChunkSize = MediaDownloader.MaximumChunkSize;

        // Execute the get request and download the file.
        using (System.IO.FileStream outFile = new System.IO.FileStream(GenerateFileName(file),
            System.IO.FileMode.Create, System.IO.FileAccess.Write)) {
          getRequest.Download(outFile);
          Console.WriteLine("File {0} downloaded to {1}", file.Id, outFile.Name);
        }
      }
    }

    private string GenerateFileName(File file) {
      // If no filename is specified, use the file ID instead.
      string fileName = file.FileName;
      if (String.IsNullOrEmpty(fileName)) {
        fileName = file.Id.ToString();
      }

      String extension = "CSV".Equals(file.Format) ? ".csv" : ".xml";

      return fileName + extension;
    }
  }
}
