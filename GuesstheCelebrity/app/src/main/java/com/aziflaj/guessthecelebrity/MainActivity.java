package com.aziflaj.guessthecelebrity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
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
    ArrayList<Button> buttons = new ArrayList<>();
    String randomCelebrity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebrityImageView = (ImageView) findViewById(R.id.imageView);
        answer1 = (Button) findViewById(R.id.button);
        answer2 = (Button) findViewById(R.id.button2);
        answer3 = (Button) findViewById(R.id.button3);
        answer4 = (Button) findViewById(R.id.button4);
        buttons.add(answer1);
        buttons.add(answer2);
        buttons.add(answer3);
        buttons.add(answer4);
        buttons.trimToSize();

        FetchCelebritiesTask fetchCelebritiesTask = new FetchCelebritiesTask();
        try {
            celebrityMap = fetchCelebritiesTask.execute(CELEBRITIES_URL).get();

            playGame();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void answer(View view) {
        String tag = (String) view.getTag();

        if (tag.equals("correct")) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
        } else {
            String wrong = "Wrong! It was " + randomCelebrity;
            Toast.makeText(getApplicationContext(), wrong, Toast.LENGTH_LONG).show();
        }

        try {
            playGame();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void playGame() throws ExecutionException, InterruptedException {
        Random random = new Random();
        int rnd = random.nextInt(celebrityMap.size());

        randomCelebrity = (String) celebrityMap.keySet().toArray()[rnd];
        Log.i("MainActivity", randomCelebrity);

        Bitmap bmp = new ImageDownloadTask().execute(celebrityMap.get(randomCelebrity)).get();
        celebrityImageView.setImageBitmap(bmp);

        // pick a button
        int randomButton = random.nextInt(4);
        buttons.get(randomButton).setTag("correct");
        buttons.get(randomButton).setText(randomCelebrity);

        for (int i = 0; i < 4; i++) {
            if (i != randomButton) {
                buttons.get(i).setTag("wrong");
                buttons.get(i).setText(getDifferentCelebrity(randomCelebrity));
            }
        }
    }

    private String getDifferentCelebrity(String celebrity) {
        String otherCelebrity;
        Random rnd = new Random();

        do {
            int i = rnd.nextInt(celebrityMap.size());
            otherCelebrity = (String) celebrityMap.keySet().toArray()[i];
        } while (otherCelebrity.equals(celebrity));

        return otherCelebrity;
    }
}
