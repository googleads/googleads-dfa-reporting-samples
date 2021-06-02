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
 * This example gets all DFA user profiles associated with the user's Google
 * Account.
 */
class GetAllUserProfiles extends BaseExample
{
    /**
     * {@inheritdoc}
     * @see BaseExample::run()
     */
    public function run()
    {
        $result = $this->service->userProfiles->listUserProfiles();

        print '<h2>Listing of User Profiles associated with your account</h2>';

        $this->printResultsTable('User Profiles', $result['items']);
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::getName()
     * @return string
     */
    public function getName()
    {
        return 'Get All User Profiles';
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['profileId' => 'Profile ID',
                'userName' => 'User Name',
                'accountId' => 'Account ID',
                'accountName' => 'Account Name',
                'subAccountId' => 'Subaccount ID',
                'subAccountName' => 'Subaccount Name'];
    }
}
