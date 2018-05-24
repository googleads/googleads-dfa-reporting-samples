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
using Google.Apis.Dfareporting.v3_1;
using Google.Apis.Dfareporting.v3_1.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example downloads activity tags for a given floodlight activity.
  /// </summary>
  class DownloadFloodlightTags : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example downloads activity tags for a given floodlight" +
            " activity.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new DownloadFloodlightTags();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long activityId = long.Parse(_T("ENTER_ACTIVITY_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      // Generate the floodlight activity tag.
      FloodlightActivitiesResource.GeneratetagRequest request =
          service.FloodlightActivities.Generatetag(profileId);
      request.FloodlightActivityId = activityId;

      FloodlightActivitiesGenerateTagResponse response = request.Execute();

      if (response.GlobalSiteTagGlobalSnippet != null) {
        // This is a global site tag, display both the global snippet and event snippet.
        Console.WriteLine("Global site tag global snippet:\n\n{0}",
            response.GlobalSiteTagGlobalSnippet);
        Console.WriteLine("\n\nGlobal site tag event snippet:\n\n{0}",
            response.FloodlightActivityTag);
      } else {
        // This is an image or iframe tag.
        Console.WriteLine("Floodlight activity tag:\n\n{0}", response.FloodlightActivityTag);
      }
    }
  }
}
