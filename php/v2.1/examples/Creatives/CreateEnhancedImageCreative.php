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
 * This example uploads creative assets and creates an enhanced image creative
 * associated with a given advertiser. To get a size ID, run GetSize.
 *
 * Tags: creatives.insert
 *
 * @author api.jimper@gmail.com (Jonathon Imperiosi)
 */
class CreateEnhancedImageCreative extends BaseExample {
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
        array('name' => 'image1_asset_file',
              'display' => 'First Image Asset File',
              'file' => true,
              'required' => true),
        array('name' => 'image2_asset_file',
              'display' => 'Second Image Asset File',
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
        '<h2>Creating enhanced banner creative from images "%s" and "%s"</h2>',
        $values['image1_asset_file']['name'],
        $values['image2_asset_file']['name']
    );

    $creative = new Google_Service_Dfareporting_Creative();
    $creative->setAdvertiserId($values['advertiser_id']);
    $creative->setAutoAdvanceImages(true);
    $creative->setName('Test enhanced image creative');
    $creative->setType('ENHANCED_IMAGE');

    $size = new Google_Service_Dfareporting_Size();
    $size->setId($values['size_id']);
    $creative->setSize($size);

    // Upload the first image asset.
    $image1_asset_id = uploadAsset($this->service, $values['user_profile_id'],
        $values['advertiser_id'], $values['image1_asset_file'], 'HTML_IMAGE');

    $image1_asset = new Google_Service_Dfareporting_CreativeAsset();
    $image1_asset->setAssetIdentifier($image1_asset_id);
    $image1_asset->setRole('PRIMARY');

    // Upload the second image asset.
    $image2_asset_id = uploadAsset($this->service, $values['user_profile_id'],
        $values['advertiser_id'], $values['image2_asset_file'], 'HTML_IMAGE');

    $image2_asset = new Google_Service_Dfareporting_CreativeAsset();
    $image2_asset->setAssetIdentifier($image2_asset_id);
    $image2_asset->setRole('PRIMARY');

    // Add the creative assets.
    $creative->setCreativeAssets(array($image1_asset, $image2_asset));

    // Add a click tag for the first image asset.
    $click_tag1 = new Google_Service_Dfareporting_ClickTag();
    $click_tag1->setName($image1_asset_id->getName());
    $click_tag1->setEventName($image1_asset_id->getName());

    // Add a click tag for the second image asset.
    $click_tag2 = new Google_Service_Dfareporting_ClickTag();
    $click_tag2->setName($image2_asset_id->getName());
    $click_tag2->setEventName($image2_asset_id->getName());

    $creative->setClickTags(array($click_tag1, $click_tag2));

    $result = $this->service->creatives->insert($values['user_profile_id'],
        $creative);

    $this->printResultsTable('Enhanced image creative created.',
        array($result));
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Enhanced Image Creative';
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
