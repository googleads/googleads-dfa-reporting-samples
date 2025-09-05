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
 * This example uploads creative assets and creates an in-stream video
 * creative associated with a given advertiser.
 */
class CreateInstreamVideoCreative extends BaseExample
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
                ['name' => 'asset_file',
                 'display' => 'Video Asset File',
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
            '<h2>Creating in-stream video creative from video asset "%s"</h2>',
            $values['asset_file']['name']
        );

        $creative = new Google_Service_Dfareporting_Creative();
        $creative->setAdvertiserId($values['advertiser_id']);
        $creative->setName('Test in-stream video creative');
        $creative->setType('INSTREAM_VIDEO');

        $size = new Google_Service_Dfareporting_Size();
        $size->setId($values['size_id']);
        $creative->setSize($size);

        // Upload the video asset.
        $video = uploadAsset(
            $this->service,
            $values['user_profile_id'],
            $values['advertiser_id'],
            $values['asset_file'],
            'VIDEO'
        );

        $asset = new Google_Service_Dfareporting_CreativeAsset();
        $asset->setAssetIdentifier($video->getAssetIdentifier());
        $asset->setRole('PARENT_VIDEO');

        // Add the creative asset.
        $creative->setCreativeAssets([$asset]);

        $result = $this->service->creatives->insert(
            $values['user_profile_id'],
            $creative
        );

        $this->printResultsTable('In-stream video creative created.', [$result]);
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Create In-stream Video Creative';
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
