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
 * This example creates a remarketing list for a given advertiser and floodlight
 * activity, using a custom rule.
 *
 * Note: this sample assumes that the floodlight activity specified has a U1
 * custom floodlight variable.
 */
class CreateRemarketingList extends BaseExample {
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
        array('name' => 'floodlight_activity_id',
              'display' => 'Floodlight Activity ID',
              'required' => true),
        array('name' => 'remarketing_list_name',
              'display' => 'Remarketing List Name',
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
        '<h2>Creating remarketing list with name "%s"</h2>',
        $values['remarketing_list_name']
    );

    // Create the remarketing list.
    $list = new Google_Service_Dfareporting_RemarketingList();
    $list->setActive(true);
    $list->setAdvertiserId($values['advertiser_id']);
    $list->setLifeSpan(30);
    $list->setName($values['remarketing_list_name']);

    // Create a list population term.
    // This term matches all visitors with a U1 value exactly matching
    // "test_value".
    $term = new Google_Service_Dfareporting_ListPopulationTerm();
    $term->setOperator('STRING_EQUALS');
    $term->setType('CUSTOM_VARIABLE_TERM');
    $term->setValue('test_value');
    $term->setVariableName('U1');

    // Add the term to a list population clause.
    $clause = new Google_Service_Dfareporting_ListPopulationClause();
    $clause->setTerms(array($term));

    // Add the clause to a list population rule.
    // This rule will target all visitors who trigger the specified floodlight
    // activity and satisfy the custom rule defined in the list population term.
    $rule = new Google_Service_Dfareporting_ListPopulationRule();
    $rule->setFloodlightActivityId($values['floodlight_activity_id']);
    $rule->setListPopulationClauses(array($clause));

    $list->setListPopulationRule($rule);

    // Insert the remarketing list.
    $result = $this->service->remarketingLists->insert(
        $values['user_profile_id'],
        $list
    );

    $this->printResultsTable('Remarketing list created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Remarketing List';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Remarketing List ID',
        'name' => 'Remarketing List Name'
    );
  }
}
