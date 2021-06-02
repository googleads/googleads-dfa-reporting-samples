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
 * This example creates a tracking creative associated with a given advertiser.
 */
class CreateTrackingCreative extends BaseExample
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
            '<h2>Creating tracking creative for advertiser ID %s</h2>',
            $values['advertiser_id']
        );

        $creative = new Google_Service_Dfareporting_Creative();
        $creative->setAdvertiserId($values['advertiser_id']);
        $creative->setName('Test tracking creative');
        $creative->setType('TRACKING_TEXT');

        $result = $this->service->creatives->insert(
            $values['user_profile_id'],
            $creative
        );

        $this->printResultsTable('Tracking creative created.', [$result]);
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public function getName()
    {
        return 'Create Tracking Creative';
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['id' => 'Creative ID',
                'name' => 'Creative Name',
                'type' => 'Creative type'];
    }
}
