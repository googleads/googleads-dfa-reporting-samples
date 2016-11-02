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
  /// This example inserts an offline conversion attributed to a mobile device ID.
  /// </summary>
  class InsertOfflineMobileConversion : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
          return "This example inserts an offline conversion attributed to a mobile device ID.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new InsertOfflineMobileConversion();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long floodlightActivityId = long.Parse(_T("INSERT_FLOODLIGHT_ACTIVITY_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string conversionMobileId = _T("INSERT_CONVERSION_MOBILE_ID_HERE");

      // Generate a timestamp in milliseconds since Unix epoch.
      TimeSpan timeStamp = DateTime.UtcNow - new DateTime(1970, 1, 1);
      long currentTimeInMilliseconds = (long) timeStamp.TotalMilliseconds;

      // Find the Floodlight configuration ID based on the provided activity ID.
      FloodlightActivity floodlightActivity =
          service.FloodlightActivities.Get(profileId, floodlightActivityId).Execute();
      long floodlightConfigurationId = (long) floodlightActivity.FloodlightConfigurationId;

      // Create the conversion.
      Conversion conversion = new Conversion();
      conversion.MobileDeviceId = conversionMobileId;
      conversion.FloodlightActivityId = floodlightActivityId;
      conversion.FloodlightConfigurationId = floodlightConfigurationId;
      conversion.Ordinal = currentTimeInMilliseconds.ToString();
      conversion.TimestampMicros = currentTimeInMilliseconds * 1000;

      // Insert the conversion.
      ConversionsBatchInsertRequest request = new ConversionsBatchInsertRequest();
      request.Conversions = new List<Conversion>() { conversion };

      ConversionsBatchInsertResponse response =
          service.Conversions.Batchinsert(request, profileId).Execute();

      // Handle the batchinsert response.
      if (!response.HasFailures.Value) {
        Console.WriteLine("Successfully inserted conversion for mobile device ID {0}.",
            conversionMobileId);
      } else {
        Console.WriteLine("Error(s) inserting conversion for mobile device ID {0}:",
            conversionMobileId);

        ConversionStatus status = response.Status[0];
        foreach(ConversionError error in status.Errors) {
          Console.WriteLine("\t[{0}]: {1}", error.Code, error.Message);
        }
      }
    }
  }
}
