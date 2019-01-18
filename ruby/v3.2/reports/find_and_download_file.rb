#!/usr/bin/env ruby

#
# Copyright:: Copyright 2018, Google Inc. All Rights Reserved.
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
# An end-to-end example of how to find and download a report file.

require_relative '../dfareporting_utils'

def find_and_download_file(profile_id, report_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # 1. Find a file to download.
  report_file = find_file(service, profile_id, report_id)

  if report_file
    # 2. (optional) Generate a browser URL.
    generate_browser_url(service, report_id, report_file.id)

    # 3. Directly download the file.
    direct_download_file(service, report_id, report_file.id)
  else
    puts format('No file found for profile ID %d and report ID %d.', profile_id, report_id)
  end
end

def find_file(service, profile_id, report_id)
  page_token = nil
  target = nil

  begin
    result = service.list_report_files(profile_id, report_id,
      page_token: page_token)

    result.items.each do |file|
      if is_target_file(file)
        target = file
        break
      end
    end

    page_token = (result.next_page_token if target.nil? && result.items.any?)
  end until page_token.to_s.empty?

  if target
    puts format('Found file %s with filename "%s".', target.id, target.file_name)
    return target
  end

  puts format('Unable to find file for profile ID %d and report ID %d.', profile_id, report_id)
  nil
end

def is_target_file(file)
  # Provide custom validation logic here.
  # For example purposes, any available file is considered valid.
  file.status == 'REPORT_AVAILABLE'
end

def generate_browser_url(service, report_id, file_id)
  report_file = service.get_file(report_id, file_id)
  browser_url = report_file.urls.browser_url

  puts format('File %s has browser URL: %s.', report_file.id, browser_url)
end

def direct_download_file(service, report_id, file_id)
  # Retrieve the file metadata.
  report_file = service.get_file(report_id, file_id)

  if report_file.status == 'REPORT_AVAILABLE'
    # Prepare a local file to download the report contents to.
    File.open(generate_file_name(report_file), 'w') do |out_file|
      # Execute the download request. Providing a download destination
      # retrieves the file contents rather than the file metadata.
      service.get_file(report_id, file_id, download_dest: out_file)

      puts format('File %s downloaded to %s', file_id, File.absolute_path(out_file.path))
    end
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
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  find_and_download_file(args[:profile_id], args[:report_id])
end
