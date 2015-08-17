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

// Require the base class.
require_once dirname(__DIR__) . "/BaseExample.php";

/**
 * This example downloads the contents of a completed report file.
 */
class DownloadReportFile extends BaseExample {
  /**
   * {@inheritdoc}
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return array(
        array('name' => 'file_url',
              'display' => 'URL of a generated report file',
              'required' => true)
    );
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    print '<h2>Retrieving and printing report file</h2>';

    $access_token = json_decode($_SESSION['access_token'])->access_token;
    $authorization_header = 'Authorization: Bearer ' . $access_token;

    // Download the file using curl.
    $request = curl_init($values['file_url']);
    curl_setopt($request, CURLOPT_HTTPHEADER, array($authorization_header));
    curl_setopt($request, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($request, CURLOPT_FOLLOWLOCATION, true);
    $report_file = curl_exec($request);
    curl_close($request);

    print nl2br($report_file);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Download Report File';
  }
}
