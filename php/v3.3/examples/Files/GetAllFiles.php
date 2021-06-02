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
 * This example returns all files available to the specified user profile.
 */
class GetAllFiles extends BaseExample
{
    /**
     * {@inheritdoc}
     * @see BaseExample::getInputParameters()
     * @return array
     */
    protected function getInputParameters()
    {
        return [['name' => 'user_profile_id',
                 'display' => 'User Profile ID',
                 'required' => true]];
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        print '<h2>Listing all report files</h2>';

        $this->printResultsTableHeader('Files');

        do {
            $files = $this->service->files->listFiles(
                $values['user_profile_id'],
                ['pageToken' => $pageToken]
            );

            foreach ($files['items'] as $file) {
                $url = $file['urls']['browserUrl'];
                $file['download'] = '<a href="' . $url . '">Link</a>';
                $this->printResultsTableRow($file);
            }

            $pageToken = $files['nextPageToken'];
        } while (!empty($files['items']) && !empty($pageToken));

        $this->printResultsTableFooter();
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::getName()
     * @return string
     */
    public function getName()
    {
        return 'Get All Files';
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['id' => 'File ID',
                'fileName' => 'File Name',
                'reportId' => 'Report ID',
                'format' => 'File Format',
                'status' => 'Status',
                'download' => 'Download'];
    }
}
