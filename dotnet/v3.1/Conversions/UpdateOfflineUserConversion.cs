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
using Google.Apis.Dfareporting.v3_1;
using Google.Apis.Dfareporting.v3_1.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example updates an offline conversion attributed to an encrypted user ID. To create a
  /// conversion attributed to an excrypted user ID, run InsertOfflineUserConversion.cs.
  /// </summary>
  class UpdateOfflineUserConversion : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example updates an offline conversion attributed to an encrypted user ID. " +
            "To create a conversion attributed to an encrypted user ID, run " +
            "InsertOfflineUserConversion.cs\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new UpdateOfflineUserConversion();
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

      // Value that specify how the existing conversion is encrypted.
      long encryptionEntityId = long.Parse(_T("INSERT_ENCRYPTION_ENTITY_TYPE"));
      string encryptionEntityType = _T("INSERT_ENCRYPTION_ENTITY_TYPE_HERE");
      string encryptionSource = _T("INSERT_ENCRYPTION_SOURCE_HERE");

      // Values that identify the existing conversion.
      long floodlightActivityId = long.Parse(_T("INSERT_FLOODLIGHT_ACTIVITY_ID_HERE"));
      long conversionTimestamp = long.Parse(_T("INSERT_CONVERSION_TIMESTAMP_HERE"));
      string conversionOrdinal = _T("INSERT_CONVERSION_ORDINAL_VALUE_HERE");
      string conversionUserId = _T("INSERT_CONVERSION_USER_ID_HERE");

      // Values to update for the specified conversion.
      long newQuantity = long.Parse(_T("INSERT_NEW_CONVERSION_QUANTITY_HERE"));
      long newValue = long.Parse(_T("INSERT_NEW_CONVERSION_VALUE_HERE"));

      // Find the Floodlight configuration ID based on the provided activity ID.
      FloodlightActivity floodlightActivity =
          service.FloodlightActivities.Get(profileId, floodlightActivityId).Execute();
      long floodlightConfigurationId = (long) floodlightActivity.FloodlightConfigurationId;

      // Construct the conversion object with values that identify the conversion to update.
      Conversion conversion = new Conversion();
      conversion.EncryptedUserId = conversionUserId;
      conversion.FloodlightActivityId = floodlightActivityId;
      conversion.FloodlightConfigurationId = floodlightConfigurationId;
      conversion.Ordinal = conversionOrdinal;
      conversion.TimestampMicros = conversionTimestamp;

      // Set the fields to be updated. These fields are required; to preserve a value from the
      // existing conversion, it must be copied over manually.
      conversion.Quantity = newQuantity;
      conversion.Value = newValue;

      // Create the encryption info.
      EncryptionInfo encryptionInfo = new EncryptionInfo();
      encryptionInfo.EncryptionEntityId = encryptionEntityId;
      encryptionInfo.EncryptionEntityType = encryptionEntityType;
      encryptionInfo.EncryptionSource = encryptionSource;

      // Insert the conversion.
      ConversionsBatchUpdateRequest request = new ConversionsBatchUpdateRequest();
      request.Conversions = new List<Conversion>() { conversion };
      request.EncryptionInfo = encryptionInfo;

      ConversionsBatchUpdateResponse response =
          service.Conversions.Batchupdate(request, profileId).Execute();

      // Handle the batchinsert response.
      if (!response.HasFailures.Value) {
        Console.WriteLine("Successfully updated conversion for encrypted user ID {0}.",
            conversionUserId);
      } else {
        Console.WriteLine("Error(s) updating conversion for encrypted user ID {0}:",
            conversionUserId);

        ConversionStatus status = response.Status[0];
        foreach(ConversionError error in status.Errors) {
          Console.WriteLine("\t[{0}]: {1}", error.Code, error.Message);
        }
      }
    }
  }
}
