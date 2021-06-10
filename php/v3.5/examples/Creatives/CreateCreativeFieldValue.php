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
 * This example creates a creative field value associated with a given creative
 * field. To get the creative field ID, run GetCreativeFields.
 */
class CreateCreativeFieldValue extends BaseExample
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
                ['name' => 'creative_field_id',
                 'display' => 'Creative Field ID',
                 'required' => true],
                ['name' => 'field_value',
                 'display' => 'Creative Field Value',
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
            '<h2>Creating creative field value with value "%s"</h2>',
            $values['field_value']
        );

        $fieldValue = new Google_Service_Dfareporting_CreativeFieldValue();
        $fieldValue->setValue($values['field_value']);

        $result = $this->service->creativeFieldValues->insert(
            $values['user_profile_id'],
            $values['creative_field_id'],
            $fieldValue
        );

        $this->printResultsTable('Creative field value created.', [$result]);
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public function getName()
    {
        return 'Create Creative Field Value';
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
