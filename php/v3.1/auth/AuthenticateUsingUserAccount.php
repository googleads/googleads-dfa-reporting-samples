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
 * a user account, via the OAuth 2.0 installed application fow.
 *
 * This example is written to be run as a command line application, not as a
 * webpage.
 */
class AuthenticateUsingUserAccount {
  // This redirect URI allows you to copy the token from the success screen.
  const OAUTH_REDIRECT_URI = 'urn:ietf:wg:oauth:2.0:oob';

  // Location where authorization credentials will be cached.
  const TOKEN_STORE = 'auth-sample.dat';

  // The OAuth 2.0 scopes to request.
  private static $OAUTH_SCOPES = [
      Google_Service_Dfareporting::DFAREPORTING
  ];

  public function run($pathToJsonFile) {
    // Create an authenticated client object.
    $client = $this->createAuthenticatedClient(
        $pathToJsonFile,
        self::TOKEN_STORE
    );

    // Create a Dfareporting service object.
    $service = new Google_Service_Dfareporting($client);

    $this->getUserProfiles($service);
  }

  private function createAuthenticatedClient($pathToJsonFile, $tokenStore) {
    // Create a Google_Client instance.
    //
    // Note: application name should be replaced with a value that identifies
    // your application. Suggested format is "MyCompany-ProductName".
    $client = new Google_Client();
    $client->setAccessType('offline');
    $client->setApplicationName('PHP installed app sample');
    $client->setRedirectUri(self::OAUTH_REDIRECT_URI);
    $client->setScopes(self::$OAUTH_SCOPES);

    // Load the client secrets file.
    $client->setAuthConfig($pathToJsonFile);

    // Try to load cached credentials from the token store. Using a token store
    // allows auth credentials to be cached, so they survive multiple runs of
    // the application. This avoids prompting the user for authorization every
    // time the access token expires, by remembering the refresh token.
    if (file_exists($tokenStore) && filesize($tokenStore) > 0) {
      $client->setAccessToken(file_get_contents($tokenStore));
    } else {
      // If no cached credentials were found, authorize and persist
      // credentials to the token store.
      print 'Open this URL in your browser and authorize the application.';
      printf("\n\n%s\n\n", $client->createAuthUrl());
      print 'Enter the authorization code: ';
      $code = trim(fgets(STDIN));
      $client->authenticate($code);

      file_put_contents($tokenStore, json_encode($client->getAccessToken()));
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

if ($argc !== 2) {
  printf("Usage: %s /path/to/client_secrets.json\n", $argv[0]);
} else {
  $sample = new AuthenticateUsingUserAccount();
  $sample->run($argv[1]);
}