/*
 * Copyright 2017 Google Inc
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
using Google.Apis.Dfareporting.v3_0;
using Google.Apis.Dfareporting.v3_0.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a remarketing list for a given advertiser and floodlight activity, using
  /// a custom rule.
  ///
  /// Note: this sample assumes that the floodlight activity specified has a U1 custom floodlight
  /// variable.
  /// </summary>
  class CreateRemarketingList : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a remarketing list for a given advertiser" +
            " and floodlight activity, using a custom rule.\n\n" +
            "Note: this sample assumes that the floodlight activity specified" +
            " has a U1 custom floodlight variable.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateRemarketingList();
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
      long floodlightActivityId = long.Parse(_T("INSERT_FLOODLIGHT_ACTIVITY_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string remarketingListName = _T("INSERT_REMARKETING_LIST_NAME_HERE");

      // Create the remarketing list.
      RemarketingList remarketingList = new RemarketingList();
      remarketingList.Active = true;
      remarketingList.AdvertiserId = advertiserId;
      remarketingList.LifeSpan = 30L;
      remarketingList.Name = remarketingListName;

      // Create a list population term.
      // This term matches all visitors with a U1 value exactly matching "test_value".
      ListPopulationTerm term = new ListPopulationTerm();
      term.Operator__ = "STRING_EQUALS";
      term.Type = "CUSTOM_VARIABLE_TERM";
      term.Value = "test_value";
      term.VariableName = "U1";

      // Add the term to a list population clause.
      ListPopulationClause clause = new ListPopulationClause();
      clause.Terms = new List<ListPopulationTerm> { term };

      // Add the clause to a list population rule.
      // This rule will target all visitors who trigger the specified floodlight activity and
      // satisfy the custom rule defined in the list population term.
      ListPopulationRule rule = new ListPopulationRule();
      rule.FloodlightActivityId = floodlightActivityId;
      rule.ListPopulationClauses = new List<ListPopulationClause> { clause };
      remarketingList.ListPopulationRule = rule;

      // Insert the remarketing list.
      RemarketingList result =
          service.RemarketingLists.Insert(remarketingList, profileId).Execute();

      // Display the new remarketing list ID.
      Console.WriteLine("Remarketing list with ID {0} was created.", result.Id);
    }
  }
}
