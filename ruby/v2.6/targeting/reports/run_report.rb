#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright (C) 2016 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# This example illustrates how to run a report.

require_relative '../dfareporting_utils'

def run_report(profile_id, report_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  # Run the report.
  result = service.run_report(profile_id, report_id)

  # Display results.
  puts 'File with ID %d and name "%s" for report ID %d is in status "%s".' %
      [result.id, result.file_name, result.report_id, result.status]
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  run_report(args[:profile_id], args[:report_id])
end
