package com.aziflaj.suber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (ParseUser.getCurrentUser() == null) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Toast.makeText(getApplicationContext(), "Anonymous user created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (ParseUser.getCurrentUser().get("role") != null) {
            redirectUser();
        }

        mSwitch = (Switch) findViewById(R.id.rider_driver_switch);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void getStarted(View view) {
        String role = mSwitch.isChecked() ? "driver" : "rider";
        ParseUser.getCurrentUser().put("role", role);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    redirectUser();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectUser() {
        if (ParseUser.getCurrentUser().get("role").equals("rider")) {
            startActivity(new Intent(MainActivity.this, RiderActivity.class));
            finish();
        } else if (ParseUser.getCurrentUser().get("role").equals("driver")) {
            startActivity(new Intent(MainActivity.this, DriverActivity.class));
            finish();
        }
    }
}