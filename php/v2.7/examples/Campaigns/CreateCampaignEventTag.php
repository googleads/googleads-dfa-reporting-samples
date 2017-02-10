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

/**
 * This example creates an event tag for the specified campaign.
 */
class CreateCampaignEventTag extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'campaign_id',
             'display' => 'Campaign ID',
             'required' => true],
            ['name' => 'event_tag_name',
             'display' => 'Event Tag Name',
             'required' => true],
            ['name' => 'event_tag_url',
             'display' => 'Event Tag URL',
             'required' => true]];
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Creating campaign event tag with name "%s"</h2>',
        $values['event_tag_name']
    );

    $eventTag = new Google_Service_Dfareporting_EventTag();
    $eventTag->setCampaignId($values['campaign_id']);
    $eventTag->setName($values['event_tag_name']);
    $eventTag->setStatus('ENABLED');
    $eventTag->setType('CLICK_THROUGH_EVENT_TAG');
    $eventTag->setUrl($values['event_tag_url']);

    $result = $this->service->eventTags->insert(
        $values['user_profile_id'], $eventTag
    );

    $this->printResultsTable('Campaign event tag created.', [$result]);
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Create Campaign Event Tag';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['id' => 'Event Tag ID',
            'name' => 'Event Tag Name'];
  }
}
