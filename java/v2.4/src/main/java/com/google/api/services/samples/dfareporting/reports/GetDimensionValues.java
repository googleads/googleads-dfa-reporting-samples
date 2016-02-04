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

package com.google.api.services.samples.dfareporting.reports;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Strings;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.DimensionValue;
import com.google.api.services.dfareporting.model.DimensionValueList;
import com.google.api.services.dfareporting.model.DimensionValueRequest;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

import java.util.Calendar;

/**
 * This example illustrates how to get all dimension values for a dimension.
 */
public class GetDimensionValues {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId) throws Exception {
    // Limit the fields returned.
    String fields = "nextPageToken,items(id,value)";

    // Construct the dimension value request.
    DimensionValueRequest request = new DimensionValueRequest();
    request.setDimensionName("dfa:advertiser");

    // Set the date range from 1 year ago until today.
    Calendar today = Calendar.getInstance();
    Calendar lastYear = Calendar.getInstance();
    lastYear.add(Calendar.YEAR, -1);

    request.setStartDate(new DateTime(true, lastYear.getTimeInMillis(), null));
    request.setEndDate(new DateTime(true, today.getTimeInMillis(), null));

    DimensionValueList values;
    String nextPageToken = null;

    do {
      // Create and execute the dimension value query request.
      values = reporting.dimensionValues().query(profileId, request).setFields(fields)
          .setPageToken(nextPageToken).execute();

      for (DimensionValue value : values.getItems()) {
        System.out.printf("Dimension value with ID %s and value \"%s\" was found.%n",
            value.getId(), value.getValue());
      }

      // Update the next page token.
      nextPageToken = values.getNextPageToken();
    } while (!values.getItems().isEmpty() && !Strings.isNullOrEmpty(nextPageToken));
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId);
  }
}
