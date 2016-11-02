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
 * This example creates a creative group associated with a given advertiser.
 * To get an advertiser ID, run GetAdvertisers. Valid group numbers are
 * limited to 1 or 2.
 */
class CreateCreativeGroup extends BaseExample {
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
        array('name' => 'group_name',
              'display' => 'Creative Group Name',
              'required' => true),
        array('name' => 'group_number',
              'display' => 'Creative Group Number (1-2)',
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
        '<h2>Creating creative group with name "%s"</h2>',
        $values['group_name']
    );

    $group = new Google_Service_Dfareporting_CreativeGroup();
    $group->setAdvertiserId($values['advertiser_id']);
    $group->setGroupNumber($values['group_number']);
    $group->setName($values['group_name']);

    $result = $this->service->creativeGroups->insert(
        $values['user_profile_id'], $group
    );

    $this->printResultsTable('Creative group created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Creative Group';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Creative Group ID',
        'name' => 'Creative Group Name',
        'groupNumber' => 'Creative Group Number'
    );
  }
}
