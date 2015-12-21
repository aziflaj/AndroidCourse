package com.aziflaj.basicphrases;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playSound(View view) {
        int viewId = view.getId();
        String stringifiedViewId = view.getResources().getResourceEntryName(viewId);

        int soundId = getResources().getIdentifier(stringifiedViewId, "raw", "com.aziflaj.basicphrases");
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundId);
        mediaPlayer.start();
    }
}
