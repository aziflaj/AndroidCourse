package com.aziflaj.suber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {

    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwitch = (Switch) findViewById(R.id.rider_driver_switch);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signup(View view) {
        if (mSwitch.isChecked()) {
            Toast.makeText(getApplicationContext(), "Driver", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Rider", Toast.LENGTH_SHORT).show();
        }
    }
}
