package com.aziflaj.suber;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

public class SuberApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseUser.enableAutomaticUser();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}