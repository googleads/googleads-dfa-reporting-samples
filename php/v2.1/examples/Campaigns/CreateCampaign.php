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
 * This example creates a campaign associated with a given advertiser. To
 * create an advertiser, run CreateAdvertiser.
 *
 * Tags: campaigns.insert
 *
 * @author api.jimper@gmail.com (Jonathon Imperiosi)
 */
class CreateCampaign extends BaseExample {
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
        array('name' => 'advertiser_id',
              'display' => 'Advertiser ID',
              'required' => true),
        array('name' => 'campaign_name',
              'display' => 'Campaign Name',
              'required' => true),
        array('name' => 'landing_page_name',
              'display' => 'Landing Page Name',
              'required' => true),
        array('name' => 'landing_page_url',
              'display' => 'Landing Page URL',
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
        '<h2>Creating campaign with name "%s"</h2>', $values['campaign_name']
    );

    $startDate = new DateTime('today');
    $endDate = new DateTime('+1 month');

    $campaign = new Google_Service_Dfareporting_Campaign();
    $campaign->setAdvertiserId($values['advertiser_id']);
    $campaign->setName($values['campaign_name']);
    $campaign->setStartDate($startDate->format('Y-m-d'));
    $campaign->setEndDate($endDate->format('Y-m-d'));

    $result = $this->service->campaigns->insert(
        $values['user_profile_id'],
        $values['landing_page_name'],
        $values['landing_page_url'],
        $campaign
    );

    $this->printResultsTable('Campaign created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Campaign';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Campaign ID',
        'name' => 'Campaign Name'
    );
  }
}
