<?php
/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

require_once dirname(__DIR__) . '/vendor/autoload.php';

/**
 * This example demonstrates how to authenticate and make a basic request using
 * a service account.
 *
 * This example is written to be run as a command line application, not as a
 * webpage. An optional Google account email to impersonate may be specified as
 * follows:
 *
 *     AuthenticateUsingServiceAccount.php /path/to/client_secrets.json <email>
 *
 * This optional flag only applies to service accounts which have domain-wide
 * delegation enabled and wish to make API requests on behalf of an account
 * within that domain. Using this flag will not allow you to impersonate a user
 * from a domain that you don't own (e.g., gmail.com).
 */
class AuthenticateUsingServiceAccount {
  // The OAuth 2.0 scopes to request.
  private static $OAUTH_SCOPES = [
      Google_Service_Dfareporting::DFAREPORTING
  ];

  public function run($pathToJsonFile, $email = null) {
    // Create an authenticated client object.
    $client = $this->createAuthenticatedClient($pathToJsonFile, $email);

    // Create a Dfareporting service object.
    $service = new Google_Service_Dfareporting($client);

    $this->getUserProfiles($service);
  }

  private function createAuthenticatedClient($pathToJsonFile, $email) {
    // Create a Google_Client instance.
    //
    // Note: application name should be replaced with a value that identifies
    // your application. Suggested format is "MyCompany-ProductName".
    $client = new Google_Client();
    $client->setApplicationName('PHP service account sample');
    $client->setScopes(self::$OAUTH_SCOPES);

    // Load the service account credentials.
    $client->setAuthConfig($pathToJsonFile);

    // Configure impersonation (if applicable).
    if (!is_null($email)) {
      $client->setSubject($email);
    }

    return $client;
  }

  private function getUserProfiles($service) {
    // Retrieve and print all user profiles for the current authorized user.
    $result = $service->userProfiles->listUserProfiles();
    foreach ($result['items'] as $userProfile) {
      printf(
          "User profile \"%s\" (ID: %d) found for account %d.\n",
          $userProfile->getUserName(),
          $userProfile->getProfileId(),
          $userProfile->getAccountId()
      );
    }
  }
}

if ($argc < 2 || $argc >= 4) {
  printf(
      "Usage: %s /path/to/client_secrets.json [email_to_impersonate]\n",
      $argv[0]
  );
} else {
  $sample = new AuthenticateUsingServiceAccount();

  if ($argc == 2) {
    $sample->run($argv[1]);
  } else {
    $sample->run($argv[1], $argv[2]);
  }
}