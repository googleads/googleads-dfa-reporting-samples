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
 * This example assigns a given creative to a given campaign. Note that both
 * the creative and campaign must be associated with the same advertiser.
 */
class AssignCreativeToCampaign extends BaseExample {
  /**
   * {@inheritdoc}
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
              'required' => true)
    );
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Assigning creative ID %s to campaign ID %s</h2>',
        $values['creative_id'], $values['campaign_id']
    );

    $association =
        new Google_Service_Dfareporting_CampaignCreativeAssociation();
    $association->setCreativeId($values['creative_id']);

    $result = $this->service->campaignCreativeAssociations->insert(
        $values['user_profile_id'], $values['campaign_id'], $association);

    printf('Successfully associated creative ID %s with campaign ID %s.',
        $result->getCreativeId(), $values['campaign_id']);
  }

  /**
   * {@inheritdoc}
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Assign Creative To Campaign';
  }
}
