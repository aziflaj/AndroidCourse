package com.aziflaj.hackernewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    NewsCursorAdapter adapter;
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

                adapter = new NewsCursorAdapter(this, cursor, 0);

                newsListView.setAdapter(adapter);
                newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                        intent.putExtra("url", view.getTag().toString());
                        startActivity(intent);
                    }
                });
            }


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
