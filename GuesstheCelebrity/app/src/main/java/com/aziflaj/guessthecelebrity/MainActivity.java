package com.aziflaj.guessthecelebrity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static final String CELEBRITIES_URL = "http://www.posh24.com/celebrities";

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
            celebrityMap = fetchCelebritiesTask.execute(CELEBRITIES_URL).get();

            Random random = new Random();
            int rnd = random.nextInt(celebrityMap.size());

            String randomCelebrity = (String) celebrityMap.keySet().toArray()[rnd];
            Log.i("MainActivity", randomCelebrity);

            Bitmap bmp = new ImageDownloadTask().execute(celebrityMap.get(randomCelebrity)).get();
            celebrityImageView.setImageBitmap(bmp);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void answer(View view) {

    }
}
