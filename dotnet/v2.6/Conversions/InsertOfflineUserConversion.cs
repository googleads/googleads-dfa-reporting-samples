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
  /// This example inserts an offline conversion attributed to an encrypted user ID.
  /// </summary>
  class InsertOfflineUserConversion : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
          return "This example inserts an offline conversion attributed to an encrypted user" +
              " ID.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new InsertOfflineUserConversion();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long encryptionEntityId = long.Parse(_T("INSERT_ENCRYPTION_ENTITY_TYPE"));
      long floodlightActivityId = long.Parse(_T("INSERT_FLOODLIGHT_ACTIVITY_ID_HERE"));
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      string conversionUserId = _T("INSERT_CONVERSION_USER_ID_HERE");
      string encryptionEntityType = _T("INSERT_ENCRYPTION_ENTITY_TYPE_HERE");
      string encryptionSource = _T("INSERT_ENCRYPTION_SOURCE_HERE");

      // Generate a timestamp in milliseconds since Unix epoch.
      TimeSpan timeSpan = DateTime.UtcNow - new DateTime(1970, 1, 1);
      long currentTimeInMilliseconds = (long) timeSpan.TotalMilliseconds;

      // Find the Floodlight configuration ID based on the provided activity ID.
      FloodlightActivity floodlightActivity =
          service.FloodlightActivities.Get(profileId, floodlightActivityId).Execute();
      long floodlightConfigurationId = (long) floodlightActivity.FloodlightConfigurationId;

      // Create the conversion.
      Conversion conversion = new Conversion();
      conversion.EncryptedUserId = conversionUserId;
      conversion.FloodlightActivityId = floodlightActivityId;
      conversion.FloodlightConfigurationId = floodlightConfigurationId;
      conversion.Ordinal = currentTimeInMilliseconds.ToString();
      conversion.TimestampMicros = currentTimeInMilliseconds * 1000;

      // Create the encryption info.
      EncryptionInfo encryptionInfo = new EncryptionInfo();
      encryptionInfo.EncryptionEntityId = encryptionEntityId;
      encryptionInfo.EncryptionEntityType = encryptionEntityType;
      encryptionInfo.EncryptionSource = encryptionSource;

      // Insert the conversion.
      ConversionsBatchInsertRequest request = new ConversionsBatchInsertRequest();
      request.Conversions = new List<Conversion>() { conversion };
      request.EncryptionInfo = encryptionInfo;

      ConversionsBatchInsertResponse response =
          service.Conversions.Batchinsert(request, profileId).Execute();

      // Handle the batchinsert response.
      if (!response.HasFailures.Value) {
        Console.WriteLine("Successfully inserted conversion for encrypted user ID {0}.",
            conversionUserId);
      } else {
        Console.WriteLine("Error(s) inserting conversion for encrypted user ID {0}:",
            conversionUserId);

        ConversionStatus status = response.Status[0];
        foreach(ConversionError error in status.Errors) {
          Console.WriteLine("\t[{0}]: {1}", error.Code, error.Message);
        }
      }
    }
  }
}
