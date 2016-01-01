package com.aziflaj.hackernewsreader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NewsCursorAdapter extends CursorAdapter {
    public NewsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view;

        tv.setText(cursor.getString(cursor.getColumnIndex("title")));
        tv.setTag(cursor.getString(cursor.getColumnIndex("url")));
    }
}
