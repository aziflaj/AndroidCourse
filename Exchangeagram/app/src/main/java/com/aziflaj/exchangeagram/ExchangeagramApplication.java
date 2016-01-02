package com.aziflaj.exchangeagram;

import android.app.Application;

import com.parse.Parse;

public class ExchangeagramApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
