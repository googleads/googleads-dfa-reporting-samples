#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2013, Google Inc. All Rights Reserved.
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
# This example illustrates how to get the compatible fields for a report.

require_relative 'dfareporting_utils'

def get_compatible_fields(dfareporting, args)
  # Get the report
  report = dfareporting.reports.get(
    :profileId => args[:profile_id],
    :reportId => args[:report_id]
  ).execute()

  # Get the compatible fields
  result = dfareporting.reports.compatible_fields.query(
    :profileId => args[:profile_id]
  ).body(report.data.to_hash).execute()

  compatibleFields = result.data.reportCompatibleFields

  # Display dimensions
  dimensions = compatibleFields.dimensions.map { |d| d.name }
  print_fields('Dimensions', dimensions)

  # Display metrics
  metrics = compatibleFields.metrics.map { |m| m.name }
  print_fields('Metrics', metrics)

  # Display dimension filters
  filters = compatibleFields.dimensionFilters.map { |f| f.name }
  print_fields('Dimension Filers', filters)

  # Display pivoted activity metrics
  activities = compatibleFields.pivotedActivityMetrics.map { |a| a.name }
  print_fields('Pivoted Activity Metrics', activities)
end

def print_fields(type, fields)
  puts 'Compatible %s' % type
  puts fields.join(', ')
  puts
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_compatible_fields(dfareporting, args)
end
