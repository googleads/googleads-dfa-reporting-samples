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
 * This example retrieves available creative field values for a given string
 * and displays the names and IDs.
 */
class GetCreativeFieldValues extends BaseExample
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
                ['name' => 'field_id',
                 'display' => 'Creative Field ID',
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
            '<h2>Listing all creative field values for creative field ID %s</h2>',
            $values['field_id']
        );


        $response = null;
        $pageToken = null;

        $this->printResultsTableHeader('Creative Field Values');

        do {
            // Create and execute the creative field values list request.
            $response = $this->service->creativeFieldValues->listCreativeFieldValues(
                $values['user_profile_id'],
                $values['field_id'],
                ['pageToken' => $pageToken]
            );

            foreach ($response->getCreativeFieldValues() as $values) {
                $this->printResultsTableRow($values);
            }

            // Update the next page token.
            $pageToken = $response->getNextPageToken();
        } while (!empty($response->getCreativeFieldValues()) && !empty($pageToken));

        $this->printResultsTableFooter();
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Get All Creative Field Values';
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['id' => 'Creative Field Value ID',
                'value' => 'Creative Field Value'];
    }
}
