#!/usr/bin/python
#
# Copyright 2018 Google Inc. All Rights Reserved.
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

"""An end-to-end example of how to find and download a report file."""

import argparse
import io
import os
import sys

import dfareporting_utils
from googleapiclient import http
from oauth2client import client

# Declare command-line flags.
argparser = argparse.ArgumentParser(add_help=False)
argparser.add_argument(
    'profile_id', type=int, help='The ID of the profile to use')
argparser.add_argument(
    'report_id', type=int, help='The ID of the report to get a file for')

# Chunk size to use when downloading report files. Defaults to 32MB.
CHUNK_SIZE = 32 * 1024 * 1024


def main(argv):
  # Retrieve command line arguments.
  flags = dfareporting_utils.get_arguments(argv, __doc__, parents=[argparser])

  # Authenticate and construct service.
  service = dfareporting_utils.setup(flags)

  profile_id = flags.profile_id
  report_id = flags.report_id

  try:
    # 1. Find a file to download.
    report_file = find_file(service, profile_id, report_id)

    if report_file:
      # 2. (optional) Generate browser URL.
      generate_browser_url(service, report_id, report_file['id'])

      # 3. Directly download the file.
      direct_download_file(service, report_id, report_file['id'])
    else:
      print(
          'No file found for profile ID %d and report ID %d.'
          % (profile_id, report_id)
      )

  except client.AccessTokenRefreshError:
    print('The credentials have been revoked or expired, please re-run the '
          'application to re-authorize')


def find_file(service, profile_id, report_id):
  """Finds a report file to download."""
  target = None

  request = (
      service.reports()
      .files()
      .list(profileId=str(profile_id), reportId=str(report_id))
  )

  while True:
    response = request.execute()

    for report_file in response['items']:
      if is_target_file(report_file):
        target = report_file
        break

    if not target and response['items'] and response['nextPageToken']:
      request = service.reports().files().list_next(request, response)
    else:
      break

  if target:
    print(
        'Found file %s with filename "%s".' % (target['id'], target['fileName'])
    )
    return target

  print(
      'Unable to find file for profile ID %d and report ID %d.'
      % (profile_id, report_id)
  )
  return None


def is_target_file(report_file):
  # Provide custom validation logic here.
  # For example purposes, any available file is considered valid.
  return report_file['status'] == 'REPORT_AVAILABLE'


def generate_browser_url(service, report_id, file_id):
  """Prints the browser download URL for the file."""
  report_file = (
      service.files()
      .get(reportId=str(report_id), fileId=str(file_id))
      .execute()
  )
  browser_url = report_file['urls']['browserUrl']

  print('File %s has browser URL: %s.' % (report_file['id'], browser_url))


def direct_download_file(service, report_id, file_id):
  """Downloads a report file to disk."""
  # Retrieve the file metadata.
  report_file = service.files().get(
      reportId=report_id, fileId=file_id).execute()

  if report_file['status'] == 'REPORT_AVAILABLE':
    # Prepare a local file to download the report contents to.
    out_file = io.FileIO(generate_file_name(report_file), mode='wb')

    # Create a get request.
    request = service.files().get_media(reportId=report_id, fileId=file_id)

    # Create a media downloader instance.
    # Optional: adjust the chunk size used when downloading the file.
    downloader = http.MediaIoBaseDownload(
        out_file, request, chunksize=CHUNK_SIZE)

    # Execute the get request and download the file.
    download_finished = False
    while not download_finished:
      _, download_finished = downloader.next_chunk()

    print('File %s downloaded to %s' % (report_file['id'],
                                        os.path.realpath(out_file.name)))


def generate_file_name(report_file):
  """Generates a report file name based on the file metadata."""
  # If no filename is specified, use the file ID instead.
  file_name = report_file['fileName'] or report_file['id']
  extension = '.csv' if report_file['format'] == 'CSV' else '.xml'
  return file_name + extension


if __name__ == '__main__':
  main(sys.argv)
