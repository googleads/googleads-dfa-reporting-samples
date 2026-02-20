# DCM/DFA Reporting and Trafficking API Java Samples

This is a collection of samples written in Java which provide a starting place
for your experimentation into the [Campaign Manager 360 API](https://developers.google.com/doubleclick-advertisers/getting_started).

## Prerequisites

Please make sure that you're running Java 7+ and have Maven installed.

## Setup Authentication

This API uses OAuth 2.0. Learn more about [Google APIs and OAuth 2.0](https://developers.google.com/accounts/docs/OAuth2).

Or, if you'd like to dive right in, follow these steps.

1. Visit the [Google Cloud Console](https://console.developers.google.com) to register your application.
2. From the APIs and Services menu, [enable](https://support.google.com/googleapi/answer/6158841?hl=en) the "Campaign Manager 360 API".
3. Create Credentials
    - Click on **Credentials** in the left navigation menu
    - Click **+ Create Credentials** and select OAuth client ID.
    - Select "Desktop app" as the "Application type", then **Create**
6. Click **Download JSON** and save the file as `client_secrets.json` in the samples project directory.

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

1. Open a sample and fill in any prerequisite values. Required values will be declared as constants near the top of the file.

2. Run the sample
    
    1. Via eclipse, right-click on the project and select Run As > Java Application

3. Complete the authorization steps on your browser

4. Examine the console output, be inspired and start hacking an amazing new app!
