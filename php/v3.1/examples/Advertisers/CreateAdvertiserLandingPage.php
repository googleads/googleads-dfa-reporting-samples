<?php
/*
 * Copyright 2017 Google Inc.
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
 * This example creates an advertiser landing page.
 */
class CreateAdvertiserLandingPage extends BaseExample {
  /**
   * (non-PHPdoc)
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
            ['name' => 'landing_page_name',
             'display' => 'Landing Page Name',
             'required' => true],
            ['name' => 'landing_page_url',
             'display' => 'Landing Page URL',
             'required' => true]];
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Creating advertiser landing page for advertiser %d</h2>',
        $values['advertiser_id']
    );

    $landingPage = new Google_Service_Dfareporting_LandingPage();
    $landingPage->setAdvertiserId($values['advertiser_id']);
    $landingPage->setArchived(false);
    $landingPage->setName($values['landing_page_name']);
    $landingPage->setUrl($values['landing_page_url']);

    $result = $this->service->advertiserLandingPages->insert(
        $values['user_profile_id'], $landingPage);

    $this->printResultsTable('Advertiser landing page created.', [$result]);
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Advertiser Landing Page';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['id' => 'Advertiser Landing Page ID',
            'name' => 'Advertiser Landing Page Name',
            'url' => 'Advertiser Landing Page URL'];
  }
}
