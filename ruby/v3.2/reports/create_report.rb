#!/usr/bin/env ruby

#
# Copyright:: Copyright 2016, Google Inc. All Rights Reserved.
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# An end-to-end example of how to create and configure a standard report.

require_relative '../dfareporting_utils'
require 'date'

def create_report(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # 1. Create a report resource.
  report = create_report_resource

  # 2. Define report criteria.
  define_report_criteria(report)

  # 3. (optional) Look up compatible fields.
  find_compatible_fields(service, profile_id, report)

  # 4. Add dimension filers to the report criteria.
  add_dimension_filters(service, profile_id, report)

  # 5. Save the report resource.
  insert_report_resource(service, profile_id, report)
end

def create_report_resource
  report = DfareportingUtils::API_NAMESPACE::Report.new(
    # Set the required fields "name" and "type".
    name: 'Example Standard Report',
    type: 'STANDARD',
    # Set optional fields.
    file_name: 'example_report',
    format: 'CSV'
  )

  puts format('Creating %s report resource with name "%s".', report.type, report.name)

  report
end

def define_report_criteria(report)
  # Define a date range to report on. This example uses explicit start and end
  # dates to mimic the "LAST_30_DAYS" relative date range.
  start_date = DateTime.now.prev_day(30).strftime('%Y-%m-%d')
  end_date = DateTime.now.strftime('%Y-%m-%d')

  # Create a report criteria
  criteria = DfareportingUtils::API_NAMESPACE::Report::Criteria.new(
    date_range: DfareportingUtils::API_NAMESPACE::DateRange.new(
      start_date: start_date,
      end_date: end_date
    ),
    dimensions: [
      DfareportingUtils::API_NAMESPACE::SortedDimension.new(
        name: 'dfa:advertiser'
      )
    ],
    metric_names: ['dfa:clicks', 'dfa:impressions']
  )

  # Add the criteria to the report resource.
  report.criteria = criteria

  puts format("\nAdded report criteria:\n%s", criteria.to_json)
end

def find_compatible_fields(service, profile_id, report)
  fields = service.query_report_compatible_field(profile_id, report)

  report_fields = fields.report_compatible_fields

  if report_fields.dimensions.any?
    # Add a compatible dimension to the report.
    report.criteria.dimensions <<
      DfareportingUtils::API_NAMESPACE::SortedDimension.new(
        name: report_fields.dimensions.first.name
      )
  elsif report_fields.metrics.any?
    # Add a compatible metric to the report.
    report.criteria.metric_names << report_fields.metrics.first.name
  end

  puts format("\nUpdated report criteria (with compatible fields):\n%s", report.criteria.to_json)
end

def add_dimension_filters(service, profile_id, report)
  # Query advertiser dimension values for report run dates.
  dimension = DfareportingUtils::API_NAMESPACE::DimensionValueRequest.new(
    dimension_name: 'dfa:advertiser',
    start_date: report.criteria.date_range.start_date,
    end_date: report.criteria.date_range.end_date
  )

  values = service.query_dimension_value(profile_id, dimension)

  unless values.items.empty?
    # Add a value as a filter to the report criteria.
    report.criteria.dimension_filters = [values.items.first]
  end

  puts format("\nUpdated report criteria (with valid dimension filters):\n%s", report.criteria.to_json)
end

def insert_report_resource(service, profile_id, report)
  report = service.insert_report(profile_id, report)

  puts format("\nSuccessfully inserted new report with ID %s.", report.id)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id)

  create_report(args[:profile_id])
end
