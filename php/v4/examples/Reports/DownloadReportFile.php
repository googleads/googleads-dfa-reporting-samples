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
 * An end-to-end of example of how to find and download a report file.
 */
class DownloadReportFile extends BaseExample
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
                 'required' => true],
                ['name' => 'report_id',
                 'display' => 'Report ID',
                 'required' => true],
                ['name' => 'file_id',
                 'display' => 'Report File ID']];
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $reportId = $values['report_id'];
        $userProfileId = $values['user_profile_id'];

        // 1. Find a file to download.
        $fileId = $values['file_id'];
        if (empty($fileId)) {
            $fileId = $this->findFile($userProfileId, $reportId);
        }

        if (!empty($fileId)) {
            printf(
                '<h2>Retrieving contents of file %d for report %d</h2>',
                $fileId,
                $reportId
            );
            $this->flush();

            // 2. (optional) Generate browser URL.
            $this->generateBrowserUrl($reportId, $fileId);
            $this->flush();

            // 3. Directly download the file.
            $this->directDownloadFile($reportId, $fileId);
        } else {
            printf(
                '<h2>No file found for profile ID %d and report ID %d</h2>',
                $userProfileId,
                $reportId
            );
        }
    }

    private function findFile($userProfileId, $reportId)
    {
        $target = null;
        $response = null;
        $pageToken = null;

        do {
            // Create and execute the file list request.
            $response = $this->service->reports_files->listReportsFiles(
                $userProfileId,
                $reportId,
                ['pageToken' => $pageToken]
            );

            foreach ($response->getItems() as $file) {
                if ($this->isTargetFile($file)) {
                    $target = $file;
                    break;
                }
            }

            $pageToken = $response->getNextPageToken();
        } while (empty($target) && !empty($response->getItems()) && !empty($pageToken));

        return is_null($target) ? null : $target->getId();
    }

    private function isTargetFile($file)
    {
        // Provide custom validation logic here.
        // For example purposes, any file with REPORT_AVAILABLE status is
        // considered valid.
        return $file->getStatus() === 'REPORT_AVAILABLE';
    }

    private function generateBrowserUrl($reportId, $fileId)
    {
        $file = $this->service->files->get($reportId, $fileId);
        $browserUrl = $file->getUrls()->getBrowserUrl();

        printf(
            '<h3>Report file has browser URL: <a href="%s">%s</a></h3>',
            $browserUrl,
            $browserUrl
        );
    }

    private function directDownloadFile($reportId, $fileId)
    {
        // Retrieve the file metadata.
        $file = $this->service->files->get($reportId, $fileId);

        if ($file->getStatus() === 'REPORT_AVAILABLE') {
            try {
                // Prepare a local file to download the report contents to.
                $fileName = join(
                    DIRECTORY_SEPARATOR,
                    [sys_get_temp_dir(), $this->generateFileName($file)]
                );
                $fileResource = fopen($fileName, 'w+');
                $fileStream = \GuzzleHttp\Psr7\stream_for($fileResource);

                // Execute the get request and download the file.
                $httpClient = $this->service->getClient()->authorize();
                $result = $httpClient->request(
                    'GET',
                    $file->getUrls()->getApiUrl(),
                    [\GuzzleHttp\RequestOptions::SINK => $fileStream]
                );

                printf('<h3>Report file saved to: %s</h3>', $fileName);
                print '<h3>Report file contents:</h3>';
                print nl2br(file_get_contents($fileName));
            } finally {
                $fileStream->close();
                fclose($fileResource);
            }
        }
    }

    private function generateFileName($file)
    {
        $fileName = $file->getFileName() ?: $file->getId();
        $extension = $file->getFormat() === 'CSV' ? '.csv' : '.xls';

        return $fileName . $extension;
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Download Report File';
    }
}
