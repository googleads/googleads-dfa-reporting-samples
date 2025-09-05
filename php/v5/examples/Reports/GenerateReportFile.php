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
 * An end-to-end example of how to find and run a report, which generates a
 * report file.
 */
class GenerateReportFile extends BaseExample
{
    // The following values control retry behavior while the report is processing.
    // Minimum amount of time between polling requests. Defaults to 10 seconds.
    const MIN_RETRY_INTERVAL = 10;
    // Maximum amount of time between polling requests. Defaults to 10 minutes.
    const MAX_RETRY_INTERVAL = 600;
    // Maximum amount of time to spend polling. Defaults to 1 hour.
    const MAX_RETRY_ELAPSED_TIME = 3600;

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
                 'display' => 'Report ID']];
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $userProfileId = $values['user_profile_id'];

        // 1. Find a report to run.
        $reportId = $values['report_id'];
        if (empty($reportId)) {
            $reportId = $this->findReport($userProfileId);
        }

        if (!empty($reportId)) {
            printf(
                '<h2>Generating a report file for report with ID %d</h2>',
                $reportId
            );
            $this->flush();

            // 2. Run the report.
            $file = $this->runReport($userProfileId, $reportId);
            $this->flush();

            // 3. Wait for the report file to be ready.
            $file = $this->waitForReportFile($reportId, $file->getId());
            $this->flush();

            if (!empty($file)) {
                $this->printResultsTable('Report File', [$file]);
            }
        } else {
            printf('<h2>No report found for profile ID %d</h2>', $userProfileId);
        }
    }

    private function findReport($userProfileId)
    {
        $target = null;
        $response = null;
        $pageToken = null;

        do {
            // Create and execute the report list request.
            $response = $this->service->reports->listReports(
                $userProfileId,
                ['pageToken' => $pageToken]
            );

            foreach ($response->getItems() as $report) {
                if ($this->isTargetReport($report)) {
                    $target = $report;
                    break;
                }
            }

            $pageToken = $response->getNextPageToken();
        } while (empty($target) && !empty($response->getItems()) && !empty($pageToken));

        return is_null($target) ? null : $target->getId();
    }

    private function isTargetReport($file)
    {
        // Provide custom validation logic here.
        // For example purposes, any report is considered valid.
        return true;
    }

    private function runReport($userProfileId, $reportId)
    {
        // Run the report.
        $file = $this->service->reports->run($userProfileId, $reportId);

        printf(
            'Running report %d, current file status is %s<br>',
            $reportId,
            $file->getStatus()
        );
        return $file;
    }

    private function waitForReportFile($reportId, $fileId)
    {
        // Wait for the report file to finish processing.
        // An exponential backoff policy is used to limit retries and conserve
        // quota.
        $sleep = 0;
        $startTime = time();

        do {
            $file = $this->service->files->get($reportId, $fileId);

            if ($file->getStatus() === 'REPORT_AVAILABLE') {
                printf('File status is %s, ready to download<br>', $file->getStatus());
                return $file;
            } elseif ($file->getStatus() !== 'PROCESSING') {
                printf('File status is %s, processing failed<br>', $file->getStatus());
                return null;
            } elseif (time() - $startTime > self::MAX_RETRY_ELAPSED_TIME) {
                printf('File processing deadline exceeded<br>');
                return null;
            }

            $sleep = $this->getNextSleepInterval($sleep);
            printf(
                'File status is %s, sleeping for %d seconds<br>',
                $file->getStatus(),
                $sleep
            );
            $this->sleep($sleep);
        } while (true);
    }

    private function getNextSleepInterval($previousSleepInterval)
    {
        $minInterval = max(self::MIN_RETRY_INTERVAL, $previousSleepInterval);
        $maxInterval = max(self::MIN_RETRY_INTERVAL, $previousSleepInterval * 3);
        return min(self::MAX_RETRY_INTERVAL, rand($minInterval, $maxInterval));
    }

    private function sleep($interval)
    {
        $startTime = time();
        do {
            // Flush regularly to prevent timeouts.
            $this->flush();
            sleep(1);

            if (time() - $startTime >= $interval) {
                return;
            }
        } while (true);
    }

    /**
     * {@inheritdoc}
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Generate Report File';
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
                'status' => 'Status'];
    }
}
