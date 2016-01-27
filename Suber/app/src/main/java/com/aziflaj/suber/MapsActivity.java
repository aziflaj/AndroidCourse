package com.aziflaj.suber;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private Marker mMarker;
    private LocationManager mLocationManager;
    private String mBestProvider;
    private TextView mStatusTextView;
    private Button mRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mStatusTextView = (TextView) findViewById(R.id.uber_request_status);
        mRequestButton = (Button) findViewById(R.id.request_btn);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mBestProvider = mLocationManager.getBestProvider(new Criteria(), true);

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

            Toast.makeText(MapsActivity.this, "Don't have location permission", Toast.LENGTH_LONG).show();
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

            Toast.makeText(MapsActivity.this, "Don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(mBestProvider, 400, 1, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MapsActivity.this, "Don't have location permission", Toast.LENGTH_LONG).show();
            return;
        }

        Location hereNow = mLocationManager.getLastKnownLocation(mBestProvider);
        if (hereNow != null) {
            onLocationChanged(hereNow);
        }

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hereNow, 17));
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
        if (tag.equals("request")) {
            requestUber();
            view.setTag("cancel");
        } else if (tag.equals("cancel")) {
            cancelUber();
            view.setTag("request");
        }
    }

    private void requestUber() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MapsActivity.this, "Don't have location permission", Toast.LENGTH_LONG).show();
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
            request.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        String gettingUber = getString(R.string.maps_status_finding);
                        mStatusTextView.setText(gettingUber);
                        String cancelButton = getString(R.string.maps_button_cancel_taxi);
                        mRequestButton.setText(cancelButton);
                    } else {
                        Toast.makeText(MapsActivity.this, "Couldn't call an Uber", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MapsActivity.this, "Couldn't call an Uber", Toast.LENGTH_SHORT).show();
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

                    String status = getString(R.string.maps_status_cancelled);
                    mStatusTextView.setText(status);
                    String findButton = getString(R.string.maps_button_call_taxi);
                    mRequestButton.setText(findButton);
                } else {
                    Toast.makeText(MapsActivity.this, "Couldn't cancel the last request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
