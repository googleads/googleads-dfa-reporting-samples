// Copyright 2017 Google Inc. All Rights Reserved.
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

package com.google.api.services.samples.dfareporting.auth;

import static java.nio.charset.StandardCharsets.UTF_8;

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
import com.google.api.services.dfareporting.model.UserProfileList;
import com.google.common.collect.ImmutableSet;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This example demonstrates how to authenticate and make a basic request using a user account, via
 * the OAuth 2.0 <a href="https://developers.google.com/identity/protocols/OAuth2InstalledApp">
 * installed application flow</a>.
 */
public class AuthenticateUsingUserAccount {
  private static final String PATH_TO_CLIENT_SECRETS = "ENTER_PATH_TO_CLIENT_SECRETS_HERE";

  // Location where authorization credentials will be cached.
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/dfareporting_auth_sample");

  // The OAuth 2.0 scopes to request.
  private static final ImmutableSet<String> OAUTH_SCOPES =
      ImmutableSet.of(DfareportingScopes.DFAREPORTING);

  private static Credential getUserAccountCredential(
      String pathToClientSecretsFile, DataStoreFactory dataStoreFactory) throws Exception {
    HttpTransport httpTransport = Utils.getDefaultTransport();
    JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

    // Load the client secrets JSON file.
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(
            jsonFactory, Files.newBufferedReader(Paths.get(pathToClientSecretsFile), UTF_8));

    // Set up the authorization code flow.
    //
    // Note: providing a DataStoreFactory allows auth credentials to be cached, so they survive
    // multiple runs of the program. This avoids prompting the user for authorization every time the
    // access token expires, by remembering the refresh token.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, OAUTH_SCOPES)
            .setDataStoreFactory(dataStoreFactory)
            .build();

    // Authorize and persist credentials to the data store.
    //
    // Note: the "user" value below is used to identify a specific set of credentials in the data
    // store. You may provide different values here to persist credentials for multiple users to
    // the same data store.
    Credential credential =
        new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

    return credential;
  }

  public static void runExample(Dfareporting reporting) throws Exception {
    // Retrieve and print all user profiles for the current authorized user.
    UserProfileList profiles = reporting.userProfiles().list().execute();
    for (int i = 0; i < profiles.getItems().size(); i++) {
      System.out.printf("%d) %s%n", i + 1, profiles.getItems().get(i).getUserName());
    }
  }

  public static void main(String[] args) throws Exception {
    // Build installed application credential.
    Credential credential =
        getUserAccountCredential(PATH_TO_CLIENT_SECRETS, new FileDataStoreFactory(DATA_STORE_DIR));

    // Create a Dfareporting client instance.
    //
    // Note: application name below should be replaced with a value that identifies your
    // application. Suggested format is "MyCompany-ProductName/Version.MinorVersion".
    Dfareporting reporting =
        new Dfareporting.Builder(credential.getTransport(), credential.getJsonFactory(), credential)
            .setApplicationName("dfareporting-java-installed-app-sample")
            .build();

    runExample(reporting);
  }
}
