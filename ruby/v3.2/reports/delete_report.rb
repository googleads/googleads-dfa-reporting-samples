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
# This example illustrates how to delete a report.

require_relative '../dfareporting_utils'

def delete_report(profile_id, report_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service

  # Delete the report.
  service.delete_report(profile_id, report_id)

  puts format('Successfully deleted report with ID %d.', report_id)
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :report_id)

  delete_report(args[:profile_id], args[:report_id])
end
