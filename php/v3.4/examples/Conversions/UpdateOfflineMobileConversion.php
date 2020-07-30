<?php
/*
 * Copyright 2017 Google Inc.
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
 * This example updates an offline conversion attributed to a mobile device ID.
 * To create a conversion attributed to a mobile device ID, run
 * InsertOfflineMobileConversion.php.
 */
class UpdateOfflineMobileConversion extends BaseExample
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
                ['name' => 'mobile_device_id',
                 'display' => 'Mobile Device ID',
                 'required' => true],
                ['name' => 'floodlight_activity_id',
                 'display' => 'Floodlight Activity ID',
                 'required' => true],
                ['name' => 'ordinal',
                 'display' => 'Ordinal',
                 'required' => true],
                ['name' => 'timestamp',
                 'display' => 'Timestamp (microseconds)',
                 'required' => true],
                ['name' => 'new_quantity',
                 'display' => 'New Quantity',
                 'required' => true],
                ['name' => 'new_value',
                 'display' => 'New Value',
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
            '<h2>Updating offline conversion for mobile device ID "%s"</h2>',
            $values['mobile_device_id']
        );

        // Find Floodlight configuration ID based on provided activity ID.
        $activity = $this->service->floodlightActivities->get(
            $values['user_profile_id'],
            $values['floodlight_activity_id']
        );
        $floodlightConfigId = $activity->getFloodlightConfigurationId();

        // Create a conversion object with values that identify the conversion to
        // update.
        $conversion = new Google_Service_Dfareporting_Conversion();
        $conversion->setMobileDeviceId($values['mobile_device_id']);
        $conversion->setFloodlightActivityId($values['floodlight_activity_id']);
        $conversion->setFloodlightConfigurationId($floodlightConfigId);
        $conversion->setOrdinal($values['ordinal']);
        $conversion->setTimestampMicros($values['timestamp']);

        // Set the fields to be updated. These fields are required; to preserve a
        // value from the existing conversion, it must be copied over manually.
        $conversion->setQuantity($values['new_quantity']);
        $conversion->setValue($values['new_value']);

        $batch = new Google_Service_Dfareporting_ConversionsBatchUpdateRequest();
        $batch->setConversions([$conversion]);

        $result = $this->service->conversions->batchupdate(
            $values['user_profile_id'],
            $batch
        );

        if (!$result->getHasFailures()) {
            printf(
                'Successfully updated conversion for mobile device ID %s.',
                $values['mobile_device_id']
            );
        } else {
            printf(
                'Error(s) updating conversion for mobile device ID %s:<br><br>',
                $values['mobile_device_id']
            );

            $status = $result->getStatus()[0];
            foreach ($status->getErrors() as $error) {
                printf('[%s] %s<br>', $error->getCode(), $error->getMessage());
            }
        }
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public function getName()
    {
        return 'Update Offline Mobile Conversion';
    }
}
