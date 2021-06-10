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
using Google.Apis.Dfareporting.v3_5;
using Google.Apis.Dfareporting.v3_5.Data;
using Google.Apis.Json;
using Google.Apis.Services;
using System;
using System.Collections.Generic;
using System.IO;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example demonstrates how to authenticate and make a basic request using a service
  /// account.
  /// </summary>
  class AuthenticateUsingServiceAccount : SampleBase {
    /// <summary>
    /// The OAuth 2.0 scopes to request.
    /// </summary>
    private static readonly IEnumerable<string> OAuthScopes = new[] {
      DfareportingService.Scope.Dfareporting
    };

    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example demonstrates how to authenticate and make a basic request" +
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
      string pathToJsonFile = _T("ENTER_PATH_TO_JSON_FILE_HERE");

      // An optional Google account email to impersonate. Only applicable to service accounts which
      // have enabled domain-wide delegation and wish to make API requests on behalf of an account
      // within their domain. Setting this field will not allow you to impersonate a user from a
      // domain you don't own (e.g., gmail.com).
      string emailToImpersonate = _T("");

      // Build service account credential.
      ServiceAccountCredential credential =
          getServiceAccountCredential(pathToJsonFile, emailToImpersonate);

      // Create a Dfareporting service object.
      //
      // Note: application name should be replaced with a value that identifies your application.
      service = new DfareportingService(
          new BaseClientService.Initializer {
            HttpClientInitializer = credential,
            ApplicationName = "C# service account sample"
          }
      );

      // Retrieve and print all user profiles for the current authorized user.
      UserProfileList profiles = service.UserProfiles.List().Execute();

      foreach (UserProfile profile in profiles.Items) {
        Console.WriteLine("Found user profile with ID {0} and name \"{1}\".",
            profile.ProfileId, profile.UserName);
      }
    }

    private ServiceAccountCredential getServiceAccountCredential(String pathToJsonFile,
        String emailToImpersonate) {
      // Load and deserialize credential parameters from the specified JSON file.
      JsonCredentialParameters parameters;
      using (Stream json = new FileStream(pathToJsonFile, FileMode.Open, FileAccess.Read)) {
        parameters = NewtonsoftJsonSerializer.Instance.Deserialize<JsonCredentialParameters>(json);
      }

      // Create a credential initializer with the correct scopes.
      ServiceAccountCredential.Initializer initializer =
          new ServiceAccountCredential.Initializer(parameters.ClientEmail) {
            Scopes = OAuthScopes
          };

      // Configure impersonation (if applicable).
      if (!String.IsNullOrEmpty(emailToImpersonate)) {
        initializer.User = emailToImpersonate;
      }

      // Create a service account credential object using the deserialized private key.
      ServiceAccountCredential credential =
          new ServiceAccountCredential(initializer.FromPrivateKey(parameters.PrivateKey));

      return credential;
    }
  }
}
