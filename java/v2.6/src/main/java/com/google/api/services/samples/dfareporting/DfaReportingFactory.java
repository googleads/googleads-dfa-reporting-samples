// Copyright 2014 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.services.samples.dfareporting;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.DfareportingScopes;

import java.io.InputStreamReader;

/**
 * Utility methods used by all DFA Reporting and Trafficking API samples.
 */
public class DfaReportingFactory {
  /** Directory to store user credentials. */
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/dfareporting_sample");

  private static final HttpTransport HTTP_TRANSPORT = Utils.getDefaultTransport();
  private static final JsonFactory JSON_FACTORY = Utils.getDefaultJsonFactory();

  /**
   * Authorizes the installed application to access user's protected data.
   * 
   * @param dataStoreFactory The data store to use for caching credential information.
   * @return A {@link Credential} object initialized with the current user's credentials.
   */
  private static Credential authorize(DataStoreFactory dataStoreFactory) throws Exception {
    // Load client secrets JSON file.
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY, new InputStreamReader(
            DfaReportingFactory.class.getResourceAsStream("/client_secrets.json")));

    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.out.println("Enter Client ID and Secret from "
          + "https://console.developers.google.com/project into "
          + "dfareporting-java-sample/src/main/resources/client_secrets.json");
      System.exit(1);
    }

    // Set up the authorization code flow.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
        JSON_FACTORY, clientSecrets, DfareportingScopes.all()).setDataStoreFactory(dataStoreFactory)
        .build();

    // Authorize and persist credential information to the data store.
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  /**
   * Performs all necessary setup steps for running requests against the API.
   * 
   * @return An initialized {@link Dfareporting} service object.
   */
  public static Dfareporting getInstance() throws Exception {
    FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

    Credential credential = authorize(dataStoreFactory);

    // Create Dfareporting client.
    return new Dfareporting.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
        "dfareporting-java-samples").build();
  }
}
