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
 * This example lists the compatible fields for the specified report.
 *
 * Tags: reports.compatibleFields.query
 *
 * @author Jonathon Imperiosi (jimper@google.com)
 */
class GetCompatibleFields extends BaseExample {
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
      '<h2>Listing compatible fields for report with ID %s</h2>',
      $values['report_id']
    );

    // Retrieve the specified report
    $report = $this->service->reports->get($values['user_profile_id'],
      $values['report_id']);

    // Look up the compatible fields for this report
    $fields = $this->service->reports_compatibleFields->query(
      $values['user_profile_id'], $report);

    // Print the compatible dimensions
    $this->printResultsTable('Dimensions',
      $fields['reportCompatibleFields']['dimensions']);

    // Print the compatible metrics
    $this->printResultsTable('Metrics',
      $fields['reportCompatibleFields']['metrics']);

    // Print the compatible dimension filters
    $this->printResultsTable('Dimension Filters',
      $fields['reportCompatibleFields']['dimensionFilters']);

    // Print the compatible pivoted activity metrics
    $this->printResultsTable('Pivoted Activity Metrics',
      $fields['reportCompatibleFields']['pivotedActivityMetrics']);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Get Compatible Fields';
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
      'name' => 'Field Name'
    );
  }
}
