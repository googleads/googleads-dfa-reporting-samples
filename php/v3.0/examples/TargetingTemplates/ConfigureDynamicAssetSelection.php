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
require_once dirname(__DIR__) . '/CreativeAssetUtils.php';

/**
 * This example uploads a new video asset to an existing in-stream video
 * creative and configures dynamic asset selection, using a specified targeting
 * template. To get an in-stream video creative, run
 * CreateInstreamVideoCreative. To get a targeting template, run
 * CreateTargetingTemplate.
 */
class ConfigureDynamicAssetSelection extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'creative_id',
             'display' => 'In-stream Video Creative ID',
             'required' => true],
            ['name' => 'template_id',
             'display' => 'Targeting Template ID',
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
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Configuring dynamic asset selection for creative ID %s</h2>',
        $values['creative_id']
    );

    // Retrieve the specified creative.
    $creative = $this->service->creatives->get(
        $values['user_profile_id'], $values['creative_id']);
    if(is_null($creative) ||
        strcmp($creative->getType(), 'INSTREAM_VIDEO') !== 0) {
      print 'Invalid creative specified.';
      return;
    }

    $assetSelection = $creative->getCreativeAssetSelection();
    if(!$creative->getDynamicAssetSelection()) {
      // Locate an existing video asset to use as a default.
      // This example uses the first PARENT_VIDEO asset found.
      $defaultVideoAssetId = $this->findDefaultVideoAssetId($creative);
      if($defaultVideoAssetId < 0) {
        print 'Default video asset could not be found.';
        return;
      }

      // Create a new selection using the existing asset as a default.
      $assetSelection =
          new Google_Service_Dfareporting_CreativeAssetSelection();
      $assetSelection->setDefaultAssetId($defaultVideoAssetId);
      $assetSelection->setRules([]);

      // Enable dynamic asset selection for the creative.
      $creative->setDynamicAssetSelection(true);
      $creative->setCreativeAssetSelection($assetSelection);
    }

    // Upload the new video asset and add it to the creative.
    $video = uploadAsset($this->service, $values['user_profile_id'],
        $creative->getAdvertiserId(), $values['asset_file'], 'VIDEO');
    $videoAsset = new Google_Service_Dfareporting_CreativeAsset();
    $videoAsset->setAssetIdentifier($video->getAssetIdentifier());
    $videoAsset->setRole('PARENT_VIDEO');
    $creative->setCreativeAssets(
        array_merge($creative->getCreativeAssets(), [$videoAsset]));

    // Create a rule targeting the new video asset and add it to the selection.
    $rule = new Google_Service_Dfareporting_Rule();
    $rule->setAssetId($video->getId());
    $rule->setName('Test rule for asset ' . $video->getId());
    $rule->setTargetingTemplateId($values['template_id']);
    $assetSelection->setRules(
        array_merge($assetSelection->getRules(), [$rule]));

    // Update the creative.
    $result = $this->service->creatives->update($values['user_profile_id'],
        $creative);

    printf('Dynamic asset selection enabled for creative with ID %d.',
        $result->getId());
  }

  private function findDefaultVideoAssetId($creative) {
    $assets = $creative->getCreativeAssets();
    $index = array_search('PARENT_VIDEO',
        array_map(
            function($asset) {
              return $asset->getRole();
            }, $assets
        )
    );

    return $index !== false ? $assets[$index]->getId() : -1;
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Configure Dynamic Asset Targeting';
  }
}
