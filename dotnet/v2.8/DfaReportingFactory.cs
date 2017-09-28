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
using System.Collections.Generic;
using System.Threading;
using Google.Apis.Auth.OAuth2;
using Google.Apis.Dfareporting.v2_8;
using Google.Apis.Services;
using Google.Apis.Util.Store;

namespace DfaReporting.Samples {
  /// <summary>
  /// Factory for generating DFA Reporting and Trafficking API service objects.
  /// </summary>
  class DfaReportingFactory {
    /// <summary>
    /// The scopes used to make reporting and trafficking requests.
    /// </summary>
    private static readonly IEnumerable<string> scopes = new[] {
      DfareportingService.Scope.Dfareporting,
      DfareportingService.Scope.Dfatrafficking,
      DfareportingService.Scope.Ddmconversions
    };

    /// <summary>
    /// Authorizes the application to access users' protected data.
    /// </summary>
    private static ICredential Authorize() {
      // Load application default credentials if they're available.
      ICredential credential = LoadApplicationDefaultCredentials();

      // Otherwise, load credentials from the provided client secrets file.
      if (credential == null) {
        credential = LoadUserCredentials("client_secrets.json",
            new FileDataStore("DfaReporting.Samples"));
      }

      return credential;
    }

    /// <summary>
    /// Attempts to load the application default credentials
    /// </summary>
    /// <returns>The application default credentials, or null if none were found.</returns>
    private static ICredential LoadApplicationDefaultCredentials() {
      try {
         GoogleCredential credential = GoogleCredential.GetApplicationDefaultAsync().Result;
         return credential.CreateScoped(scopes);
      } catch (Exception) {
        // No application default credentials, continue to try other options.
      }

      return null;
    }
    
    /// <summary>
    /// Attempts to load user credentials from the provided client secrets file and persists data to
    /// the provided data store.
    /// </summary>
    /// <returns>The user credentials.</returns>
    /// <param name="clientSecretsFile">Path to the file containing client secrets.</param>
    /// <param name="dataStore">The data store to use for caching credential information.</param>
    private static ICredential LoadUserCredentials(String clientSecretsFile, IDataStore dataStore) {
      using (var stream = new System.IO.FileStream(clientSecretsFile, System.IO.FileMode.Open,
          System.IO.FileAccess.Read)) {
        return GoogleWebAuthorizationBroker.AuthorizeAsync(
            GoogleClientSecrets.Load(stream).Secrets,
            scopes,
            "dfa-user", CancellationToken.None,
            dataStore).Result;
      }
    }

    /// <summary>
    /// Initializes a <code>DfaReportingService</code> instance.
    /// </summary>
    /// <returns>An initialized <code>DfaReportingService</code> object.</returns>
    public static DfareportingService getInstance() {
      ICredential credential = Authorize();

      // Create and return the service.
      return new DfareportingService(new BaseClientService.Initializer {
        HttpClientInitializer = credential,
        ApplicationName = "DFA/DCM Reporting and Trafficking API Samples"
      });
    }
  }
}
