#!/usr/bin/python
#
# Copyright 2015 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the 'License');
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an 'AS IS' BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""An end-to-end example of how to create and configure a standard report."""

import argparse
from datetime import date
from datetime import timedelta
import sys

import dfareporting_utils
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int, help='The ID of the profile to create a report for')


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id

  try:
    # 1. Create a report resource.
    report = create_report_resource()

    # 2. Define the report criteria.
    define_report_criteria(report)

    # 3. (optional) Look up compatible fields.
    find_compatible_fields(service, profile_id, report)

    # 4. Add dimension filters to the report criteria.
    add_dimension_filters(service, profile_id, report)

    # 5. Save the report resource.
    report = insert_report_resource(service, profile_id, report)

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


def create_report_resource():
  """Creates a report resource."""
  report = {
      # Set the required fields "name" and "type".
      'name': 'Example Standard Report',
      'type': 'STANDARD',
      # Set optional fields.
      'fileName': 'example_report',
      'format': 'CSV'
  }

  print 'Creating %s report resource with name "%s".' % (report['type'],
                                                         report['name'])

  return report


def define_report_criteria(report):
  """Defines a criteria for the report."""
  # Define a date range to report on. This example uses explicit start and end
  # dates to mimic the "LAST_30_DAYS" relative date range.
  end_date = date.today()
  start_date = end_date - timedelta(days=30)

  # Create a report criteria.
  criteria = {
      'dateRange': {
          'startDate': start_date.strftime('%Y-%m-%d'),
          'endDate': end_date.strftime('%Y-%m-%d')
      },
      'dimensions': [{
          'name': 'advertiser'
      }],
      'metricNames': ['clicks', 'impressions']
  }

  # Add the criteria to the report resource.
  report['criteria'] = criteria

  print '\nAdded report criteria:\n%s' % criteria


def find_compatible_fields(service, profile_id, report):
  """Finds and adds a compatible dimension/metric to the report."""
  fields = service.reports().compatibleFields().query(
      profileId=profile_id, body=report).execute()

  report_fields = fields['reportCompatibleFields']

  if report_fields['dimensions']:
    # Add a compatible dimension to the report.
    report['criteria']['dimensions'].append({
        'name': report_fields['dimensions'][0]['name']
    })
  elif report_fields['metrics']:
    # Add a compatible metric to the report.
    report['criteria']['metricNames'].append(
        report_fields['metrics'][0]['name'])

  print('\nUpdated report criteria (with compatible fields):\n%s' %
        report['criteria'])


def add_dimension_filters(service, profile_id, report):
  """Finds and adds a valid dimension filter to the report."""
  # Query advertiser dimension values for report run dates.
  request = {
      'dimensionName': 'advertiser',
      'endDate': report['criteria']['dateRange']['endDate'],
      'startDate': report['criteria']['dateRange']['startDate']
  }

  values = service.dimensionValues().query(
      profileId=profile_id, body=request).execute()

  if values['items']:
    # Add a value as a filter to the report criteria.
    report['criteria']['dimensionFilters'] = [values['items'][0]]

  print('\nUpdated report criteria (with valid dimension filters):\n%s' %
        report['criteria'])


def insert_report_resource(service, profile_id, report):
  """Inserts the report."""
  inserted_report = service.reports().insert(
      profileId=profile_id, body=report).execute()

  print('\nSuccessfully inserted new report with ID %s.' %
        inserted_report['id'])

  return inserted_report


if __name__ == '__main__':
  main(sys.argv)
