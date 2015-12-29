# Memorable Places

## Set Up

1. Obtain an Maps API key
2. Create a file `app/src/debug/res/values/google_maps_api.xml` with this content:

```xml
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
        YOUR_KEY_HERE
    </string>
</resources>
```