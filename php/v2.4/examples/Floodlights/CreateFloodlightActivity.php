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
 * This example creates a floodlight activity in a given activity group. To
 * create an activity group, run CreateFloodlightActivityGroup.
 */
class CreateFloodlightActivity extends BaseExample {
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
        array('name' => 'activity_group_id',
              'display' => 'Floodlight Activity Group ID',
              'required' => true),
        array('name' => 'activity_name',
              'display' => 'Floodlight Activity Name',
              'required' => true),
        array('name' => 'url',
              'display' => 'Expected URL',
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
        '<h2>Creating floodlight activity with name "%s" under group ID'
        . ' %s</h2>', $values['activity_name'], $values['activity_group_id']
    );

    $activity = new Google_Service_Dfareporting_FloodlightActivity();
    $activity->setCountingMethod('STANDARD_COUNTING');
    $activity->setExpectedUrl($values['url']);
    $activity->setFloodlightActivityGroupId($values['activity_group_id']);
    $activity->setName($values['activity_name']);

    $result = $this->service->floodlightActivities->insert(
        $values['user_profile_id'], $activity
    );

    $this->printResultsTable('Floodlight activity created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Floodlight Activity';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Floodlight Activity ID',
        'name' => 'Floodlight Activity Name'
    );
  }
}
