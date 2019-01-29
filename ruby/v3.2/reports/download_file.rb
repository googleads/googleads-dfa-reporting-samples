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
# This example illustrates how to download a file

require_relative '../dfareporting_utils'

def download_file(report_id, file_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Retrieve the file metadata.
  report_file = service.get_file(report_id, file_id)
  return unless report_file.status == 'REPORT_AVAILABLE'

  # Prepare a local file to download the report contents to.
  File.open(generate_file_name(report_file), 'w') do |out_file|
    # Execute the download request. Providing a download destination
    # retrieves the file contents rather than the file metadata.
    service.get_file(report_id, file_id, download_dest: out_file)

    puts format('File %s downloaded to %s', file_id,
      File.absolute_path(out_file.path))
  end
end

def generate_file_name(report_file)
  file_name = report_file.file_name
  # If no filename is specified, use the file ID instead.
  file_name = report_file.id.to_s if file_name.empty?
  extension = report_file.format == 'CSV' ? '.csv' : '.xml'
  file_name + extension
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :report_id, :file_id)

  download_file(args[:report_id], args[:file_id])
end
