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
# This example illustrates how to create a report

require_relative 'dfareporting_utils'

def create_report(dfareporting, args)
  # Create a new report resource to insert
  report = {
    :name => 'Example Standard Report',
    :type => 'STANDARD',
    :criteria => {
      :dateRange => {:relativeDateRange => 'YESTERDAY'},
      :dimensions => [{:name => 'dfa:campaign'}],
      :metricNames => ['dfa:clicks']
    }
  }

  # Insert the report
  report = dfareporting.reports.insert(
    :profileId => args[:profile_id]
  ).body(report).execute()

  # Display results
  puts 'Standard report with ID %d and name "%s" was created.' %
      [report.data.id, report.data.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  create_report(dfareporting, args)
end
