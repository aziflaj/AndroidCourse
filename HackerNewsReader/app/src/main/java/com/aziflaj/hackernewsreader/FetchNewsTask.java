package com.aziflaj.hackernewsreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchNewsTask extends AsyncTask<String, Void, Cursor> {
    private Context mContext;
    private SQLiteDatabase mDb;

    public FetchNewsTask(Context context) {
        mContext = context;

        mDb = mContext.openOrCreateDatabase(Utils.DB_NAME, Context.MODE_PRIVATE, null);
        mDb.execSQL(Utils.DROP_TABLES_SQL);
        mDb.execSQL(Utils.CREATE_TABLES_SQL);
    }

    @Override
    protected Cursor doInBackground(String... params) {
        HttpURLConnection topNewsCon;
        HttpURLConnection singleNewsCon;

        try {
            URL topNewsUrl = new URL(params[0]);
            topNewsCon = (HttpURLConnection) topNewsUrl.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(topNewsCon.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JSONArray array = new JSONArray(sb.toString());
            int max = array.length() > 10 ? 10 : array.length();

            for (int i = 0; i < max; i++) {
                int id = array.getInt(i);
                Uri singleNewsUri = Uri.parse(Utils.BASE_URL).buildUpon()
                        .appendPath(String.format(Utils.ITEM_FORMAT, id))
                        .build();
                URL singleNewsUrl = new URL(singleNewsUri.toString());
                singleNewsCon = (HttpURLConnection) singleNewsUrl.openConnection();

                BufferedReader singleBr = new BufferedReader(new InputStreamReader(singleNewsCon.getInputStream()));
                StringBuilder singleBuilder = new StringBuilder();

                while ((line = singleBr.readLine()) != null) {
                    singleBuilder.append(line).append("\n");
                }

                JSONObject json = new JSONObject(singleBuilder.toString());
                if (json.has("url")) {
                    String title = escape(json.getString("title"));
                    String url = escape(json.getString("url"));

                    SQLiteStatement stmt = mDb.compileStatement(Utils.INSERT_STATEMENT);
                    stmt.bindString(1, String.valueOf(id));
                    stmt.bindString(2, title);
                    stmt.bindString(3, url);

                    stmt.execute();
                }
            }

            return mDb.rawQuery(Utils.SELECT_SQL, null);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String escape(String s) {
        s = s.replace("'", "\'");
        return s;
    }
}
