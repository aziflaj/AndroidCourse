package com.aziflaj.suber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
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
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    public static final String TAG = DriverMapActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker driverMarker;
    private Marker riderMarker;
    private LocationManager mLocationManager;
    private String mBestProvider;
    private RelativeLayout mapLayout;

    double riderLat;
    double riderLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mBestProvider = mLocationManager.getBestProvider(new Criteria(), true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapLayout = (RelativeLayout) findViewById(R.id.driver_map_layout);

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        riderLat = getIntent().getDoubleExtra(DriverActivity.RIDER_LATITUDE_EXTRA, 0.0);
        riderLong = getIntent().getDoubleExtra(DriverActivity.RIDER_LONGITUDE_EXTRA, 0.0);
        LatLng rider = new LatLng(riderLat, riderLong);
        riderMarker = mMap.addMarker(new MarkerOptions()
                .position(rider)
                .title("Rider")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

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

        mLocationManager.requestLocationUpdates(mBestProvider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(final Location location) {
        if (driverMarker != null) {
            driverMarker.remove();
        }

        ParseUser.getCurrentUser().put("userLocation", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
        ParseUser.getCurrentUser().saveInBackground();

        mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                double driverLat = location.getLatitude();
                double driverLong = location.getLongitude();
                LatLng hereNow = new LatLng(driverLat, driverLong);
                driverMarker = mMap.addMarker(new MarkerOptions()
                        .position(hereNow)
                        .title("You")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(riderMarker.getPosition())
                        .include(driverMarker.getPosition())
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });
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

    public void goBack(View vew) {
        startActivity(new Intent(DriverMapActivity.this, DriverActivity.class));
    }

    public void acceptRequest(View view) {
        String riderUsername = getIntent().getStringExtra(DriverActivity.RIDER_USERNAME_EXTRA);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
        query.whereEqualTo("riderUsername", riderUsername);
        query.whereDoesNotExist("driverUsername");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject o : objects) {
                        o.put("driverUsername", ParseUser.getCurrentUser().getUsername());
                        o.saveInBackground();
                    }

                    Intent directionIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + riderLat + "," + riderLong));
                    startActivity(directionIntent);
                }
            }
        });
    }
}
