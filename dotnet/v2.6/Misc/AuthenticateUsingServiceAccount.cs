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

using Google.Apis.Auth.OAuth2;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;
using Google.Apis.Json;
using Google.Apis.Services;
using System;
using System.IO;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example shows how to authenticate and make a basic request using a service account.
  /// </summary>
  class AuthenticateUsingServiceAccount : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example shows how to authenticate and make a basic request" +
            " using a service account.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new AuthenticateUsingServiceAccount();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(null);
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">Unused</param>
    public override void Run(DfareportingService service) {
      string emailToImpersonate = _T("ENTER_EMAIL_TO_IMPERSONATE_HERE");
      string pathToJsonFile = _T("ENTER_PATH_TO_JSON_FILE_HERE");

      // Create the Dfareporting service using service account credentials.
      service = new DfareportingService(new BaseClientService.Initializer {
        HttpClientInitializer =
            getServiceAccountCredential(pathToJsonFile, emailToImpersonate),
        ApplicationName = "DFA/DCM Reporting and Trafficking API Samples"
      });

      UserProfileList profiles = service.UserProfiles.List().Execute();

      foreach (UserProfile profile in profiles.Items) {
        Console.WriteLine("Found user profile with ID {0} and name \"{1}\".",
            profile.ProfileId, profile.UserName);
      }
    }

    private ServiceAccountCredential getServiceAccountCredential(String pathToJsonFile,
      String emailToImpersonate) {
      // Load and deserialize the specified JSON file.
      JsonCredentialParameters parameters;
      using (Stream json = new FileStream(pathToJsonFile, FileMode.Open, FileAccess.Read)) {
        parameters = NewtonsoftJsonSerializer.Instance.Deserialize<JsonCredentialParameters>(json);
      }

      // Generate a ServiceAccountCredential object with the correct scopes and impersonation info.
      return new ServiceAccountCredential(
         new ServiceAccountCredential.Initializer(parameters.ClientEmail) {
           Scopes = new[] {
             DfareportingService.Scope.Dfareporting,
             DfareportingService.Scope.Dfatrafficking,
             DfareportingService.Scope.Ddmconversions
           },
           User = emailToImpersonate
         }.FromPrivateKey(parameters.PrivateKey));
    }
  }
}
