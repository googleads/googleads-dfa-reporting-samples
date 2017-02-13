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
 * This example creates a simple Floodlight report for the given Floodlight
 * Configuration ID.
 */
class CreateFloodlightReport extends BaseExample {
  /**
   * {@inheritdoc}
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'floodlight_config_id',
             'display' => 'Floodlight Configuration ID',
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

    printf(
        '<h2>Creating a new Floodlight report for Floodlight config ID %s</h2>',
        $values['floodlight_config_id']
    );

    // Create a report.
    $report = new Google_Service_Dfareporting_Report();
    $report->setName('API Floodlight Report: Floodlight ID ' .
        $values['floodlight_config_id']);
    $report->setFileName('api_floodlight_report_files');
    $report->setType('FLOODLIGHT');

    // Create Floodlight criteria for the report.
    $dateRange = new Google_Service_Dfareporting_DateRange();
    $dateRange->setStartDate($values['start_date']);
    $dateRange->setEndDate($values['end_date']);

    $floodlightConfigDimension =
        new Google_Service_Dfareporting_SortedDimension();
    $floodlightConfigDimension->setName('dfa:floodlightConfigId');
    $activityDimension = new Google_Service_Dfareporting_SortedDimension();
    $activityDimension->setName('dfa:activity');
    $advDimension = new Google_Service_Dfareporting_SortedDimension();
    $advDimension->setName('dfa:advertiser');

    $filter = new Google_Service_Dfareporting_DimensionValue();
    $filter->setDimensionName('dfa:floodlightConfigId');
    $filter->setMatchType('EXACT');
    $filter->setValue($values['floodlight_config_id']);

    $criteria = new Google_Service_Dfareporting_ReportFloodlightCriteria();
    $criteria->setDateRange($dateRange);
    $criteria->setDimensions([$floodlightConfigDimension,
        $activityDimension, $advDimension]);
    $criteria->setMetricNames([
        'dfa:activityClickThroughConversions',
        'dfa:activityClickThroughRevenue',
        'dfa:activityViewThroughConversions',
        'dfa:activityViewThroughRevenue'
    ]);
    $criteria->setDimensionFilters([$filter]);

    $report->setFloodlightCriteria($criteria);

    $result = $this->service->reports->insert($values['user_profile_id'],
        $report);
    $this->printResultsTable('Floodlight Report', [$result]);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Floodlight Report';
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
