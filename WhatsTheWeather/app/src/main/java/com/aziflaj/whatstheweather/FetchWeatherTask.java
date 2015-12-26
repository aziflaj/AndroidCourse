package com.aziflaj.whatstheweather;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchWeatherTask extends AsyncTask<String, Void, String> {
    TextView mTextView;

    public FetchWeatherTask(TextView resultTextView) {
        mTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JSONObject jsonResponse = new JSONObject(sb.toString());
            JSONArray weatherArray = jsonResponse.getJSONArray("weather");

            StringBuilder sbResult = new StringBuilder();

            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject object = weatherArray.getJSONObject(i);
                sbResult
                        .append(object.getString("main"))
                        .append(": ")
                        .append(object.getString("description"))
                        .append("\n");
            }

            return sbResult.toString();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);

        mTextView.setText(result);
    }
}
