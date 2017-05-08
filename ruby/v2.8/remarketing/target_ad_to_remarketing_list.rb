#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2017, Google Inc. All Rights Reserved.
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
# This example targets an ad to a remarketing list.
#
# The first targetable remarketing list, either owned by or shared to the ad's
# advertiser, will be used. To create a remarketing list, see
# create_remarketing_list.rb. To share a remarketing list with the ad's
# advertiser, see share_remarketing_list_to_advertiser.rb.

require_relative '../dfareporting_utils'

def target_ad_to_remarketing_list(profile_id, ad_id)
  # Authenticate and initialize API service.
  service = DfareportingUtils.get_service()

  # Retrieve the ad.
  ad = service.get_ad(profile_id, ad_id)

  # Retrieve a single targetable remarketing list for the ad.
  lists = service.list_targetable_remarketing_lists(profile_id,
      ad.advertiser_id, {:max_results => 1})

  if lists.targetable_remarketing_lists.any?
    list = lists.targetable_remarketing_lists.first

    # Update the ad with a list targeting expression.
    ad.remarketing_list_expression =
        DfareportingUtils::API_NAMESPACE::ListTargetingExpression.new({
          :expression => list.id
        })

    ad = service.update_ad(profile_id, ad)

    puts 'Ad %d updated to use remarketing list expression: "%s".' %
        [ad.id, ad.remarketing_list_expression.expression]
  else
    puts 'No targetable remarketing lists found for ad with ID %d.' % [ad_id]
  end
end

if __FILE__ == $0
  # Retrieve command line arguments.
  args = DfareportingUtils.get_arguments(ARGV, :profile_id, :ad_id)

  target_ad_to_remarketing_list(args[:profile_id], args[:ad_id])
end
