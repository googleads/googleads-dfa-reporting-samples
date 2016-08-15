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
  /// This example creates a creative field value associated with a given
  /// creative field. To get the creative field ID, run GetCreativeFields.java.
  /// </summary>
  class CreateCreativeFieldValue : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a creative field value associated with" +
            " a given creative field. To get the creative field ID, run" +
            " GetCreativeFields.java.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateCreativeFieldValue();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long creativeFieldId = long.Parse(_T("INSERT_CREATIVE_FIELD_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string creativeFieldValueName = _T("INSERT_CREATIVE_FIELD_VALUE_NAME_HERE");

      // Create the creative field value.
      CreativeFieldValue creativeFieldValue = new CreativeFieldValue();
      creativeFieldValue.Value = creativeFieldValueName;

      // Insert the creative field value.
      CreativeFieldValue result = service.CreativeFieldValues
          .Insert(creativeFieldValue, profileId, creativeFieldId).Execute();

      // Display the new creative field value ID.
      Console.WriteLine("Creative field value with ID {0} was created.", result.Id);
    }
  }
}
