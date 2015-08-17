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

package com.google.api.services.samples.dfareporting.misc;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.DfareportingScopes;
import com.google.api.services.dfareporting.model.UserProfileList;
import com.google.common.collect.ImmutableList;

import java.io.File;

/**
 * This example shows how to authenticate and make a basic request using a service account.
 */
public class AuthenticateUsingServiceAccount {
  private static final String PATH_TO_P12_FILE = "ENTER_PATH_TO_P12_FILE_HERE";
  private static final String SERVICE_ACCOUNT_EMAIL = "ENTER_SERVICE_ACCOUNT_EMAIL_HERE";
  private static final String ACCOUNT_TO_IMPERSONATE = "ENTER_ACCOUNT_TO_IMPERSONATE_HERE";

  private static Credential getServiceAccountCredential(HttpTransport httpTransport,
      JsonFactory factory) throws Exception {
    // Service account credential.
    GoogleCredential credential = new GoogleCredential.Builder()
      .setTransport(httpTransport)
      .setJsonFactory(factory)
      .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
      .setServiceAccountScopes(ImmutableList.of(DfareportingScopes.DFAREPORTING))
      .setServiceAccountPrivateKeyFromP12File(new File(PATH_TO_P12_FILE))
      // Set the user you are impersonating (this can be yourself).
      .setServiceAccountUser(ACCOUNT_TO_IMPERSONATE)
      .build();

    credential.refreshToken();
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
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    // Build service account credential.
    Credential credential = getServiceAccountCredential(httpTransport, jsonFactory);

    Dfareporting reporting = new Dfareporting.Builder(httpTransport, jsonFactory, credential)
      .setApplicationName("dfareporting-java-samples")
      .build();

    runExample(reporting);
  }
}
