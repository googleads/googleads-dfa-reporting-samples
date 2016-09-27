#!/usr/bin/env ruby
# Encoding: utf-8
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
# This example illustrates how to patch a report

require_relative '../dfareporting_utils'

def patch_report(profile_id, report_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  # Create a report resource with the fields to patch.
  report = DfareportingUtils::API_NAMESPACE::Report.new({
    :criteria => DfareportingUtils::API_NAMESPACE::Report::Criteria.new({
      :date_range => DfareportingUtils::API_NAMESPACE::DateRange.new({
        :start_date => nil,
        :end_date => nil,
        :relative_date_range => 'YESTERDAY'
      })
    })
  })

  # Patch the report.
  result = service.patch_report(profile_id, report_id, report)

  # Display results.
  puts '%s report with ID %d and name "%s" has been patched.' %
      [result.type, result.id, result.name]
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  patch_report(args[:profile_id], args[:report_id])
end
