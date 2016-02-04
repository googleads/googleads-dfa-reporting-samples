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
# This example displays all sizes for a given width and height.

require_relative 'dfareporting_utils'

def get_size(dfareporting, args)
  # Construct and execute the subaccount request
  result = dfareporting.sizes.list(
    :profileId => args[:profile_id],
    :height => args[:height],
    :width => args[:width]
  ).execute()

  result.data.sizes.each do |size|
    puts 'Found size with ID %d.' % size.id
  end
end

if __FILE__ == $0
  # Retrieve command line arguments
  args = DfaReportingUtils.get_arguments(ARGV, :profile_id, :height, :width)

  # Authenticate and initialize API service
  dfareporting = DfaReportingUtils.setup()

  get_size(dfareporting, args)
end
