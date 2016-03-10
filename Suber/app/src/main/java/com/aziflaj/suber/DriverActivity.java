package com.aziflaj.suber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {
    public static final String RIDER_USERNAME_EXTRA = "RIDER_USERNAME";
    public static final String RIDER_LATITUDE_EXTRA = "RIDER_LATITUDE";
    public static final String RIDER_LONGITUDE_EXTRA = "RIDER_LONGITUDE";

    private ArrayList<String> mRiderDistances;
    private ArrayList<String> mRiderUsernames;
    private ArrayList<ParseGeoPoint> mRiderLocations;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        ListView mRiderListView = (ListView) findViewById(R.id.rider_list_view);
        mRiderDistances = new ArrayList<>();
        mRiderUsernames = new ArrayList<>();
        mRiderLocations = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRiderDistances);
        mRiderListView.setAdapter(mAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String mBestProvider = mLocationManager.getBestProvider(new Criteria(), true);
        final Location last = mLocationManager.getLastKnownLocation(mBestProvider);
        if (last != null) {
            ParseGeoPoint driverLocation = new ParseGeoPoint(last.getLatitude(), last.getLongitude());
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
            query.whereWithinKilometers("location", driverLocation, 100);
            query.whereDoesNotExist("driverUsername");
            query.setLimit(100);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        mRiderDistances.clear();
                        mRiderUsernames.clear();
                        mRiderLocations.clear();
                        for (ParseObject o : objects) {
                            mRiderUsernames.add(String.valueOf(o.get("riderUsername")));
                            ParseGeoPoint location = o.getParseGeoPoint("location");
                            double distance = location.distanceInKilometersTo(new ParseGeoPoint(last.getLatitude(), last.getLongitude()));
                            mRiderDistances.add(String.format("%.3f km", distance));
                            mRiderLocations.add(location);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        mRiderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DriverActivity.this, DriverMapActivity.class);
                intent.putExtra(RIDER_USERNAME_EXTRA, mRiderUsernames.get(position));
                intent.putExtra(RIDER_LATITUDE_EXTRA, mRiderLocations.get(position).getLatitude());
                intent.putExtra(RIDER_LONGITUDE_EXTRA, mRiderLocations.get(position).getLongitude());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ParseUser.logOut();
                startActivity(new Intent(DriverActivity.this, MainActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}