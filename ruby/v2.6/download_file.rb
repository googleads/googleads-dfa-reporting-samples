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
# This example illustrates how to download a file

require_relative 'dfareporting_utils'

def download_file(dfareporting, args)
  # Get all reports
  result = dfareporting.files.get(
    :alt => 'media',
    :reportId => args[:report_id],
    :fileId => args[:file_id]
  ).execute()

  # Files.get returns a redirect to the actual report file
  real_url = result.headers['location']

  # Obtain the file and display results
  puts Faraday.get(real_url).body
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :report_id, :file_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  download_file(dfareporting, args)
end
