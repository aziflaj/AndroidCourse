package com.aziflaj.guessthecelebrity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ImageView celebrityImageView;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;

    Map<String, String> celebrityMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebrityImageView = (ImageView) findViewById(R.id.imageView);
        answer1 = (Button) findViewById(R.id.button);
        answer2 = (Button) findViewById(R.id.button2);
        answer3 = (Button) findViewById(R.id.button3);
        answer4 = (Button) findViewById(R.id.button4);

        FetchCelebritiesTask fetchCelebritiesTask = new FetchCelebritiesTask();
        try {
            Log.i("Getting celebrities", "start");
            celebrityMap = fetchCelebritiesTask.execute("http://www.posh24.com/celebrities").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
