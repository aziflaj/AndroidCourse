package com.aziflaj.notes;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {
    EditText newNoteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        newNoteEditText = (EditText) findViewById(R.id.new_note_edit_text);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PreferenceHelper helper = new PreferenceHelper(this);
                helper.addNote(newNoteEditText.getText().toString());
                this.finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
