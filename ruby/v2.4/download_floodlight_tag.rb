#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2015, Google Inc. All Rights Reserved.
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
# This example downloads activity tags for a given floodlight activity.

require_relative 'dfareporting_utils'

def download_floodlight_tag(dfareporting, args)
  # Construct the request
  result = dfareporting.floodlight_activities.generatetag(
    :floodlightActivityId => args[:activity_id],
    :profileId => args[:profile_id]
  ).execute()

  puts result.data.floodlight_activity_tag
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :activity_id)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  download_floodlight_tag(dfareporting, args)
end
