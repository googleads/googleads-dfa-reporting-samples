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

function uploadAsset($service, $userProfileId, $advertiserId, $asset,
  $type) {
  $assetId = new Google_Service_Dfareporting_CreativeAssetId();
  $assetId->setName($asset['name']);
  $assetId->setType($type);

  $metadata = new Google_Service_Dfareporting_CreativeAssetMetadata();
  $metadata->setAssetIdentifier($assetId);

  $result = $service->creativeAssets->insert(
      $userProfileId, $advertiserId, $metadata,
      array(
          'data' => file_get_contents($asset['tmp_name']),
          'mimeType' => $asset['type'],
          'uploadType' => 'multipart'
      )
  );

  return $result;
}
