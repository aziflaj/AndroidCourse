package com.aziflaj.memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LatLng coords;
    String marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);
        coords = new LatLng(lat, lng);
        marker = intent.getStringExtra("marker");

        setupMap();
    }

    private void setupMap() {
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (coords.latitude == coords.longitude && coords.latitude == 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 10));
        }

        mMap.addMarker(new MarkerOptions()
                .position(coords)
                .title(marker)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String marker = new Date().toString();

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                marker = addressList.get(0).getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(marker)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        MainActivity.arrayList.add(marker);
        MainActivity.locationList.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}
