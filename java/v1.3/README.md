# DFA Reporting Java Samples

This is a collection of samples written in Java which provide a starting place
for your experimentation into the DFA Reporting API.

## Prerequisites

Please make sure that you're running Java 6+ and have maven installed.

## Setup Authentication

This API uses OAuth 2.0. Learn more about Google APIs and OAuth 2.0 here:
https://developers.google.com/accounts/docs/OAuth2

Or, if you'd like to dive right in, follow these steps.
 - Visit https://console.developers.google.com to register your application.
 - From the APIs & Auth -> APIs screen, activate access to "DFA Reporting API".
 - Click on "Credentials" in the left navigation menu
 - Click the button labeled "Create an OAuth2 client ID"
 - Give your application a name and click "Next"
 - Select "Installed Application" as the "Application type"
 - Under "Installed application type" select "Other"
 - Click "Create client ID"
 - Click "Download JSON" and save the file as `client_secrets.json` in the samples project directory

## Set up your environment ##
### Via the command line ###

1. Execute the following command:

```Batchfile
$ mvn compile
```

### Via Eclipse ###

1. Setup Eclipse preferences:
    1. Window > Preferences .. (or on Mac, Eclipse > Preferences)
    2. Select Maven
    3. Select "Download Artifact Sources"
    4. Select "Download Artifact JavaDoc"
2. Import the sample project
    1. File > Import...
    2. Select General > Existing Project into Workspace and click "Next"
    3. Click "Browse" next to "Select root directory", find the sample directory and click "Next"
    4. Click "Finish"

## Running the Examples

Once you've checked out the code:

1. Run DfaReportingSample.java
    1. Via the command line, execute the following command:

    ```Batchfile
    $ mvn -q exec:java
    ```
    2. Via eclipse, right-click on the project and select Run As > Java Application


2. Complete the authorization steps on your browser

3. Examine the console output, be inspired and start hacking an amazing new app!
