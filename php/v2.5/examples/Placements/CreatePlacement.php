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
 * This example creates a placement in a given campaign. Requires a site ID
 * and ID of the campaign in which the placement will be created. To create a
 * campaign, run CreateCampaign. To get a site ID, run GetSite. To get a size
 * ID, run GetSize.
 */
class CreatePlacement extends BaseExample {
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
        array('name' => 'size_id',
              'display' => 'Size ID',
              'required' => true),
        array('name' => 'placement_name',
              'display' => 'Placement Name',
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
        '<h2>Creating placement with name "%s" under campaign ID %s</h2>',
        $values['placement_name'], $values['campaign_id']
    );

    // Retrieve the campaign.
    $campaign = $this->service->campaigns->get(
        $values['user_profile_id'], $values['campaign_id']);

    $placement = new Google_Service_Dfareporting_Placement();
    $placement->setCampaignId($values['campaign_id']);
    $placement->setCompatibility('DISPLAY');
    $placement->setName($values['placement_name']);
    $placement->setPaymentSource('PLACEMENT_AGENCY_PAID');
    $placement->setSiteId($values['site_id']);
    $placement->setTagFormats(array('PLACEMENT_TAG_STANDARD'));

    // Set the size of the placement.
    $size = new Google_Service_Dfareporting_Size();
    $size->setId($values['size_id']);
    $placement->setSize($size);

    // Set the pricing schedule for the placement.
    $pricing_schedule = new Google_Service_Dfareporting_PricingSchedule();
    $pricing_schedule->setEndDate($campaign->getEndDate());
    $pricing_schedule->setPricingType('PRICING_TYPE_CPM');
    $pricing_schedule->setStartDate($campaign->getStartDate());
    $placement->setPricingSchedule($pricing_schedule);

    // Insert the placement.
    $result = $this->service->placements->insert(
        $values['user_profile_id'], $placement
    );

    $this->printResultsTable('Placement created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Placement';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Placement ID',
        'name' => 'Placement Name'
    );
  }
}
