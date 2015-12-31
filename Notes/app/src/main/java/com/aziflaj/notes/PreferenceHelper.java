package com.aziflaj.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class PreferenceHelper {
    public static final String KEY = "notes";
    private Set<String> notes;
    private SharedPreferences prefs;

    public PreferenceHelper(Context context) {
        notes = new HashSet<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void addNote(String note) {
        notes.add(note);
        prefs.edit().putStringSet(KEY, notes).apply();
    }

    public Set<String> getNotes() {
        return prefs.getStringSet(KEY, new HashSet<String>());
    }

}
