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
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.DfareportingScopes;
import com.google.api.services.dfareporting.model.UserProfileList;

import java.io.FileInputStream;

/**
 * This example shows how to authenticate and make a basic request using a service account.
 */
public class AuthenticateUsingServiceAccount {
  private static final String PATH_TO_JSON_FILE = "ENTER_PATH_TO_JSON_FILE_HERE";
  private static final String EMAIL_TO_IMPERSONATE = "ENTER_EMAIL_TO_IMPERSONATE_HERE";

  private static Credential getServiceAccountCredential(String pathToJsonFile,
      String emailToImpersonate) throws Exception {
    // Generate a credential object from the specified JSON file.
    GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(pathToJsonFile));

    // Update the credential object with appropriate scopes and impersonation info.
    credential = new GoogleCredential.Builder()
        .setTransport(credential.getTransport())
        .setJsonFactory(credential.getJsonFactory())
        .setServiceAccountId(credential.getServiceAccountId())
        .setServiceAccountPrivateKey(credential.getServiceAccountPrivateKey())
        .setServiceAccountScopes(DfareportingScopes.all())
        // Set the email of the user you are impersonating (this can be yourself).
        .setServiceAccountUser(emailToImpersonate)
        .build();

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
    // Build service account credential.
    Credential credential = getServiceAccountCredential(PATH_TO_JSON_FILE, EMAIL_TO_IMPERSONATE);

    Dfareporting reporting = new Dfareporting.Builder(credential.getTransport(),
        credential.getJsonFactory(), credential).setApplicationName("dfareporting-java-samples")
        .build();

    runExample(reporting);
  }
}
