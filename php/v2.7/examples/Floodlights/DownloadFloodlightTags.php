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
 * This example downloads activity tags for a given floodlight activity.
 */
class DownloadFloodlightTags extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'activity_id',
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
        '<h2>Downloading floodlight activity tags for activity ID %s</h2>',
        $values['activity_id']
    );

    $result = $this->service->floodlightActivities->generatetag(
        $values['user_profile_id'],
        ['floodlightActivityId' => $values['activity_id']]
    );

    // Prepare the tag for display
    $htmlEscapedTag = htmlentities($result->getFloodlightActivityTag());
    print str_replace("\r\n", '<br>', $htmlEscapedTag);
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Download Floodlight Activity Tags';
  }
}
