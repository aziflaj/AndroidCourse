package com.aziflaj.tweeter;

import android.app.Application;

import com.parse.Parse;

import timber.log.Timber;

public class TweeterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this);
        Timber.plant(new Timber.DebugTree());
    }
}
