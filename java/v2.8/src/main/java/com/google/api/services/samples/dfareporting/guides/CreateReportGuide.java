/*
 * Copyright (c) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.dfareporting.guides;

import com.google.api.client.util.DateTime;
import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.CompatibleFields;
import com.google.api.services.dfareporting.model.DateRange;
import com.google.api.services.dfareporting.model.Dimension;
import com.google.api.services.dfareporting.model.DimensionValue;
import com.google.api.services.dfareporting.model.DimensionValueList;
import com.google.api.services.dfareporting.model.DimensionValueRequest;
import com.google.api.services.dfareporting.model.Metric;
import com.google.api.services.dfareporting.model.Report;
import com.google.api.services.dfareporting.model.ReportCompatibleFields;
import com.google.api.services.dfareporting.model.SortedDimension;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * This example provides an end-to-end example of how to create and configure a standard report.
 */
public class CreateReportGuide {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    // 1. Create a report resource
    Report report = createReportResource(reporting);

    // 2. Define the report criteria
    defineReportCriteria(report);

    // 3. (optional) Look up compatible fields
    findCompatibleFields(reporting, profileId, report);

    // 4. Add dimension filters to the report criteria
    addDimensionFilters(reporting, profileId, report);

    // 5. Save the report resource
    report = insertReportResource(reporting, profileId, report);
  }

  private static Report createReportResource(Dfareporting reporting) {
    // [START create_report]
    Report report = new Report();

    // Set the required fields "name" and "type".
    report.setName("Example standard report");
    report.setType("STANDARD");

    // Set optional fields
    report.setFileName("example_report");
    report.setFormat("CSV");
    // [END create_report]

    System.out.printf("Creating %s report resource with name \"%s\".%n", report.getType(),
        report.getName());

    return report;
  }

  private static void defineReportCriteria(Report report) throws IOException {
    // [START report_criteria]
    // Define a date range to report on. This example uses explicit start and end dates to mimic
    // the "LAST_MONTH" relative date range.
    DateRange dateRange = new DateRange();
    dateRange.setEndDate(new DateTime(true, System.currentTimeMillis(), null));

    Calendar lastMonth = Calendar.getInstance();
    lastMonth.add(Calendar.MONTH, -1);
    dateRange.setStartDate(new DateTime(true, lastMonth.getTimeInMillis(), null));

    // Create a report criteria.
    Report.Criteria criteria = new Report.Criteria();
    criteria.setDateRange(dateRange);
    criteria.setDimensions(Lists.newArrayList(new SortedDimension().setName("dfa:advertiser")));
    criteria.setMetricNames(Lists.newArrayList("dfa:clicks", "dfa:impressions"));

    // Add the criteria to the report resource.
    report.setCriteria(criteria);
    // [END report_criteria]

    System.out.printf("%nAdded report criteria:%n%s%n", criteria.toPrettyString());
  }

  private static void findCompatibleFields(Dfareporting reporting, long profileId, Report report)
      throws IOException {
    // [START compatible_fields]
    CompatibleFields fields = reporting.reports().compatibleFields()
        .query(profileId, report).execute();

    ReportCompatibleFields reportFields = fields.getReportCompatibleFields();

    if (!reportFields.getDimensions().isEmpty()) {
      // Add a compatible dimension to the report.
      Dimension dimension = reportFields.getDimensions().get(0);
      SortedDimension sortedDimension = new SortedDimension().setName(dimension.getName());
      report.getCriteria().getDimensions().add(sortedDimension);
    } else if (!reportFields.getMetrics().isEmpty()) {
      // Add a compatible metric to the report.
      Metric metric = reportFields.getMetrics().get(0);
      report.getCriteria().getMetricNames().add(metric.getName());
    }
    // [END compatible_fields]

    System.out.printf("%nUpdated report criteria (with compatible fields):%n%s%n",
        report.getCriteria().toPrettyString());
  }

  private static void addDimensionFilters(Dfareporting reporting, long profileId, Report report)
      throws IOException {
    // [START dimension_values]
    // Query advertiser dimension values for report run dates.
    DimensionValueRequest request = new DimensionValueRequest();
    request.setStartDate(report.getCriteria().getDateRange().getStartDate());
    request.setEndDate(report.getCriteria().getDateRange().getEndDate());
    request.setDimensionName("dfa:advertiser");

    DimensionValueList values = reporting.dimensionValues().query(profileId, request).execute();

    if (!values.getItems().isEmpty()) {
      // Add a value as a filter to the report criteria.
      List<DimensionValue> filters = Lists.newArrayList(values.getItems().get(0));
      report.getCriteria().setDimensionFilters(filters);
    }
    // [END dimension_values]

    System.out.printf("%nUpdated report criteria (with valid dimension filters):%n%s%n",
        report.getCriteria());
  }

  private static Report insertReportResource(Dfareporting reporting, long profileId, Report report)
      throws IOException {
    // [START insert_report]
    Report insertedReport = reporting.reports().insert(profileId, report).execute();
    // [END insert_report]

    System.out.printf("%nSuccessfully inserted new report with ID %d.%n", insertedReport.getId());
    return insertedReport;
  }
}
