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
 * This example creates a user role in a given DoubleClick Campaign Manager
 * subaccount. To get the subaccount ID, run GetSubaccounts. To get the
 * available permissions, run GetUserRolePermissions. To get the parent user
 * role ID, run GetUserRoles.
 */
class CreateUserRole extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'parent_user_role_id',
             'display' => 'Parent User Role ID',
             'required' => true],
            ['name' => 'subaccount_id',
             'display' => 'Subaccount ID',
             'required' => true],
            ['name' => 'permission_one',
             'display' => 'First Permission ID',
             'required' => true],
            ['name' => 'permission_two',
             'display' => 'Second Permission ID',
             'required' => true],
            ['name' => 'user_role_name',
             'display' => 'User Role Name',
             'required' => true]];
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Creating user role with name "%s" under parent role ID %s</h2>',
        $values['user_role_name'], $values['parent_user_role_id']
    );

    $userRole = new Google_Service_Dfareporting_UserRole();
    $userRole->setName($values['user_role_name']);
    $userRole->setParentUserRoleId($values['parent_user_role_id']);
    $userRole->setPermissions(
        [$values['permission_one'], $values['permission_two']]);
    $userRole->setSubaccountId($values['subaccount_id']);

    $result = $this->service->userRoles->insert(
        $values['user_profile_id'], $userRole
    );

    $this->printResultsTable('User role created.', [$result]);
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create User Role';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['id' => 'User Role ID',
            'name' => 'User Role Name'];
  }
}
