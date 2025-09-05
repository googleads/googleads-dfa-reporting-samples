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
 * This example targets an ad to a remarketing list.
 *
 * The first targetable remarketing list, either owned by or shared to the ad's
 * advertiser, will be used. To create a remarketing list, see
 * CreateRemarketingList.php. To share a remarketing list with the ad's
 * advertiser, see ShareRemarketingListToAdvertiser.php.
 */
class TargetAdToRemarketingList extends BaseExample
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
                ['name' => 'ad_id',
                 'display' => 'Ad ID',
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
            '<h2>Targeting ad %s to a targetable remarketing list.</h2>',
            $values['ad_id']
        );

        // Load the specified ad.
        $ad = $this->service->ads->get(
            $values['user_profile_id'],
            $values['ad_id']
        );

        // Find a targetable remarketing list for this ad's advertiser.
        $listService = $this->service->targetableRemarketingLists;
        $lists = $listService->listTargetableRemarketingLists(
            $values['user_profile_id'],
            $ad['advertiserId'],
            ['maxResults' => 1]
        );

        if (!empty($lists['targetableRemarketingLists'])) {
            // Select the first targetable remarketing list that was returned.
            $list = $lists['targetableRemarketingLists'][0];

            // Create a list targeting expression.
            $expression = new Google_Service_Dfareporting_ListTargetingExpression();
            $expression->setExpression(strval($list['id']));

            // Update the ad.
            $ad->setRemarketingListExpression($expression);
            $result = $this->service->ads->update(
                $values['user_profile_id'],
                $ad
            );

            $result['expression'] =
                $result['remarketing_list_expression']['expression'];

            $this->printResultsTable('Ad targeted to remarketing list.', [$result]);
        } else {
            print '<pre>Ad has no targetable remarketing lists.</pre>';
        }
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getName()
     * @return string
     */
    public static function getName()
    {
        return 'Target Ad to a Remarketing List';
    }

    /**
     * (non-PHPdoc)
     * @see BaseExample::getResultsTableHeaders()
     * @return array
     */
    public function getResultsTableHeaders()
    {
        return ['id' => 'Ad ID',
                'expression' => 'Remarketing List Expression'];
    }
}
