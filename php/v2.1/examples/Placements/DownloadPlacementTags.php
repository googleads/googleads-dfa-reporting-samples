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

/**
 * This example downloads HTML Tags for a given campaign and placement ID. To
 * create campaigns, run CreateCampaign. To create placements, run
 * CreatePlacement.
 *
 * Tags: placements.generatetags
 *
 * @author api.jimper@gmail.com (Jonathon Imperiosi))
 */
class DownloadPlacementTags extends BaseExample {
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
        array('name' => 'campaign_id',
              'display' => 'Campaign ID',
              'required' => true),
        array('name' => 'placement_id',
              'display' => 'Placement ID',
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
        '<h2>Downloading placement tags for placement ID %s</h2>',
        $values['placement_id']
    );

    $placementTags = $this->service->placements->generatetags(
        $values['user_profile_id'],
        array(
            'campaignId' => $values['campaign_id'],
            'placementIds' => array($values['placement_id']),
            'tagFormats' => array(
                'PLACEMENT_TAG_STANDARD',
                'PLACEMENT_TAG_IFRAME_JAVASCRIPT',
                'PLACEMENT_TAG_INTERNAL_REDIRECT'
            )
        )
    );

    $this->printResultsTableHeader('Placement Tags');

    foreach ($placementTags['placementTags'] as $placementTag) {
      foreach ($placementTag['tagDatas'] as $tagData) {
        $result = array(
            'clickTag' => htmlspecialchars($tagData->getClickTag()),
            'format' => $tagData->getFormat(),
            'impressionTag' =>
                htmlspecialchars($tagData->getImpressionTag())
        );

        $this->printResultsTableRow($result);
      }
    }

    $this->printResultsTableFooter();
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Download Placement Tags';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return array(
        'format' => 'Format',
        'impressionTag' => 'Impression Tag',
        'clickTag' => 'Click Tag'
    );
  }
}
