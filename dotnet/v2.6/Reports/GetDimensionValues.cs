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
  /// This example illustrates how to get all dimension values for a dimension.
  /// </summary>
  class GetDimensionValues : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example illustrates how to get all dimension values for" +
            " a dimension.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetDimensionValues();
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

      // Limit the fields returned.
      String fields = "nextPageToken,items(id,value)";

      // Construct the dimension value request.
      DimensionValueRequest request = new DimensionValueRequest();
      request.DimensionName = "dfa:advertiser";

      // Set the date range from 1 year ago until today.
      request.StartDate =
          DfaReportingDateConverterUtil.convertToDateString(DateTime.Now.AddYears(-1));
      request.EndDate =
          DfaReportingDateConverterUtil.convertToDateString(DateTime.Now);

      DimensionValueList values;
      String nextPageToken = null;

      do {
        // Create and execute the dimension value query request.
        DimensionValuesResource.QueryRequest query =
            service.DimensionValues.Query(request, profileId);
        query.Fields = fields;
        query.PageToken = nextPageToken;
        values = query.Execute();

        foreach (DimensionValue value in values.Items) {
          Console.WriteLine("Dimension value with ID {0} and value \"{1}\" was found.",
              value.Id, value.Value);
        }

        // Update the next page token.
        nextPageToken = values.NextPageToken;
      } while (values.Items.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
