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
using System.Security.Cryptography.X509Certificates;
using Google.Apis.Auth.OAuth2;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;
using Google.Apis.Services;

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
      string accountToImpersonate = _T("ENTER_ACCOUNT_TO_IMPERSONATE_HERE");
      string pathToP12File = _T("ENTER_PATH_TO_P12_FILE_HERE");
      string serviceAccountEmail = _T("ENTER_SERVICE_ACCOUNT_EMAIL_HERE");

      // Create the Dfareporting service using service account credentials.
      service = new DfareportingService(new BaseClientService.Initializer {
        HttpClientInitializer =
            getServiceAccountCredential(pathToP12File, serviceAccountEmail, accountToImpersonate),
        ApplicationName = "DFA/DCM Reporting and Trafficking API Samples"
      });

      UserProfileList profiles = service.UserProfiles.List().Execute();

      foreach (UserProfile profile in profiles.Items) {
        Console.WriteLine("Found user profile with ID {0} and name \"{1}\".",
            profile.ProfileId, profile.UserName);
      }
    }

    private ServiceAccountCredential getServiceAccountCredential(String pathToP12File,
      String serviceAccountEmail, String accountToImpersonate) {
      // Load the P12 file using a default password.
      var certificate =
          new X509Certificate2(pathToP12File, "notasecret", X509KeyStorageFlags.Exportable);

      return new ServiceAccountCredential(
         new ServiceAccountCredential.Initializer(serviceAccountEmail) {
           Scopes = new[] { DfareportingService.Scope.Dfareporting },
           User = accountToImpersonate
         }.FromCertificate(certificate));
    }
  }
}
