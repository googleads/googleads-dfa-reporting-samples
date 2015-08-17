// Copyright 2014 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.services.samples.dfareporting.misc;

import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.ChangeLog;
import com.google.api.services.dfareporting.model.ChangeLogsListResponse;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example displays the change logs of a specified advertiser object.
 *
 * A similar pattern can be applied to get change logs for many other object types.
 */
public class GetChangeLogsForAdvertiser {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String ADVERTISER_ID = "INSERT_ADVERTISER_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long advertiserId)
      throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,changeLogs(action,fieldName,oldValue,newValue)";

    ChangeLogsListResponse changeLogs;
    String nextPageToken = null;

    do {
      // Create and execute the change logs list request
      changeLogs = reporting.changeLogs().list(profileId)
          .setObjectIds(ImmutableList.of(advertiserId)).setObjectType("OBJECT_ADVERTISER")
          .setFields(fields).setPageToken(nextPageToken).execute();

      for (ChangeLog changeLog : changeLogs.getChangeLogs()) {
        System.out.printf("%s: Field \"%s\" from \"%s\" to \"%s\".%n", changeLog.getAction(),
            changeLog.getFieldName(), changeLog.getOldValue(), changeLog.getNewValue());
      }

      // Update the next page token.
      nextPageToken = changeLogs.getNextPageToken();
    } while (!changeLogs.getChangeLogs().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long advertiserId = Long.parseLong(ADVERTISER_ID);
    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, advertiserId);
  }
}
