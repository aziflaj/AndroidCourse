package com.aziflaj.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity implements TextWatcher {
    public static final String TAG = NoteActivity.class.getSimpleName();
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        EditText noteEditText = (EditText) findViewById(R.id.note_edit_text);

        Intent intent = getIntent();
        index = intent.getIntExtra(MainActivity.INTENT_KEY, -1);

        if (index > -1) {
            noteEditText.setText(MainActivity.sNotesList.get(index));
        }

        noteEditText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged: update index " + index);

        MainActivity.sNotesList.set(index, s.toString());
        MainActivity.sArrayAdapter.notifyDataSetChanged();

        MainActivity.sNotesSet.clear();
        MainActivity.sNotesSet.addAll(MainActivity.sNotesList);

        SharedPreferences prefs = getSharedPreferences("com.aziflaj.notes", Context.MODE_PRIVATE);
        prefs.edit().remove(MainActivity.SHARED_KEY).apply();
        prefs.edit().putStringSet(MainActivity.SHARED_KEY, MainActivity.sNotesSet).apply();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
