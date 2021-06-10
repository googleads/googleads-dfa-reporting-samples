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

using Google.Apis.Auth.OAuth2;
using Google.Apis.Dfareporting.v3_5;
using Google.Apis.Dfareporting.v3_5.Data;
using Google.Apis.Json;
using Google.Apis.Services;
using Google.Apis.Util.Store;
using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Threading.Tasks;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example demonstrates how to authenticate and make a basic request using a user account,
  /// via the <a href="https://developers.google.com/identity/protocols/OAuth2InstalledApp">
  /// installed application flow</a>.
  /// </summary>
  class AuthenticateUsingUserAccount : SampleBase {
    /// <summary>
    /// The location where authorization credentials will be cached.
    /// </summary>
    private static readonly string DataStorePath = Path.Combine(
        Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),
        "DfaReporting.Auth.Sample");

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
        return "This example demonstrates how to authenticate and make a basic request using a" +
               " user account, via the installed application flow.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new AuthenticateUsingUserAccount();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(null);
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">Unused</param>
    public override void Run(DfareportingService service) {
      string pathToJsonFile = _T("ENTER_PATH_TO_JSON_FILE_HERE");

      // Build user account credential.
      UserCredential credential =
          getUserAccountCredential(pathToJsonFile, new FileDataStore(DataStorePath, true));

      // Create a Dfareporting service object.
      //
      // Note: application name should be replaced with a value that identifies your application.
      service = new DfareportingService(
          new BaseClientService.Initializer {
            HttpClientInitializer = credential,
            ApplicationName = "C# installed app sample"
          }
      );

      // Retrieve and print all user profiles for the current authorized user.
      UserProfileList profiles = service.UserProfiles.List().Execute();

      foreach (UserProfile profile in profiles.Items) {
        Console.WriteLine("Found user profile with ID {0} and name \"{1}\".",
            profile.ProfileId, profile.UserName);
      }
    }

    private UserCredential getUserAccountCredential(String pathToJsonFile, IDataStore dataStore) {
      // Load client secrets from the specified JSON file.
      GoogleClientSecrets clientSecrets;
      using(Stream json = new FileStream(pathToJsonFile, FileMode.Open, FileAccess.Read)) {
        clientSecrets = GoogleClientSecrets.Load(json);
      }

      // Create an asynchronous authorization task.
      //
      // Note: providing a data store allows auth credentials to be cached, so they survive multiple
      // runs of the application. This avoids prompting the user for authorization every time the
      // access token expires, by remembering the refresh token. The "user" value is used to
      // identify a specific set of credentials within the data store. You may provide different
      // values here to persist credentials for multiple users to the same data store.
      Task<UserCredential> authorizationTask = GoogleWebAuthorizationBroker.AuthorizeAsync(
          clientSecrets.Secrets,
          OAuthScopes,
          "user",
          CancellationToken.None,
          dataStore);

      // Authorize and persist credentials to the data store.
      UserCredential credential = authorizationTask.Result;

      return credential;
    }
  }
}
