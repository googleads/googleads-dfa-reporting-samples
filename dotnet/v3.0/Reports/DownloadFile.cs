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
using System.Text;
using Google.Apis.Dfareporting.v3_0;
using Google.Apis.Dfareporting.v3_0.Data;
using Google.Apis.Download;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example illustrates how to download a file.
  /// </summary>
  class DownloadFile : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example illustrates how to download a file.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new DownloadFile();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long fileId = long.Parse(_T("INSERT_FILE_ID_HERE"));
      long reportId = long.Parse(_T("ENTER_REPORT_ID_HERE"));

      // Retrive the file metadata.
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
