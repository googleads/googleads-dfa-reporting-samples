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

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.DateRange;
import com.google.api.services.dfareporting.model.Report;
import com.google.api.services.dfareporting.model.Report.Criteria;
import com.google.api.services.dfareporting.model.SortedDimension;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.ImmutableList;

/**
 * This example illustrates how to create a report.
 */
public class CreateReport {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String REPORT_NAME = "INSERT_REPORT_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String reportName)
      throws Exception {
    // Create a date range to report on.
    DateRange dateRange = new DateRange();
    dateRange.setRelativeDateRange("YESTERDAY");

    // Create a dimension to report on.
    SortedDimension dimension = new SortedDimension();
    dimension.setName("dfa:campaign");

    // Create the criteria for the report.
    Criteria criteria = new Criteria();
    criteria.setDateRange(dateRange);
    criteria.setDimensions(ImmutableList.of(dimension));
    criteria.setMetricNames(ImmutableList.of("dfa:clicks"));

    // Create the report.
    Report report = new Report();
    report.setCriteria(criteria);
    report.setName(reportName);
    report.setType("STANDARD");

    // Insert the report.
    Report result = reporting.reports().insert(profileId, report).execute();

    // Display the new report ID.
    System.out.printf("Standard report with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, REPORT_NAME);
  }
}
