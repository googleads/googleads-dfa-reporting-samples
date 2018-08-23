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
 * An end-to-end example of how to create a standard report.
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
             'required' => true]];
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    print '<h2>Creating a new standard report</h2>';
    $this->flush();

    $userProfileId = $values['user_profile_id'];

    // 1. Create a report resource.
    $report = $this->createReportResource();

    // 2. Define the report criteria.
    $this->defineReportCriteria($report);

    // 3. (optional) Look up compatible fields.
    $this->findCompatibleFields($userProfileId, $report);

    // 4. Add dimension filters to the report criteria.
    $this->addDimensionFilters($userProfileId, $report);

    // 5. Save the report resource.
    $report = $this->insertReportResource($userProfileId, $report);

    $this->printResultsTable('Standard Report', [$report]);
  }

  private function createReportResource() {
    $report = new Google_Service_Dfareporting_Report();

    // Set the required fields "name" and "type".
    $report->setName('Example standard report');
    $report->setType('STANDARD');

    // Set optional fields.
    $report->setFileName('example_report');
    $report->setFormat('CSV');

    return $report;
  }

  private function defineReportCriteria($report) {
    // Define a date range to report on. This example uses explicit start and
    // end dates to mimic the "LAST_30_DAYS" relative date range.
    $dateRange = new Google_Service_Dfareporting_DateRange();
    $dateRange->setStartDate(
        date('Y-m-d', mktime(0, 0, 0, date('m'), date('d') - 30, date('Y'))));
    $dateRange->setEndDate(date('Y-m-d'));

    // Create a report criteria.
    $dimension = new Google_Service_Dfareporting_SortedDimension();
    $dimension->setName('dfa:advertiser');

    $criteria = new Google_Service_Dfareporting_ReportCriteria();
    $criteria->setDateRange($dateRange);
    $criteria->setDimensions([$dimension]);
    $criteria->setMetricNames(['dfa:clicks', 'dfa:impressions']);

    // Add the criteria to the report resource.
    $report->setCriteria($criteria);
  }

  private function findCompatibleFields($userProfileId, $report) {
    $fields = $this->service->reports_compatibleFields->query($userProfileId,
        $report);

    $reportFields = $fields->getReportCompatibleFields();

    if (!empty($reportFields->getDimensions())) {
      // Add a compatible dimension to the report.
      $dimension = $reportFields->getDimensions()[0];
      $sortedDimension = new Google_Service_Dfareporting_SortedDimension();
      $sortedDimension->setName($dimension->getName());
      $report->getCriteria()->setDimensions(
          array_merge($report->getCriteria()->getDimensions(),
              [$sortedDimension]));
    } else if (!empty($reportFields->getMetrics())) {
      // Add a compatible metric to the report.
      $metric = $reportFields->getMetrics()[0];
      $report->getCriteria()->setMetricNames(
          array_merge($report->getCriteria()->getMetricNames(),
              [$metric->getName()]));
    }
  }

  private function addDimensionFilters($userProfileId, $report) {
    // Query advertiser dimension values for report run dates.
    $request = new Google_Service_Dfareporting_DimensionValueRequest();
    $request->setStartDate(
        $report->getCriteria()->getDateRange()->getStartDate());
    $request->setEndDate(
        $report->getCriteria()->getDateRange()->getEndDate());
    $request->setDimensionName('dfa:advertiser');

    $values =
        $this->service->dimensionValues->query($userProfileId, $request);

    if (!empty($values->getItems())) {
      // Add a value as a filter to the report criteria.
      $report->getCriteria()->setDimensionFilters([$values->getItems()[0]]);
    }
  }

  private function insertReportResource($userProfileId, $report) {
    $insertedReport =
        $this->service->reports->insert($userProfileId, $report);
    return $insertedReport;
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
