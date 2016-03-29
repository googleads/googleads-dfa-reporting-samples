# DFA Reporting and Trafficking Python Samples

This is a collection of samples written in Python which provide a starting place
for your experimentation into the DFA Reporting and Trafficking API.

## Prerequisites

Please make sure that you're running the latest version of Python 2 and have pip
installed. Use the following command from the samples directory to install all
dependencies:

```Batchfile
$ pip install --upgrade google-api-python-client
```

## Setup Authentication

This API uses OAuth 2.0. Learn more about Google APIs and OAuth 2.0 here:
https://developers.google.com/accounts/docs/OAuth2

Or, if you'd like to dive right in, follow these steps.
 - Visit https://console.developers.google.com to register your application.
 - From the API Manager -> Google APIs screen, activate access to "DCM/DFA Reporting and Trafficking API".
 - Click on "Credentials" in the left navigation menu
 - Click the button labeled "Create credentials" and select "OAuth Client ID"
 - Select "Other" as the "Application type", then "Create"
 - From the Credentials page, click "Download JSON" next to the client ID you just created and save the file as `client_secrets.json` in the samples project directory

## Running the Examples

I'm assuming you've checked out the code and are reading this from a local
directory. If not check out the code to a local directory.

1. Start up a sample, e.g.

        $ python create_report.py <profile_id>

2. Complete the authorization steps on your browser

3. Examine your shell output, be inspired and start hacking an amazing new app!
