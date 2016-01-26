# Suber
Suber is an Uber clone, utilizing [Parse](http://parse.com) capabilities on Geolocation. The app creates an anonymous user, who can choose between being a driver or a rider.

## Parse API keys
To connect to the Parse API, you should initially create a Parse account, and then add your API keys in the `/src/main/res/values/api.xml` file. It should look like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="key.parse.app_id">YOUR_PARSE_APP_ID</string>
    <string name="key.parse.client">YOUR_PARSE_CLIENT_KEY</string>
</resources>
```

## Google Maps API keys
To use Google Maps in the application (the location of the user is shown on a map), you need to create an API key for Google Maps. During development, this key goes to `/src/debug/res/values/google_maps_api.xml` (the file is `.gitignore`d).