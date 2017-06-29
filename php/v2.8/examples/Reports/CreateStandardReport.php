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
 * This example creates a simple standard report for the given advertiser.
 */
class CreateStandardReport extends BaseExample {
  /**
   * {@inheritdoc}
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'advertiser_id',
             'display' => 'Advertiser ID',
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

    printf('<h2>Creating a new standard report for advertiser %s</h2>',
        $values['advertiser_id']);

    // Create a report.
    $report = new Google_Service_Dfareporting_Report();
    $report->setName('API Report: Advertiser ' . $values['advertiser_id']);
    $report->setFileName('api_report_files');
    $report->setType('STANDARD');

    // Create criteria for the report.
    $dateRange = new Google_Service_Dfareporting_DateRange();
    $dateRange->setStartDate($values['start_date']);
    $dateRange->setEndDate($values['end_date']);

    $dimension = new Google_Service_Dfareporting_SortedDimension();
    $dimension->setName('dfa:advertiser');

    $filter = new Google_Service_Dfareporting_DimensionValue();
    $filter->setDimensionName('dfa:advertiser');
    $filter->setId($values['advertiser_id']);
    $filter->setMatchType('EXACT');

    $criteria = new Google_Service_Dfareporting_ReportCriteria();
    $criteria->setDateRange($dateRange);
    $criteria->setDimensions([$dimension]);
    $criteria->setMetricNames(['dfa:clicks', 'dfa:impressions']);
    $criteria->setDimensionFilters([$filter]);

    $report->setCriteria($criteria);

    $result = $this->service->reports->insert($values['user_profile_id'],
        $report);
    $this->printResultsTable('Standard Report', [$result]);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Standard Report';
  }
  /**
   * {@inheritdoc}
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['id' => 'Report ID',
            'name' => 'Report Name'];
  }
}
