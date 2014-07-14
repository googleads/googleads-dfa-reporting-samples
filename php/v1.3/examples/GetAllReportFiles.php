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

// Require the base class.
require_once dirname(__DIR__) . "/BaseExample.php";

/**
 * This example returns all files for the specified report.
 *
 * Tags: reports.files.list
 *
 * @author Jonathon Imperiosi (jimper@google.com)
 */
class GetAllReportFiles extends BaseExample {
  /**
   * {@inheritdoc}
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return array(
      array('name' => 'user_profile_id',
            'display' => 'User Profile ID',
            'required' => true),
      array('name' => 'report_id',
            'display' => 'Report ID',
            'required' => true),
      array('name' => 'page_size',
            'display' => 'Page Size (Max 10)',
            'required' => true)
    );
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
      '<h2>Listing all files for report with ID %s</h2>',
      $values['report_id']
    );

    $this->printResultsTableHeader('Report Files');

    do {
      $files = $this->service->reports_files->listReportsFiles(
        $values['user_profile_id'],
        $values['report_id'],
        array('maxResults' => $values['page_size'], 'pageToken' => $pageToken)
      );

      foreach ($files['items'] as $file) {
        $file['apiUrl'] = $file['urls']['apiUrl'];
        $this->printResultsTableRow($file);
      }

      $pageToken = $files['nextPageToken'];
    } while ($pageToken && !empty($files['items']));

    $this->printResultsTableFooter();
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Get All Report Files';
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
      'id' => 'File ID',
      'fileName' => 'File Name',
      'reportId' => 'Report ID',
      'format' => 'File Format',
      'status' => 'Status',
      'apiUrl' => 'API URL'
    );
  }
}
