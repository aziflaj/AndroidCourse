package com.aziflaj.guessthecelebrity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchCelebritiesTask extends AsyncTask<String, Void, Map<String, String>> {
    public static final String LOG_TAG = FetchCelebritiesTask.class.getSimpleName();

    @Override
    protected Map<String, String> doInBackground(String... params) {
        Map<String, String> celebrityMap = new HashMap<>();
        String url = params[0];
        HttpURLConnection connection;
        List<String> imageUrls = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        try {
            URL connectionUrl = new URL(url);
            connection = (HttpURLConnection) connectionUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String content = sb.toString();
            // get the part below
            // <div class="channels_nav">
            String[] splitContent = content.split("<div class=\"channelList\">");
            content = splitContent[splitContent.length - 1];

            // get the part above
            // <div class="col-xs-12 col-sm-6 col-md-4">
            String[] reSplitContent = content.split("<div class=\"col-xs-12 col-sm-6 col-md-4\">");
            content = reSplitContent[0];


            Pattern celebrityImagePattern = Pattern.compile("src=\"(.*?)\"");
            Pattern celebrityNamePattern = Pattern.compile("alt=\"(.*?)\"");

            Matcher celebrityNameMatcher = celebrityNamePattern.matcher(content);
            while (celebrityNameMatcher.find()) {
                nameList.add(celebrityNameMatcher.group(1));
            }

            Matcher celebrityImageMatcher = celebrityImagePattern.matcher(content);
            while (celebrityImageMatcher.find()) {
                imageUrls.add(celebrityImageMatcher.group(1));
            }

            for (int i = 0; i < nameList.size(); i++) {
                celebrityMap.put(nameList.get(i), imageUrls.get(i));
            }

            return celebrityMap;

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed Url Exception: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "I/O Exception" + e.getMessage());
        }

        return null;
    }
}
