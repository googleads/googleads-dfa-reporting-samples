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
 * This example inserts an offline conversion attributed to an encrypted user
 * ID and associated with a specified Floodlight activity. To create an
 * activity, run CreateFloodlightActivity.
 */
class InsertOfflineUserConversion extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'encrypted_user_id',
             'display' => 'Encrypted User ID',
             'required' => true],
            ['name' => 'encryption_entity_id',
             'display' => 'Encryption Entity ID',
             'required' => true],
            ['name' => 'encryption_entity_type',
             'display' => 'Encryption Entity Type',
             'required' => true],
            ['name' => 'encryption_source',
             'display' => 'Encryption Source',
             'required' => true],
            ['name' => 'floodlight_activity_id',
             'display' => 'Floodlight Activity ID',
             'required' => true]];
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Inserting offline conversion for encrypted user ID "%s"</h2>',
        $values['encrypted_user_id']
    );

    $currentTimeInMicros = time() * 1000 * 1000;

    // Find Floodlight configuration ID based on provided activity ID.
    $activity = $this->service->floodlightActivities->get(
        $values['user_profile_id'], $values['floodlight_activity_id']);
    $floodlightConfigId = $activity->getFloodlightConfigurationId();

    $conversion = new Google_Service_Dfareporting_Conversion();
    $conversion->setEncryptedUserId($values['encrypted_user_id']);
    $conversion->setFloodlightActivityId($values['floodlight_activity_id']);
    $conversion->setFloodlightConfigurationId($floodlightConfigId);
    $conversion->setOrdinal($currentTimeInMicros);
    $conversion->setTimestampMicros($currentTimeInMicros);

    $encryptionInfo = new Google_Service_Dfareporting_EncryptionInfo();
    $encryptionInfo->setEncryptionEntityId($values['encryption_entity_id']);
    $encryptionInfo->setEncryptionEntityType($values['encryption_entity_type']);
    $encryptionInfo->setEncryptionSource($values['encryption_source']);

    $batch = new Google_Service_Dfareporting_ConversionsBatchInsertRequest();
    $batch->setConversions([$conversion]);
    $batch->setEncryptionInfo($encryptionInfo);

    $result = $this->service->conversions->batchinsert(
        $values['user_profile_id'], $batch
    );

    if(!$result->getHasFailures()) {
      printf('Successfully inserted conversion for encrypted user ID %s.',
          $values['encrypted_user_id']);
    } else {
      printf('Error(s) inserting conversion for encrypted user ID %s:<br><br>',
          $values['encrypted_user_id']);

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
  public function getName() {
    return 'Insert Offline User Conversion';
  }
}
