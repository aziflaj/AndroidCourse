package com.aziflaj.memorableplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> arrayList;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<LatLng> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_view);

        arrayList = new ArrayList<>();
        arrayList.add("Add a place...");

        locationList = new ArrayList<>();
        locationList.add(new LatLng(0, 0));

        arrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.list_item,
                arrayList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                Log.d("MainActivity", String.valueOf(position));
                if (position > 0) {
                    intent.putExtra("lat", locationList.get(position).latitude);
                    intent.putExtra("lng", locationList.get(position).longitude);
                    intent.putExtra("marker", arrayList.get(position));
                }

                startActivity(intent);
            }
        });
    }
}
