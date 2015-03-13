# DFA Reporting PHP Samples

This is a collection of samples written in PHP which provide a starting place
for your experimentation into the DFA Reporting API.

## Prerequisites

  - PHP 5.3+
  - JSON PHP extension
  - Composer

From the example directory, run `composer install` to install all dependecies.

## Setup Authentication

This API uses OAuth 2.0. Learn more about Google APIs and OAuth 2.0 here:
https://developers.google.com/accounts/docs/OAuth2

Or, if you'd like to dive right in, follow these steps.
 - Visit https://console.developers.google.com to register your application.
 - From the APIs & Auth -> APIs screen, activate access to "DFA Reporting API".
 - Click on "Credentials" in the left navigation menu
 - Click the button labeled "Create an OAuth2 client ID"
 - Select "Web Application" as the "Application type"
 - Configure javascript origins and redirect URIs
   - Authorized Javascript Origins: http://localhost
   - Authorized Redirect URIs: http://localhost/path/to/index.php
 - Click "Create client ID"
 - Click "Download JSON" and save the file as `client_secrets.json` in your
   examples directory

> #### Security alert!

> Always ensure that your client_secrets.json file is not publicly accessible.
> This file contains credential information which could allow unauthorized access
> to your DFA account.

## Running the Examples

I'm assuming you've checked out the code and are reading this from a local
directory. If not check out the code to a local directory.

1. Open the sample (`http://your/path/index.php`) in your browser

2. Complete the authorization steps

3. Select an example and provide the required information

3. Examine the response, be inspired and start hacking an amazing new app!
