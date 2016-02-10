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
 * This example creates a placement group in a given campaign. Requires the
 * site ID and campaign ID in which the placement group will be created into.
 * To create a campaign, run CreateCampaign. To get a site ID, run GetSite.
 */
class CreatePlacementGroup extends BaseExample {
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
        array('name' => 'site_id',
              'display' => 'Site ID',
              'required' => true),
        array('name' => 'group_name',
              'display' => 'Placement Group Name',
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
        '<h2>Creating placement group with name "%s" under campaign ID %s</h2>',
        $values['group_name'], $values['campaign_id']
    );

    // Retrieve the campaign.
    $campaign = $this->service->campaigns->get(
        $values['user_profile_id'], $values['campaign_id']);

    $group = new Google_Service_Dfareporting_PlacementGroup();
    $group->setCampaignId($values['campaign_id']);
    $group->setName($values['group_name']);
    $group->setPlacementGroupType('PLACEMENT_PACKAGE');
    $group->setSiteId($values['site_id']);

    // Set the pricing schedule for the placement group.
    $pricing_schedule = new Google_Service_Dfareporting_PricingSchedule();
    $pricing_schedule->setEndDate($campaign->getEndDate());
    $pricing_schedule->setPricingType('PRICING_TYPE_CPM');
    $pricing_schedule->setStartDate($campaign->getStartDate());
    $group->setPricingSchedule($pricing_schedule);

    // Insert the placement group.
    $result = $this->service->placementGroups->insert(
        $values['user_profile_id'], $group
    );

    $this->printResultsTable('Placement group created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Placement Group';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Placement Group ID',
        'name' => 'Placement Group Name'
    );
  }
}
