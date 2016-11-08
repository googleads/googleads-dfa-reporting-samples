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
import com.google.api.services.dfareporting.model.CompatibleFields;
import com.google.api.services.dfareporting.model.Dimension;
import com.google.api.services.dfareporting.model.Metric;
import com.google.api.services.dfareporting.model.Report;
import com.google.api.services.dfareporting.model.ReportCompatibleFields;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

import java.util.List;

/**
 * This example illustrates how to get the compatible fields for a standard report.
 */
public class GetCompatibleFields {
  private static final String USER_PROFILE_ID = "ENTER_USER_PROFILE_ID_HERE";

  private static final String REPORT_ID = "ENTER_REPORT_ID_HERE";

  public static void runExample(Dfareporting reporting, long profileId, long reportId)
      throws Exception {
    // Retrieve the specified report.
    Report report = reporting.reports().get(profileId, reportId).execute();

    // Look up the compatible fields for the report.
    CompatibleFields fields =
        reporting.reports().compatibleFields().query(profileId, report).execute();

    // Display the compatible fields, assuming this is a standard report.
    ReportCompatibleFields reportFields = fields.getReportCompatibleFields();

    System.out.println("Compatible dimensions:");
    printDimensionNames(reportFields.getDimensions());

    System.out.printf("%nCompatible metrics:%n");
    printMetricNames(reportFields.getMetrics());

    System.out.printf("%nCompatible dimension filters:%n");
    printDimensionNames(reportFields.getDimensionFilters());

    System.out.printf("%nCompatible pivoted activity metrics:%n");
    printMetricNames(reportFields.getPivotedActivityMetrics());
  }

  private static void printDimensionNames(List<Dimension> dimensions) {
    for (Dimension dimension : dimensions) {
      System.out.println(dimension.getName());
    }
  }

  private static void printMetricNames(List<Metric> metrics) {
    for (Metric metric : metrics) {
      System.out.println(metric.getName());
    }
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);
    long reportId = Long.parseLong(REPORT_ID);

    runExample(reporting, profileId, reportId);
  }
}
