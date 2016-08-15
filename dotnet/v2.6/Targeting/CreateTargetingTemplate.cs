/*
 * Copyright 2016 Google Inc
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
using System.Collections.Generic;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a basic targeting template associated with a given advertiser. To get an
  /// advertiser ID, run GetAdvertisers.cs.
  /// </summary>
  class CreateTargetingTemplate : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a basic targeting template associated with a given " +
            "advertiser. To get an advertiser ID, run GetAdvertisers.cs.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateTargetingTemplate();
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
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string targetingTemplateName = _T("INSERT_TARGETING_TEMPLATE_NAME_HERE");

      // Create the targeting template.
      TargetingTemplate template = new TargetingTemplate();
      template.AdvertiserId = advertiserId;
      template.Name = targetingTemplateName;

      // Configure the template to serve ads on Monday, Wednesday, and Friday from 9-10am
      // and 3-5pm.
      DayPartTargeting dayTargeting = new DayPartTargeting();
      dayTargeting.DaysOfWeek = new List<string>() { "MONDAY", "WEDNESDAY", "FRIDAY" };
      dayTargeting.HoursOfDay = new List<int?>() { 9, 15, 16 };
      dayTargeting.UserLocalTime = true;
      template.DayPartTargeting = dayTargeting;

      // Insert the targeting template.
      TargetingTemplate result = service.TargetingTemplates.Insert(template, profileId).Execute();

      // Display the new targeting template ID.
      Console.WriteLine("Targeting template with ID {0} was created.", result.Id);
    }
  }
}
