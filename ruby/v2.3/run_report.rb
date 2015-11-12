# Copyright (C) 2013 Google Inc.
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

# This example illustrates how to run a report.
require_relative 'dfareporting_utils'

def run_report(dfareporting, args)
  # Run the report
  result = dfareporting.reports.run(
    :profileId => args[:profile_id],
    :reportId => args[:report_id]
  ).execute()

  file = result.data

  # Display results
  puts 'File with ID %d and name "%s" for report ID %d is in status "%s".' %
      [file.id, file.fileName, file.reportId, file.status]
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  run_report(dfareporting, args)
end
