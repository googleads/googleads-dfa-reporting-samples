<?php
/*
 * Copyright 2014 Google Inc.
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
 *
 * @author Jonathon Imperiosi <jimper@google.com>
 */
require_once __DIR__ . "/vendor/autoload.php";
require_once 'htmlHelper.php';

session_start();

$client = new Google_Client();
$client->setApplicationName('DFA Reporting API PHP Samples');
$client->addScope(Google_Service_Dfareporting::DFAREPORTING);
$client->setAccessType('offline');

// Be sure to replace the contents of client_secrets.json with your developer
// credentials.
$client->setAuthConfigFile('client_secrets.json');

$service = new Google_Service_Dfareporting($client);

// If we're logging out we just need to clear our local access token
if (isset($_REQUEST['logout'])) {
  unset($_SESSION['access_token']);
}

if (isset($_GET['code'])) {
  $client->authenticate($_GET['code']);
  $_SESSION['access_token'] = $client->getAccessToken();
  $redirect = 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'];
  header('Location: ' . filter_var($redirect, FILTER_SANITIZE_URL));
}

if (isset($_SESSION['access_token']) && $_SESSION['access_token']) {
  $client->setAccessToken($_SESSION['access_token']);
} else {
  $authUrl = $client->createAuthUrl();
}

printHtmlHeader('DFA Reporting API PHP usage examples');

if(isset($authUrl)) {
  // No access token found, show the link to generate one
  print "<a class='login' href='$authUrl'>Login!</a>";
} else {
  print "<a class='logout' href='?logout'>Logout</a>";
}

if ($client->getAccessToken()) {
  // If the action is set, dispatch the action if supported
  if (isset($_GET['action'])) {
    $action = $_GET['action'];
    if (!isValidAction($action)) {
      die('Unsupported action: ' . $action . "\n");
    }

    displayAction($action);
  } else {
    // Show the list of links to supported actions.
    printExamplesIndex(getSupportedActions());
    printHtmlFooter();
  }

  // The access token may have been updated.
  $_SESSION['access_token'] = $client->getAccessToken();
}

/**
 * Displays the requested action.
 */
function displayAction($action) {
  global $service;

  // Render the required action.
  include_once 'examples/' . $action . '.php';
  $class = ucfirst($action);
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
function isValidAction($requested_action) {
  $actions = getSupportedActions();

  foreach ($actions as $action => $sub_actions) {
    if (in_array($requested_action, $sub_actions)) {
      return true;
    }
  }

  return false;
}

/**
 * Builds an array containing the supported actions, separated into sections.
 */
function getSupportedActions() {
  return array(
    'DimensionValues' => array('GetDimensionValues'),
    'Files' => array('CheckFileStatus', 'GetAllFiles'),
    'Reports' => array('CreateStandardReport', 'CreateFloodlightReport',
                       'DeleteReport', 'GenerateReportFile', 'GetAllReports'),
    'Reports.compatibleFields' => array('GetCompatibleFields'),
    'Reports.files' => array('DownloadReportFile', 'GetAllReportFiles'),
    'UserProfiles' => array('GetAllUserProfiles')
  );
}
