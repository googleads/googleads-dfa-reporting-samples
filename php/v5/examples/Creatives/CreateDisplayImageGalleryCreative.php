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
 * This example uploads creative assets and creates a display image gallery
 * creative associated with a given advertiser. To get a size ID, run GetSize.
 */
class CreateDisplayImageGalleryCreative extends BaseExample
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
                ['name' => 'size_id',
                 'display' => 'Size ID',
                 'required' => true],
                ['name' => 'image1_asset_file',
                 'display' => 'First Image Asset File',
                 'file' => true,
                 'required' => true],
                ['name' => 'image2_asset_file',
                 'display' => 'Second Image Asset File',
                 'file' => true,
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
            '<h2>Creating display image gallery creative from "%s" and "%s"</h2>',
            $values['image1_asset_file']['name'],
            $values['image2_asset_file']['name']
        );

        $creative = new Google_Service_Dfareporting_Creative();
        $creative->setAdvertiserId($values['advertiser_id']);
        $creative->setAutoAdvanceImages(true);
        $creative->setName('Test display image gallery creative');
        $creative->setType('DISPLAY_IMAGE_GALLERY');

        $size = new Google_Service_Dfareporting_Size();
        $size->setId($values['size_id']);
        $creative->setSize($size);

        // Upload the first image asset.
        $image1 = uploadAsset(
            $this->service,
            $values['user_profile_id'],
            $values['advertiser_id'],
            $values['image1_asset_file'],
            'HTML_IMAGE'
        );

        $image1Asset = new Google_Service_Dfareporting_CreativeAsset();
        $image1Asset->setAssetIdentifier($image1->getAssetIdentifier());
        $image1Asset->setRole('PRIMARY');

        // Upload the second image asset.
        $image2 = uploadAsset(
            $this->service,
            $values['user_profile_id'],
            $values['advertiser_id'],
            $values['image2_asset_file'],
            'HTML_IMAGE'
        );

        $image2Asset = new Google_Service_Dfareporting_CreativeAsset();
        $image2Asset->setAssetIdentifier($image2->getAssetIdentifier());
        $image2Asset->setRole('PRIMARY');

        // Add the creative assets.
        $creative->setCreativeAssets([$image1Asset, $image2Asset]);

        // Add a click tag for the first image asset.
        $clickTag1 = new Google_Service_Dfareporting_ClickTag();
        $clickTag1->setName($image1->getAssetIdentifier()->getName());
        $clickTag1->setEventName($image1->getAssetIdentifier()->getName());

        // Add a click tag for the second image asset.
        $clickTag2 = new Google_Service_Dfareporting_ClickTag();
        $clickTag2->setName($image2->getAssetIdentifier()->getName());
        $clickTag2->setEventName($image2->getAssetIdentifier()->getName());

        $creative->setClickTags([$clickTag1, $clickTag2]);

        $result = $this->service->creatives->insert(
            $values['user_profile_id'],
            $creative
        );

        $this->printResultsTable(
            'Display image gallery creative created.',
            [$result]
        );
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Create Display Image Gallery Creative';
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
