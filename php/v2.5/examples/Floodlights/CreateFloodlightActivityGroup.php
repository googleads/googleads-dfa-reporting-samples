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
 * This example creates a new activity group for a given floodlight
 * configuration. To get a floodlight tag configuration ID, run GetAdvertisers.
 */
class CreateFloodlightActivityGroup extends BaseExample {
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
        array('name' => 'group_name',
              'display' => 'Floodlight Activity Group Name',
              'required' => true),
        array('name' => 'configuration_id',
              'display' => 'Floodlight Configuration (Advertiser) ID',
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
        '<h2>Creating floodlight activity group with name "%s"</h2>',
        $values['group_name']
    );

    $group = new Google_Service_Dfareporting_FloodlightActivityGroup();
    $group->setFloodlightConfigurationId($values['configuration_id']);
    $group->setName($values['group_name']);
    $group->setType('COUNTER');

    $result = $this->service->floodlightActivityGroups->insert(
        $values['user_profile_id'], $group
    );

    $this->printResultsTable('Floodlight activity group created.',
        array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Floodlight Activity Group';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Floodlight Activity Group ID',
        'name' => 'Floodlight Activity Group Name',
        'floodlightConfigurationId' => 'Floodlight Configuration ID'
    );
  }
}
