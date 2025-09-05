<?php
/*
 * Copyright 2015 Google Inc.
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

/**
 * Implements the examples execution flow.
 * Load this file with no parameters to get the list of available examples.
 */
require_once __DIR__ . '/vendor/autoload.php';
require_once 'htmlHelper.php';

session_start();

// Configure token storage on disk.
// If you want to store refresh tokens in a local disk file, set this to true.
define('STORE_ON_DISK', false, true);
define('TOKEN_FILENAME', 'tokens.dat', true);

// Set up authentication
$client = new Google_Client();
$client->setApplicationName(
    'DCM/DFA Reporting and Trafficking API PHP Samples'
);
$client->addScope(Google_Service_Dfareporting::DFAREPORTING);
$client->addScope(Google_Service_Dfareporting::DFATRAFFICKING);
$client->addScope(Google_Service_Dfareporting::DDMCONVERSIONS);
$client->setAccessType('offline');

if (getenv('GOOGLE_APPLICATION_CREDENTIALS')) {
    $client->useApplicationDefaultCredentials();
} else {
    // Be sure to replace the contents of client_secrets.json with your developer
    // credentials.
    $client->setAuthConfigFile('client_secrets.json');
}

// Create service.
$service = new Google_Service_Dfareporting($client);

// If we're logging out we just need to clear our local access token.
// Note that this only logs you out of the session. If STORE_ON_DISK is
// enabled and you want to remove stored data, delete the file.
if (isset($_REQUEST['logout'])) {
    unset($_SESSION['access_token']);
}

// If we have a code back from the OAuth 2.0 flow, we need to exchange that
// with the authenticate() function. We store the resultant access token
// bundle in the session (and disk, if enabled), and redirect to this page.
if (isset($_GET['code'])) {
    $client->authenticate($_GET['code']);
    // Note that "getAccessToken" actually retrieves both the access and refresh
    // tokens, assuming both are available.
    $_SESSION['access_token'] = $client->getAccessToken();
    if (STORE_ON_DISK) {
        file_put_contents(TOKEN_FILENAME, json_encode($_SESSION['access_token']));
    }
    $redirect = 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'];
    header('Location: ' . filter_var($redirect, FILTER_SANITIZE_URL));
    exit;
}

// If we have an access token, we can make requests, else we generate an
// authentication URL.
if (isset($_SESSION['access_token']) && $_SESSION['access_token']) {
    $client->setAccessToken($_SESSION['access_token']);
} elseif (STORE_ON_DISK && file_exists(TOKEN_FILENAME) &&
    filesize(TOKEN_FILENAME) > 0) {
    // Note that "setAccessToken" actually sets both the access and refresh token,
    // assuming both were saved.
    $client->setAccessToken(file_get_contents(TOKEN_FILENAME));
    $_SESSION['access_token'] = $client->getAccessToken();
} else {
    // If we're doing disk storage, generate a URL that forces user approval.
    // This is the only way to guarantee we get back a refresh token.
    if (STORE_ON_DISK) {
        $client->setApprovalPrompt('force');
    }
    $authUrl = $client->createAuthUrl();
}

$pageTitle = sprintf(
    'DCM/DFA Reporting and Trafficking API %s PHP usage samples',
    $service->version
);
printHtmlHeader($pageTitle);

if (isset($authUrl)) {
    // No access token found, show the link to generate one
    printf("<a class='login' href='%s'>Login!</a>", $authUrl);
} else {
    print "<a class='logout' href='?logout'>Logout</a>";
}

if ($client->getAccessToken()) {
    // If the action is set, dispatch the action if supported
    if (isset($_GET['action'])) {
        $action = decodeActionString($_GET['action']);
        if (!isValidAction($action)) {
            die('Unsupported action: ' . $_GET['action'] . "\n");
        }

        displayAction($action);
    } else {
        // Show the list of links to supported actions.
        printExamplesIndex(getSupportedActions());
        printHtmlFooter();
    }

    // Note that we re-store the access_token bundle, just in case anything
    // changed during the request - the main thing that might happen here is the
    // access token itself is refreshed if the application has offline access.
    $_SESSION['access_token'] = $client->getAccessToken();
}

/**
 * Displays the requested action.
 */
function displayAction($action)
{
    global $service;

    // Render the required action.
    include_once 'examples/' . $action[0] . '/' . $action[1] . '.php';
    $class = $action[1];
    $example = new $class($service);
    printHtmlHeader($example->getName());
    try {
        $example->execute();
    } catch (Google_Exception $ex) {
        print_r($ex);
        print 'An error as occurred while calling the example:<br/>';
        print $ex->getMessage();
    }
    printSampleHtmlFooter();
}

/**
 * Determines whether the requested action is in our list of supported actions.
 */
function isValidAction($action)
{
    $actions = getSupportedActions();

    if (array_key_exists($action[0], $actions)) {
        $section = $actions[$action[0]];
        if (in_array($action[1], $section)) {
            return true;
        }
    }

    return false;
}

/**
 * Decodes an action string passed as a URL parameter into a section and action
 * pair.
 */
function decodeActionString($actionString)
{
    $parts = explode(':', $actionString);
    if (count($parts) != 2) {
        die('Invalid action specified.');
    }

    return $parts;
}

/**
 * Builds an array containing the supported actions, separated into sections.
 */
function getSupportedActions()
{
    $actions = [];

    foreach (glob('examples/*/*.php') as $file) {
        $dir = dirname($file);
        $section = substr($dir, strrpos($dir, '/') + 1);

        if (!array_key_exists($section, $actions)) {
            $actions[$section] = [];
        }

        $actions[$section][] = basename($file, '.php');
    }

    return $actions;
}
