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
using System.IO;
using System.Text;
using Google.Apis.Dfareporting.v2_6;

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

      // Download the file.
      using (Stream fileStream = new MemoryStream()) {
        service.Files.Get(reportId, fileId).Download(fileStream);

        // Display the file contents.
        fileStream.Position = 0;
        StreamReader reader = new StreamReader(fileStream, Encoding.UTF8);
        Console.WriteLine(reader.ReadToEnd());
      }
    }
  }
}
