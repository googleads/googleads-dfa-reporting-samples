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
# This example illustrates how to get a report resource.

require_relative 'dfareporting_utils'

def get_report(dfareporting, args)
  # Get the report
  result = dfareporting.reports.get(
    :profileId => args[:profile_id],
    :reportId => args[:report_id]
  ).execute()

  report = result.data

  # Display results
  puts '%s report with ID %d and name "%s" was found.' %
      [report.type, report.id, report.name]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_report(dfareporting, args)
end