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
 * This example displays the change logs of a specified advertiser object.
 *
 * A similar pattern can be applied to get change logs for many other object
 * types.
 */
class GetChangeLogsForAdvertiser extends BaseExample
{
    /**
     * (non-PHPdoc)
     * @see BaseExample::getInputParameters()
     * @return array
     */
    protected function getInputParameters()
    {
        return [['name' => 'user_profile_id',
                 'display' => 'User Profile ID',
                 'required' => true],
                ['name' => 'advertiser_id',
                 'display' => 'Advertiser ID',
                 'required' => true]];
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        printf(
            '<h2>Listing all change logs for advertiser ID %s</h2>',
            $values['advertiser_id']
        );

        $response = null;
        $pageToken = null;

        $this->printResultsTableHeader('Change Logs');

        do {
            // Create and execute the change logs list request.
            $response = $this->service->changeLogs->listChangeLogs(
                $values['user_profile_id'],
                ['objectIds' => [$values['advertiser_id']],
                 'objectType' => 'OBJECT_ADVERTISER',
                 'pageToken' => $pageToken]
            );

            foreach ($response->getChangeLogs() as $log) {
                $this->printResultsTableRow($log);
            }

            // Update the next page token.
            $pageToken = $response->getNextPageToken();
        } while (!empty($response->getChangeLogs()) && !empty($pageToken));

        $this->printResultsTableFooter();
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Get All Change Logs For Advertiser';
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['action' => 'Action',
                'fieldName' => 'Field',
                'oldValue' => 'Old Value',
                'newValue' => 'New Value'];
    }
}
