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

using System.Collections.Generic;
using System.Threading;
using Google.Apis.Auth.OAuth2;
using Google.Apis.Dfareporting.v2_6;
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
    /// Initializes a <code>DfaReportingService</code> instance using
    /// credentials stored in <code>client_secrets.json</code>.
    /// </summary>
    /// <returns>An initialized <code>DfaReportingService</code> object.</returns>
    public static DfareportingService getInstance() {
      UserCredential credential;

      using (var stream = new System.IO.FileStream("client_secrets.json",
          System.IO.FileMode.Open, System.IO.FileAccess.Read)) {
        credential = GoogleWebAuthorizationBroker.AuthorizeAsync(
            GoogleClientSecrets.Load(stream).Secrets,
            scopes,
            "dfa-user", CancellationToken.None,
            new FileDataStore("DfaReporting.Samples")).Result;
      }

      // Create and return the service.
      return new DfareportingService(new BaseClientService.Initializer {
        HttpClientInitializer = credential,
        ApplicationName = "DFA/DCM Reporting and Trafficking API Samples"
      });
    }
  }
}
