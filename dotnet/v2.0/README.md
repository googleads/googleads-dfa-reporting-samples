# DCM/DFA Reporting and Trafficking API .NET Samples

This is a collection of samples written in C# which provide a starting place
for your experimentation into the DCM/DFA Reporting and Trafficking API.

## Prerequisites

Install Visual Studio 2012 and Nuget

## Setup Authentication

This API uses OAuth 2.0. Learn more about Google APIs and OAuth 2.0 here:
https://developers.google.com/accounts/docs/OAuth2

Or, if you'd like to dive right in, follow these steps.
 - Visit https://console.developers.google.com to register your application.
 - From the APIs & Auth -> APIs screen, activate access to "DCM/DFA Reporting and Trafficking API".
 - Click on "Credentials" in the left navigation menu
 - Click the button labeled "Create an OAuth2 client ID"
 - Give your application a name and click "Next"
 - Select "Installed Application" as the "Application type"
 - Under "Installed application type" select "Other"
 - Click "Create client ID"
 - Click "Download JSON" and save the file as `client_secrets.json` in the samples project directory

## Running the Examples

Once you've checked out the code:

1. Open the DfaReporting.Samples.csproj file.

2. Choose a sample and fill in any prerequisite values. Required values can be identified by the placeholder text: "ENTER_..._HERE".

3. Add the sample name as a command line argument and build/run the project.

4. Complete the authorization steps on your browser.

5. Examine the console output, be inspired and start hacking an amazing new app!
