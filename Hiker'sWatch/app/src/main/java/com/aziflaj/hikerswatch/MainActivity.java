package com.aziflaj.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView accuracyTextView;
    TextView speedTextView;
    TextView bearingTextView;
    TextView altitudeTextView;
    TextView addressTextView;

    LocationManager mLocationManager;
    String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = (TextView) findViewById(R.id.lat_text_view);
        longitudeTextView = (TextView) findViewById(R.id.lng_text_view);
        accuracyTextView = (TextView) findViewById(R.id.acr_text_view);
        speedTextView = (TextView) findViewById(R.id.speed_text_view);
        bearingTextView = (TextView) findViewById(R.id.bearing_text_view);
        altitudeTextView = (TextView) findViewById(R.id.alt_text_view);
        addressTextView = (TextView) findViewById(R.id.address_text_view);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = mLocationManager.getBestProvider(new Criteria(), false);
        Log.d("MainActivity", locationProvider);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);

        }

        Location last = mLocationManager.getLastKnownLocation(locationProvider);

        if (last == null) {
            Log.d("MainActivity", "last known location is null");
        } else {
            onLocationChanged(last);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);

        }

        mLocationManager.requestLocationUpdates(locationProvider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);

        }

        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location loc) {
        latitudeTextView.setText("Latitude: " + loc.getLatitude());
        longitudeTextView.setText("Longitude: " + loc.getLongitude());
        accuracyTextView.setText("Accuracy: " + loc.getAccuracy() + "m");
        speedTextView.setText("Speed: " + loc.getSpeed() + "m/s");
        bearingTextView.setText("Bearing: " + loc.getBearing());
        altitudeTextView.setText("Altitude: " + loc.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (addressList.size() > 0) {
                Address here = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < here.getMaxAddressLineIndex(); i++) {
                    sb.append(here.getAddressLine(i));
                }
                addressTextView.setText(sb.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
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
}
