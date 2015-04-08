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
 * This example creates a creative field associated with a given advertiser.
 * To get an advertiser ID, run GetAdvertisers.
 *
 * Tags: creativeFields.insert
 *
 * @author api.jimper@gmail.com (Jonathon Imperiosi)
 */
class CreateCreativeField extends BaseExample {
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
        array('name' => 'field_name',
              'display' => 'Creative Field Name',
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
        '<h2>Creating creative field with name "%s"</h2>',
        $values['field_name']
    );

    $field = new Google_Service_Dfareporting_CreativeField();
    $field->setAdvertiserId($values['advertiser_id']);
    $field->setName($values['field_name']);

    $result = $this->service->creativeFields->insert(
        $values['user_profile_id'], $field
    );

    $this->printResultsTable('Creative field created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Creative Field';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Creative Field ID',
        'name' => 'Creative Field Name'
    );
  }
}
