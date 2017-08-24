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
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.DfareportingScopes;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
   * Authorizes the application to access users' protected data.
   *
   * @return An initialized {@link Credential} object.
   */
  private static Credential authorize() throws Exception {
    // Load application default credentials if they're available.
    Credential credential = loadApplicationDefaultCredentials();

    // Otherwise, load credentials from the provided client secrets file.
    if (credential == null) {
      String clientSecretsFile =
          DfaReportingFactory.class.getResource("/client_secrets.json").getFile();
      credential = loadUserCredentials(clientSecretsFile, new FileDataStoreFactory(DATA_STORE_DIR));
    }

    return credential;
  }

  /**
   * Attempts to load application default credentials.
   *
   * @return A {@link Credential} object initialized with application default credentials, or
   * {@code null} if none were found.
   */
  private static Credential loadApplicationDefaultCredentials() {
    try {
      GoogleCredential credential = GoogleCredential.getApplicationDefault();
      return credential.createScoped(DfareportingScopes.all());
    } catch (IOException ignored) {
      // No application default credentials, continue to try other options.
    }

    return null;
  }

  /**
   * Attempts to load user credentials from the provided client secrets file and persists data to
   * the provided data store.
   *
   * @param clientSecretsFile The path to the file containing client secrets.
   * @param dataStoreFactory he data store to use for caching credential information.
   * @return A {@link Credential} object initialized with user account credentials.
   */
  private static Credential loadUserCredentials(String clientSecretsFile,
      DataStoreFactory dataStoreFactory) throws Exception {
     // Load client secrets JSON file.
    GoogleClientSecrets clientSecrets;
    try (Reader reader = new FileReader(clientSecretsFile)) {
      clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
    }

    // Set up the authorization code flow.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
        JSON_FACTORY, clientSecrets, DfareportingScopes.all())
        .setDataStoreFactory(dataStoreFactory)
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
    Credential credential = authorize();

    // Create Dfareporting client.
    return new Dfareporting.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
        "dfareporting-java-samples").build();
  }
}
