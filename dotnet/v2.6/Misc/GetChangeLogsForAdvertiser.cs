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
  /// This example displays the change logs of a specified advertiser object.
  /// A similar pattern can be applied to get change logs for many other object
  /// types.
  /// </summary>
  class GetChangeLogsForAdvertiser : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example displays the change logs of a specified" +
            " advertiser object. A similar pattern can be applied to get" +
            " change logs for many other object types.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new GetChangeLogsForAdvertiser();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long advertiserId = long.Parse(_T("INSERT_ADVERTISER_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_PROFILE_ID_HERE"));

      // Limit the fields returned.
      String fields = "nextPageToken,changeLogs(action,fieldName,oldValue,newValue)";

      ChangeLogsListResponse changeLogs;
      String nextPageToken = null;

      do {
        // Create and execute the change logs list request
        ChangeLogsResource.ListRequest request = service.ChangeLogs.List(profileId);
        request.ObjectIds = advertiserId.ToString();
        request.ObjectType = ChangeLogsResource.ListRequest.ObjectTypeEnum.OBJECTADVERTISER;
        request.Fields = fields;
        request.PageToken = nextPageToken;
        changeLogs = request.Execute();

        foreach (ChangeLog changeLog in changeLogs.ChangeLogs) {
          Console.WriteLine("{0}: Field \"{1}\" from \"{2}\" to \"{3}\".",
              changeLog.Action, changeLog.FieldName, changeLog.OldValue,
              changeLog.NewValue);
        }

        // Update the next page token.
        nextPageToken = changeLogs.NextPageToken;
      } while (changeLogs.ChangeLogs.Any() && !String.IsNullOrEmpty(nextPageToken));
    }
  }
}
