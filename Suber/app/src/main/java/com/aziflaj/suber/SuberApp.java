package com.aziflaj.suber;

import android.app.Application;

import com.parse.Parse;

public class SuberApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
