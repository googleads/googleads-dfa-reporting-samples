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
# This example retrieves all targeting templates for your DCM user profile.
#
# Displays name, ID, and advertiser ID for each targeting template found.

require_relative '../dfareporting_utils'

def get_targeting_templates(profile_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.initialize_service

  token = nil
  loop do
    result = service.list_targeting_templates(profile_id,
      page_token: token,
      fields: 'nextPageToken,targetingTemplates(advertiserId,id,name)')

    # Display results.
    if result.targeting_templates.any?
      result.targeting_templates.each do |template|
        puts format(
          'Found targeting template with ID %d and name "%s" associated with ' \
          'advertiser ID %d.', template.id, template.name,
          template.advertiser_id
        )
      end

      token = result.next_page_token
    else
      # Stop paging if there are no more results.
      token = nil
    end

    break if token.to_s.empty?
  end
end

if $PROGRAM_NAME == __FILE__
  # Retrieve command line arguments.
  args = DfareportingUtils.parse_arguments(ARGV, :profile_id)

  get_targeting_templates(args[:profile_id])
end
