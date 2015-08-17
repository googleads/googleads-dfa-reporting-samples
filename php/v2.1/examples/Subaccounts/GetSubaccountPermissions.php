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
 * This example displays all of the available permissions for a given
 * subacount.
 *
 * To get a subaccount ID, run GetSubaccounts.
 */
class GetSubaccountPermissions extends BaseExample {
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
        array('name' => 'subaccount_id',
              'display' => 'Subaccount ID',
              'required' => true)
    );
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    $subaccount = $this->service->subaccounts->get(
        $values['user_profile_id'], $values['subaccount_id']
    );

    printf('<h2>Listing all permissions for subaccount "%s"</h2>',
        $subaccount->getName()
    );

    $permissions =
        $this->service->userRolePermissions->listUserRolePermissions(
            $values['user_profile_id'],
            array('ids' => $subaccount->getAvailablePermissionIds())
        );

    $this->printResultsTable('Subaccount Permissions', $permissions);
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Get Subaccount Permissions';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Permission ID',
        'name' => 'Permission Name'
    );
  }
}
