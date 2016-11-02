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
using System.Linq;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example retrieves available creative field values for a given string
  /// and displays the names and IDs.
  /// </summary>
  class GetCreativeFieldValues : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example retrieves available creative field values for a" +
            " given string and displays the names and IDs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetCreativeFieldValues();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long creativeFieldId = long.Parse(_T("ENTER_CREATIVE_FIELD_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));

      CreativeFieldValuesListResponse values;
      String nextPageToken = null;

      do {
        // Create and execute the creative field values list request.
        CreativeFieldValuesResource.ListRequest request =
            service.CreativeFieldValues.List(profileId, creativeFieldId);
        request.PageToken = nextPageToken;
        values = request.Execute();

        foreach (CreativeFieldValue value in values.CreativeFieldValues) {
          Console.WriteLine("Found creative field value with ID {0} and value \"{1}\".",
              value.Id, value.Value);
        }

        // Update the next page token.
        nextPageToken = values.NextPageToken;
      } while (values.CreativeFieldValues.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
