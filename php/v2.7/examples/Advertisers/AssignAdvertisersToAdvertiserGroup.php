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
 * This example assigns an advertiser to an advertiser group.
 *
 * CAUTION: An advertiser that has campaigns associated with it cannot be
 * removed from an advertiser group once assigned.
 */
class AssignAdvertisersToAdvertiserGroup extends BaseExample {
  /**
   * {@inheritdoc}
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
            ['name' => 'advertiser_group_id',
             'display' => 'Advertiser Group ID',
             'required' => true]];
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Assigning advertiser ID %s to advertiser group ID %s</h2>',
        $values['advertiser_id'], $values['advertiser_group_id']
    );

    $advertiser = new Google_Service_Dfareporting_Advertiser();
    $advertiser->setAdvertiserGroupId($values['advertiser_group_id']);

    $result = $this->service->advertisers->patch($values['user_profile_id'],
        $values['advertiser_id'], $advertiser);

    $this->printResultsTable('Advertiser', [$result]);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Assign Advertiser To Advertiser Group';
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['id' => 'Advertiser ID',
            'name' => 'Advertiser Name',
            'advertiserGroupId' => 'Advertiser Group ID'];
  }
}
