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
 * This example creates a rotation group ad in a given campaign. Start and end
 * date for the ad must be within campaign start and end dates. To create
 * creatives, run one of the Create*Creative examples. To get available
 * placements, run GetPlacements.
 */
class CreateRotationGroup extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return array(
        array('name' => 'user_profile_id',
              'display' => 'User Profile ID',
              'required' => true),
        array('name' => 'campaign_id',
              'display' => 'Campaign ID',
              'required' => true),
        array('name' => 'creative_id',
              'display' => 'Creative ID',
              'required' => true),
        array('name' => 'placement_id',
              'display' => 'Placement ID',
              'required' => true),
        array('name' => 'ad_name',
              'display' => 'Ad Name',
              'required' => true)
    );
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Creating rotation group ad with name "%s" for placement ID'
        . ' %s</h2>', $values['ad_name'], $values['placement_id']
    );

    // Retrieve the campaign.
    $campaign = $this->service->campaigns->get($values['user_profile_id'],
        $values['campaign_id']);

    // Create a click-through URL.
    $url = new Google_Service_Dfareporting_ClickThroughUrl();
    $url->setDefaultLandingPage(true);

    // Create a creative assignment.
    $creativeAssignment =
        new Google_Service_Dfareporting_CreativeAssignment();
    $creativeAssignment->setActive(true);
    $creativeAssignment->setCreativeId($values['creative_id']);
    $creativeAssignment->setClickThroughUrl($url);

    // Create a placement assignment.
    $placementAssignment =
        new Google_Service_Dfareporting_PlacementAssignment();
    $placementAssignment->setActive(true);
    $placementAssignment->setPlacementId($values['placement_id']);

    // Create a creative rotation.
    $creativeRotation = new Google_Service_Dfareporting_CreativeRotation();
    $creativeRotation->setCreativeAssignments(array($creativeAssignment));

    // Create a delivery schedule.
    $deliverySchedule = new Google_Service_Dfareporting_DeliverySchedule();
    $deliverySchedule->setImpressionRatio(1);
    $deliverySchedule->SetPriority('AD_PRIORITY_01');

    $startDate = new DateTime('today');
    $endDate = new DateTime($campaign->getEndDate());

    // Create a rotation group.
    $ad = new Google_Service_Dfareporting_Ad();
    $ad->setActive(true);
    $ad->setCampaignId($values['campaign_id']);
    $ad->setCreativeRotation($creativeRotation);
    $ad->setDeliverySchedule($deliverySchedule);
    $ad->setStartTime($startDate->format('Y-m-d') . 'T23:59:59Z');
    $ad->setEndTime($endDate->format('Y-m-d') . 'T00:00:00Z');
    $ad->setName($values['ad_name']);
    $ad->setPlacementAssignments(array($placementAssignment));
    $ad->setType('AD_SERVING_STANDARD_AD');

    $result = $this->service->ads->insert($values['user_profile_id'], $ad);

    $this->printResultsTable('Rotation group ad created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Rotation Group Ad';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Ad ID',
        'name' => 'Ad Name'
    );
  }
}
