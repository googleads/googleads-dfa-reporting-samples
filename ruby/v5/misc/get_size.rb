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
# This example displays all sizes for a given width and height.

require_relative '../dfareporting_utils'

def get_size(profile_id, width, height)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  # Construct and execute the size request.
  result = service.list_sizes(profile_id,
    height: height,
    width: width)

  result.sizes.each do |size|
    puts format('Found size with ID %d.', size.id)
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id, :width, :height)

  get_size(args[:profile_id], args[:width], args[:height])
end
