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
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates an event tag for the specified campaign.
  /// </summary>
  class CreateCampaignEventTag : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates an event tag for the specified campaign.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateCampaignEventTag();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long campaignId = long.Parse(_T("INSERT_CAMPAIGN_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      String eventTagName = _T("INSERT_EVENT_TAG_NAME_HERE");
      String eventTagUrl = _T("INSERT_EVENT_TAG_URL_HERE");

      // Create the event tag structure.
      EventTag eventTag = new EventTag();
      eventTag.CampaignId = campaignId;
      eventTag.Name = eventTagName;
      eventTag.Status = "ENABLED";
      eventTag.Type = "CLICK_THROUGH_EVENT_TAG";
      eventTag.Url = eventTagUrl;

      // Insert the campaign.
      EventTag result = service.EventTags.Insert(eventTag, profileId).Execute();

      // Display the new campaign ID.
      Console.WriteLine("Event Tag with ID {0} was created.", result.Id);
    }
  }
}
