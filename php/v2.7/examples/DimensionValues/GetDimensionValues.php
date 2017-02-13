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
require_once dirname(__DIR__) . '/BaseExample.php';

/**
 * This example gets the first page of a particular type of dimension available
 * for reporting in the given date range. You can use a similar workflow to
 * retrieve the values for any dimension.
 *
 * For our examples, you need a "dfa:advertiser" to create a standard report and
 * a "dfa:floodlightConfigId" to create a Floodlight report.
 */
class GetDimensionValues extends BaseExample {
  /**
   * {@inheritdoc}
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'dimension_name',
             'display' => 'Dimension Name',
             'required' => true],
            ['name' => 'start_date',
             'display' => 'Start Date (yyyy-MM-dd)',
             'required' => true],
            ['name' => 'end_date',
             'display' => 'End Date (yyyy-MM-dd)',
             'required' => true]];
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    $dimensionValueRequest =
        new Google_Service_Dfareporting_DimensionValueRequest();
    $dimensionValueRequest->setDimensionName($values['dimension_name']);
    $dimensionValueRequest->setStartDate($values['start_date']);
    $dimensionValueRequest->setEndDate($values['end_date']);

    $dimensionValues = $this->service->dimensionValues->query(
        $values['user_profile_id'],
        $dimensionValueRequest
    );

    printf('<h2>Listing available %s values</h2>', $values['dimension_name']);

    $this->printResultsTable('Dimension Values', $dimensionValues['items']);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Get Dimension Values';
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['value' => 'Dimension Value',
            'id' => 'Dimension ID'];
  }
}
