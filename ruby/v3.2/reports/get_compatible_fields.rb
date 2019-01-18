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
# This example illustrates how to get the compatible fields for a report.

require_relative '../dfareporting_utils'

def get_compatible_fields(profile_id, report_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Get the report.
  report = service.get_report(profile_id, report_id)

  # Get the compatible fields.
  result = service.query_report_compatible_field(profile_id, report)

  compatible_fields = result.report_compatible_fields

  # Display dimensions.
  dimensions = compatible_fields.dimensions.map(&:name)
  print_fields('Dimensions', dimensions)

  # Display metrics.
  metrics = compatible_fields.metrics.map(&:name)
  print_fields('Metrics', metrics)

  # Display dimension filters.
  filters = compatible_fields.dimension_filters.map(&:name)
  print_fields('Dimension Filers', filters)

  # Display pivoted activity metrics.
  activities = compatible_fields.pivoted_activity_metrics.map(&:name)
  print_fields('Pivoted Activity Metrics', activities)
end

def print_fields(type, fields)
  puts format('Compatible %s', type)
  puts fields.join(', ')
  puts
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  get_compatible_fields(args[:profile_id], args[:report_id])
end
