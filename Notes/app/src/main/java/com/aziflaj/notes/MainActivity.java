package com.aziflaj.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String INTENT_KEY = "index";
    public static final String SHARED_KEY = "notes";

    static ListView sNotesListView;
    static ArrayAdapter<String> sArrayAdapter;
    static ArrayList<String> sNotesList = new ArrayList<>();
    static Set<String> sNotesSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sNotesList.add("");

                SharedPreferences prefs = getSharedPreferences("com.aziflaj.notes", Context.MODE_PRIVATE);

                if (sNotesSet == null) {
                    sNotesSet = new HashSet<String>();
                } else {
                    sNotesSet.clear();
                }

                sNotesSet.addAll(sNotesList);
                sArrayAdapter.notifyDataSetChanged();

                prefs.edit().remove(SHARED_KEY).apply();
                prefs.edit().putStringSet(SHARED_KEY, sNotesSet).apply();

                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra(INTENT_KEY, sNotesList.size() -1);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences("com.aziflaj.notes", Context.MODE_PRIVATE);
        sNotesSet = prefs.getStringSet(SHARED_KEY, null);

        sNotesList.clear();
        if (sNotesSet != null) {
            sNotesList.addAll(sNotesSet);
        } else {
            sNotesList.add("Example note...");
            sNotesSet = new HashSet<>();
            sNotesSet.addAll(sNotesList);
            prefs.edit().putStringSet(SHARED_KEY, sNotesSet).apply();
        }

        sNotesListView = (ListView) findViewById(R.id.notes_list_view);
        sArrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.list_item,
                sNotesList);

        sNotesListView.setAdapter(sArrayAdapter);

        sNotesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra(INTENT_KEY, position);
                startActivity(intent);
            }
        });

        sNotesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Do you want to delete this note?")
                        .setMessage("This action can not be undone")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sNotesList.remove(position);
                                SharedPreferences prefs = MainActivity.this.getSharedPreferences("com.aziflaj.notes", Context.MODE_PRIVATE);

                                if (sNotesSet == null) {
                                    sNotesSet = new HashSet<>();
                                } else {
                                    sNotesSet.clear();
                                }

                                sNotesSet.addAll(sNotesList);
                                prefs.edit().remove(SHARED_KEY).apply();
                                prefs.edit().putStringSet(SHARED_KEY, sNotesSet).apply();
                                sArrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });
    }
}
