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
 * This example displays all sizes for a given width and height.
 */
class GetSize extends BaseExample {
  /**
   * (non-PHPdoc)
   * @see BaseExample::getInputParameters()
   * @return array
   */
  protected function getInputParameters() {
    return [['name' => 'user_profile_id',
             'display' => 'User Profile ID',
             'required' => true],
            ['name' => 'width',
             'display' => 'Width (px)',
             'required' => true],
            ['name' => 'height',
             'display' => 'Height (px)',
             'required' => true]];
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::run()
   */
  public function run() {
    $values = $this->formValues;

    printf(
        '<h2>Listing sizes matching %sx%s</h2>',
        $values['width']. $values['height']
    );

    $this->printResultsTableHeader('Sizes');

    // Create and execute the size list request.
    $response = $this->service->sizes->listSizes(
        $values['user_profile_id'],
        ['height' => $values['height'], 'width' => $values['width']]
    );

    foreach ($response->getSizes() as $sizes) {
      $this->printResultsTableRow($sizes);
    }

    $this->printResultsTableFooter();
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getName()
   * @return string
   */
  public function getName() {
    return 'Get Size';
  }

  /**
   * (non-PHPdoc)
   * @see BaseExample::getResultsTableHeaders()
   * @return array
   */
  public function getResultsTableHeaders() {
    return ['id' => 'Size ID',
            'width' => 'Width',
            'height' => 'Height'];
  }
}
