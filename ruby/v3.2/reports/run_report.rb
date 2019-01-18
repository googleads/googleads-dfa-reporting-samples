#!/usr/bin/env ruby

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

# The following values control retry behavior while the report is processing.
# Minimum amount of time between polling requests. Defaults to 10 seconds.
MIN_RETRY_INTERVAL = 10
# Maximum amount of time between polling requests. Defaults to 10 minutes.
MAX_RETRY_INTERVAL = 10 * 60
# Maximum amount of time to spend polling. Defaults to 1 hour.
MAX_RETRY_ELAPSED_TIME = 60 * 60

def run_report(profile_id, report_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Run the report.
  report_file = service.run_report(profile_id, report_id)
  puts format('File with ID %s has been created.', report_file.id)

  # Wait for the report to finish processing.
  # An exponential backoff strategy is used to conserve request quota.
  interval = 0
  start_time = Time.now
  loop do
    report_file = service.get_file(report_id, report_file.id)

    status = report_file.status
    if status == 'REPORT_AVAILABLE'
      puts format('File status is %s, ready to download.', status)
      break
    elsif status != 'PROCESSING'
      puts format('File status is %s, processing failed.', status)
      break
    elsif Time.now - start_time > MAX_RETRY_ELAPSED_TIME
      puts 'File processing deadline exceeded.'
      break
    end

    interval = next_sleep_interval(interval)
    puts format('File status is %s, sleeping for %d seconds.', status, interval)
    sleep(interval)
  end
end

def next_sleep_interval(previous_interval)
  min_interval = [MIN_RETRY_INTERVAL, previous_interval].max
  max_interval = [MIN_RETRY_INTERVAL, previous_interval * 3].max
  [MAX_RETRY_INTERVAL, rand(min_interval..max_interval)].min
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  run_report(args[:profile_id], args[:report_id])
end
