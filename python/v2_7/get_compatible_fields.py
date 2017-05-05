#!/usr/bin/python
#
# Copyright 2015 Google Inc. All Rights Reserved.
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

"""This example illustrates how to get the compatible fields for a report."""

import argparse
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int,
    help='The ID of the profile to use')
argparser.add_argument(
    'report_id', type=int,
    help='The ID of the report to get compatible fields for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  report_id = flags.report_id

  try:
    # Retrieve the specified report resource
    report = service.reports().get(
        profileId=profile_id, reportId=report_id).execute()

    compatible_fields_type = get_compatible_fields_type(report['type'])

    # Construct the request
    request = service.reports().compatibleFields().query(
        profileId=profile_id, body=report)

    # Execute the request and print response.
    response = request.execute()

    compatible_fields = response[compatible_fields_type]
    print_fields('Dimensions', compatible_fields['dimensions'])
    print_fields('Metrics', compatible_fields['metrics'])
    print_fields('Dimension Filters',
                 compatible_fields['dimensionFilters'])
    print_fields('Pivoted Activity Metrics',
                 compatible_fields['pivotedActivityMetrics'])

  except client.AccessTokenRefreshError:
    print ('The credentials have been revoked or expired, please re-run the '
           'application to re-authorize')


def get_compatible_fields_type(report_type):
  return {
      'CROSS_DIMENSION_REACH': 'crossDimensionReachReportCompatibleFields',
      'FLOODLIGHT': 'floodlightReportCompatibleFields',
      'PATH_TO_CONVERSION': 'pathToConversionReportCompatibleFields',
      'REACH': 'reachReportCompatibleFields'
  }.get(report_type, 'reportCompatibleFields')


def print_fields(field_type, fields):
  field_names = [field['name'] for field in fields]
  print 'Compatible %s\n%s\n\n' % (field_type, ','.join(field_names))


if __name__ == '__main__':
  main(sys.argv)
