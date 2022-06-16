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
 * This example displays floodlight activity groups for a given advertiser.
 *
 * To create an advertiser, run CreateAdvertiser.
 */
class GetFloodlightActivityGroups extends BaseExample
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
            '<h2>Listing all floodlight activity groups for advertiser ID %s</h2>',
            $values['advertiser_id']
        );

        $response = null;
        $pageToken = null;

        $this->printResultsTableHeader('Floodlight Activity Groups');

        $activityGroupService = $this->service->floodlightActivityGroups;

        do {
            // Create and execute the floodlight activity groups list request.
            $response = $activityGroupService->listFloodlightActivityGroups(
                $values['user_profile_id'],
                ['advertiserId' => $values['advertiser_id'],
                 'pageToken' => $pageToken]
            );

            foreach ($response->getFloodlightActivityGroups() as $groups) {
                $this->printResultsTableRow($groups);
            }

            // Update the next page token.
            $pageToken = $response->getNextPageToken();
        } while (!empty($response->getFloodlightActivityGroups()) && !empty($pageToken));

        $this->printResultsTableFooter();
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Get All Floodlight Activity Groups';
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['id' => 'Floodlight Activity Group ID',
                'name' => 'Floodlight Activity Group Name',
                'floodlightConfigurationId' => 'Floodlight Configuration ID'];
    }
}
