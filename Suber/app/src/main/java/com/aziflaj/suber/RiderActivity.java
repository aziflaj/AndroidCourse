package com.aziflaj.suber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class RiderActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    public static final String TAG = RiderActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker mMarker;
    private Marker mDriverMarker;
    private LocationManager mLocationManager;
    private String mBestProvider;
    private TextView mStatusTextView;
    private Button mRequestButton;

    private String mDriverUsername;
    boolean requested = false;
    RelativeLayout mapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        mStatusTextView = (TextView) findViewById(R.id.uber_request_status);
        mRequestButton = (Button) findViewById(R.id.request_btn);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mBestProvider = mLocationManager.getBestProvider(new Criteria(), true);

        mapLayout = (RelativeLayout) findViewById(R.id.rider_map_layout);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        mLocationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(mBestProvider, 400, 1, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rider_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ParseUser.logOut();
                startActivity(new Intent(RiderActivity.this, MainActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "You don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        Location hereNow = mLocationManager.getLastKnownLocation(mBestProvider);
        if (hereNow != null) {
            onLocationChanged(hereNow);
        }

        ParseQuery<ParseObject> query = new ParseQuery<>("Requests");
        query.whereEqualTo("riderUsername", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject obj : objects) {
                        if (obj.get("driverUsername") != null) {
                            mDriverUsername = obj.getString("driverUsername");
                            mStatusTextView.setText(getString(R.string.rider_status_driver_found));
                            mRequestButton.setText(getString(R.string.rider_button_show_taxi));
                            mRequestButton.setTag("found");
                        } else {
                            mStatusTextView.setText(getString(R.string.rider_status_finding));
                            mRequestButton.setText(getString(R.string.rider_button_cancel_taxi));
                            mRequestButton.setTag("cancel");
                        }
                    }
                }
            }
        });

        mLocationManager.requestLocationUpdates(mBestProvider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMarker != null) {
            mMarker.remove();
        }

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng hereNow = new LatLng(lat, lng);
        mMarker = mMap.addMarker(new MarkerOptions().position(hereNow));
        if (requested) {
            mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(mMarker.getPosition())
                            .include(mDriverMarker.getPosition())
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                }
            });
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hereNow, 17));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void getTaxi(View view) {
        String tag = view.getTag().toString();
        switch (tag) {
            case "request":
                requestUber();
                view.setTag("cancel");
                break;
            case "cancel":
                cancelUber();
                view.setTag("request");
                break;
            case "found":
                showUberLocation();
                break;
        }
    }

    private void requestUber() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        Location hereNow = mLocationManager.getLastKnownLocation(mBestProvider);

        if (hereNow != null) {
            ParseObject request = new ParseObject("Requests");
            request.put("riderUsername", ParseUser.getCurrentUser().getUsername());
            ParseACL acl = new ParseACL();
            acl.setPublicWriteAccess(true);
            acl.setPublicReadAccess(true);
            request.setACL(acl);
            ParseGeoPoint herePoint = new ParseGeoPoint(hereNow.getLatitude(), hereNow.getLongitude());
            request.put("location", herePoint);
            Log.d(TAG, String.format("requestUber: location (%.2f, %.2f)", herePoint.getLatitude(), herePoint.getLongitude()));
            request.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        String gettingUber = getString(R.string.rider_status_finding);
                        mStatusTextView.setText(gettingUber);
                        String cancelButton = getString(R.string.rider_button_cancel_taxi);
                        mRequestButton.setText(cancelButton);
                    } else {
                        Toast.makeText(RiderActivity.this, "Couldn't call an Uber", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Couldn't call an Uber", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelUber() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
        query.whereEqualTo("riderUsername", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject o : objects) {
                        o.deleteInBackground();
                    }

                    String status = getString(R.string.rider_status_cancelled);
                    mStatusTextView.setText(status);
                    String findButton = getString(R.string.rider_button_call_taxi);
                    mRequestButton.setText(findButton);
                } else {
                    Toast.makeText(RiderActivity.this, "Couldn't cancel the last request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUberLocation() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", mDriverUsername);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseUser driver = objects.get(0);
                    ParseGeoPoint driverLocation = driver.getParseGeoPoint("userLocation");

                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }

                    mDriverMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    requested = true;
                }
            }
        });
    }
}