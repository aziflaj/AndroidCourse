# Exchangeagram
Exchangeagram is an Instagram copy application, using [Parse](http://parse.com/) for online storage.

## Set up the keys
To connect to the Parse API, you should initially create a Parse account, and then add your API keys in the `/src/main/res/values/api.xml` file. It should look like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="key.parse.app_id">YOUR_PARSE_APP_ID</string>
    <string name="key.parse.client">YOUR_PARSE_CLIENT_KEY</string>
</resources>
```