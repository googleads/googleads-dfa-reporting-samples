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
 * This example creates a subaccount in a given DoubleClick Campaign Manager
 * account. To get available permissions, run GetUserRolePermissions.
 */
class CreateSubaccount extends BaseExample {
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
        array('name' => 'account_id',
              'display' => 'Account ID',
              'required' => true),
        array('name' => 'permission_one',
              'display' => 'First Permission ID',
              'required' => true),
        array('name' => 'permission_two',
              'display' => 'Second Permission ID',
              'required' => true),
        array('name' => 'subaccount_name',
              'display' => 'Subaccount Name',
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
        '<h2>Creating subaccount with name "%s" for account ID %s</h2>',
        $values['subaccount_name'], $values['account_id']
    );

    $subaccount = new Google_Service_Dfareporting_Subaccount();
    $subaccount->setName($values['subaccount_name']);
    $subaccount->setAvailablePermissionIds(array(
        $values['permission_one'], $values['permission_two']
    ));

    $result = $this->service->subaccounts->insert(
        $values['user_profile_id'], $subaccount
    );

    $this->printResultsTable('Subaccount created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Subaccount';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Subaccount ID',
        'name' => 'Subaccount Name'
    );
  }
}
