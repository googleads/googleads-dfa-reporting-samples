<?php
/*
 * Copyright 2016 Google Inc.
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
 * This example creates a basic targeting template associated with a given
 * advertiser. To get an advertiser ID, run GetAdvertisers.
 */
class CreateTargetingTemplate extends BaseExample
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
                 'required' => true],
                ['name' => 'template_name',
                 'display' => 'Template Name',
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
            '<h2>Creating targeting template "%s" for advertiser ID %s</h2>',
            $values['template_name'],
            $values['advertiser_id']
        );

        // Create the targeting template.
        $template = new Google_Service_Dfareporting_TargetingTemplate();
        $template->setAdvertiserId($values['advertiser_id']);
        $template->setName($values['template_name']);

        // Configure the template to serve ads on Monday, Wednesday, and Friday from
        // 9 to 10am and 3 to 5pm.
        $targeting = new Google_Service_Dfareporting_DayPartTargeting();
        $targeting->setDaysOfWeek(['MONDAY', 'WEDNESDAY', 'FRIDAY']);
        $targeting->setHoursOfDay([9, 15, 16]);
        $targeting->setUserLocalTime(true);
        $template->setDayPartTargeting($targeting);

        $result = $this->service->targetingTemplates->insert(
            $values['user_profile_id'],
            $template
        );

        $this->printResultsTable('Targeting template created.', [$result]);
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Create Targeting Template';
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['id' => 'Template ID',
                'name' => 'Template Name'];
    }
}
