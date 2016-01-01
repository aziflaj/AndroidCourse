package com.aziflaj.hackernewsreader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    SimpleCursorAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.news_list_view);
        String fetchNewsUrl = Uri.parse(Utils.BASE_URL)
                .buildUpon()
                .appendPath(Utils.TOP_STORIES).toString();
        Log.d("LOG", fetchNewsUrl);
        FetchNewsTask task = new FetchNewsTask(this);

        try {
            cursor = task.execute(fetchNewsUrl).get();

            if (cursor != null) {

                adapter = new SimpleCursorAdapter(
                        this,
                        R.layout.list_item,
                        cursor,
                        new String[]{"title"},
                        new int[]{R.id.news_list_view_item},
                        0);
                Log.d("LOG", "Cursor not null");
                newsListView.setAdapter(adapter);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
