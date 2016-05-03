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
require_once dirname(__DIR__) . "/BaseExample.php";
require_once 'utils/CreativeAssetUtils.php';

/**
 * This example uploads creative assets and creates a flash display creative
 * associated with a given advertiser. To get a size ID, run GetSize.
 */
class CreateFlashInpageCreative extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return array(
        array('name' => 'user_profile_id',
              'display' => 'User Profile ID',
              'required' => true),
        array('name' => 'advertiser_id',
              'display' => 'Advertiser ID',
              'required' => true),
        array('name' => 'size_id',
              'display' => 'Size ID',
              'required' => true),
        array('name' => 'flash_asset_file',
              'display' => 'Flash Asset File',
              'file' => true,
              'required' => true),
        array('name' => 'image_asset_file',
              'display' => 'Backup Image Asset File',
              'file' => true,
              'required' => true)
    );
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Creating flash display creative from flash asset "%s"</h2>',
        $values['flash_asset_file']['name']
    );

    $creative = new Google_Service_Dfareporting_Creative();
    $creative->setAdvertiserId($values['advertiser_id']);
    $creative->setAutoAdvanceImages(true);
    $creative->setName('Test flash display creative');
    $creative->setType('ENHANCED_BANNER');

    $size = new Google_Service_Dfareporting_Size();
    $size->setId($values['size_id']);
    $creative->setSize($size);

    // Upload the flash asset.
    $flash_asset_id = uploadAsset($this->service, $values['user_profile_id'],
        $values['advertiser_id'], $values['flash_asset_file'], 'FLASH');

    $flash_asset = new Google_Service_Dfareporting_CreativeAsset();
    $flash_asset->setAssetIdentifier($flash_asset_id);
    $flash_asset->setRole('PRIMARY');
    $flash_asset->setWindowMode('TRANSPARENT');

    // Upload the backup image asset.
    $image_asset_id = uploadAsset($this->service, $values['user_profile_id'],
        $values['advertiser_id'], $values['image_asset_file'], 'HTML_IMAGE');

    $image_asset = new Google_Service_Dfareporting_CreativeAsset();
    $image_asset->setAssetIdentifier($image_asset_id);
    $image_asset->setRole('BACKUP_IMAGE');

    // Add the creative assets.
    $creative->setCreativeAssets(array($flash_asset, $image_asset));

    // Configure the backup image.
    $creative->setBackupImageClickThroughUrl('https://www.google.com');
    $creative->setBackupImageReportingLabel('backup');

    $target_window = new Google_Service_Dfareporting_TargetWindow();
    $target_window->setTargetWindowOption('NEW_WINDOW');
    $creative->setBackupImageTargetWindow($target_window);

    $result = $this->service->creatives->insert($values['user_profile_id'],
        $creative);

    $this->printResultsTable('Flash display creative created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Flash Display Creative';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'id' => 'Creative ID',
        'name' => 'Creative Name',
        'type' => 'Creative type'
    );
  }
}
