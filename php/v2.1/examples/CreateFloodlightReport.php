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
 * This example creates a simple Floodlight report for the given Floodlight
 * Configuration ID.
 *
 * Tags: reports.insert
 *
 * @author api.jimper@gmail.com (Jonathon Imperiosi)
 */
class CreateFloodlightReport extends BaseExample {
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
        array('name' => 'floodlight_config_id',
              'display' => 'Floodlight Configuration ID',
              'required' => true),
        array('name' => 'start_date',
              'display' => 'Start Date (yyyy-MM-dd)',
              'required' => true),
        array('name' => 'end_date',
              'display' => 'End Date (yyyy-MM-dd)',
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
    $date_range = new Google_Service_Dfareporting_DateRange();
    $date_range->setStartDate($values['start_date']);
    $date_range->setEndDate($values['end_date']);

    $floodlight_config_dimension =
        new Google_Service_Dfareporting_SortedDimension();
    $floodlight_config_dimension->setName('dfa:floodlightConfigId');
    $activity_dimension = new Google_Service_Dfareporting_SortedDimension();
    $activity_dimension->setName('dfa:activity');
    $adv_dimension = new Google_Service_Dfareporting_SortedDimension();
    $adv_dimension->setName('dfa:advertiser');

    $criteria = new Google_Service_Dfareporting_ReportFloodlightCriteria();
    $criteria->setDateRange($date_range);
    $criteria->setDimensions(array($floodlight_config_dimension,
        $activity_dimension, $adv_dimension));
    $criteria->setMetricNames(
        array(
            'dfa:activityClickThroughConversions',
            'dfa:activityClickThroughRevenue',
            'dfa:activityViewThroughConversions',
            'dfa:activityViewThroughRevenue'
        )
    );
    $criteria->setDimensionFilters(array($values['floodlight_config_id']));

    $report->setFloodlightCriteria($criteria);

    $result = $this->service->reports->insert($values['user_profile_id'],
        $report);
    $this->printResultsTable('Floodlight Report', array($result));
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
    return array(
        'id' => 'Report ID',
        'name' => 'Report Name'
    );
  }
}
