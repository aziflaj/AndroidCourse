package com.aziflaj.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        HttpURLConnection connection = null;

        try {
            URL connectionUrl = new URL(url);
            connection = (HttpURLConnection) connectionUrl.openConnection();

            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
