package com.aziflaj.suber;

import android.content.Intent;
import android.os.Bundle;
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

//        if (ParseUser.getCurrentUser() != null) {
//            startActivity(new Intent(MainActivity.this, UserActivity.class));
//            finish();
//        }

        mSwitch = (Switch) findViewById(R.id.rider_driver_switch);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signup(View view) {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    String type = mSwitch.isChecked() ? "driver" : "rider";
                    user.put("type", type);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                startActivity(new Intent(MainActivity.this, UserActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
