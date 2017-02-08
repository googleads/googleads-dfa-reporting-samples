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
require_once dirname(__DIR__) . '/CreativeAssetUtils.php';

/**
 * This example uploads creative assets and creates an HTML5 display creative
 * associated with a given advertiser. To get a size ID, run GetSize.
 */
class CreateHTML5DisplayCreative extends BaseExample {
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
        array('name' => 'html_asset_file',
              'display' => 'HTML5 Asset File',
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
        '<h2>Creating HTML5 display creative from HTML5 asset "%s"</h2>',
        $values['html_asset_file']['name']
    );

    $creative = new Google_Service_Dfareporting_Creative();
    $creative->setAdvertiserId($values['advertiser_id']);
    $creative->setAutoAdvanceImages(true);
    $creative->setName('Test HTML5 display creative');
    $creative->setType('DISPLAY');

    $size = new Google_Service_Dfareporting_Size();
    $size->setId($values['size_id']);
    $creative->setSize($size);

    // Upload the HTML5 asset.
    $html = uploadAsset($this->service, $values['user_profile_id'],
        $values['advertiser_id'], $values['html_asset_file'], 'HTML');

    $htmlAsset = new Google_Service_Dfareporting_CreativeAsset();
    $htmlAsset->setAssetIdentifier($html->getAssetIdentifier());
    $htmlAsset->setRole('PRIMARY');

    // Upload the backup image asset.
    $image = uploadAsset($this->service, $values['user_profile_id'],
        $values['advertiser_id'], $values['image_asset_file'], 'HTML_IMAGE');

    $imageAsset = new Google_Service_Dfareporting_CreativeAsset();
    $imageAsset->setAssetIdentifier($image->getAssetIdentifier());
    $imageAsset->setRole('BACKUP_IMAGE');

    // Add the creative assets.
    $creative->setCreativeAssets(array($htmlAsset, $imageAsset));

    // Configure the backup image.
    $creative->setBackupImageClickThroughUrl('https://www.google.com');
    $creative->setBackupImageReportingLabel('backup');

    $targetWindow = new Google_Service_Dfareporting_TargetWindow();
    $targetWindow->setTargetWindowOption('NEW_WINDOW');
    $creative->setBackupImageTargetWindow($targetWindow);

    // Add a click tag.
    $clickTag = new Google_Service_Dfareporting_ClickTag();
    $clickTag->setName('clickTag');
    $clickTag->setEventName('exit');
    $clickTag->setValue('https://www.google.com');
    $creative->setClickTags(array($clickTag));

    $result = $this->service->creatives->insert($values['user_profile_id'],
        $creative);

    $this->printResultsTable('HTML5 display creative created.', array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create HTML5 Display Creative';
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
