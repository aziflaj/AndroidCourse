package com.aziflaj.exchangeagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        // Object creation
        ParseObject progressObject = new ParseObject("Progress");
        progressObject.put("username", "aziflaj");
        progressObject.put("progress", 50);
        progressObject.saveInBackground();
        //*/

        //*
        // Object update
        ParseQuery<ParseObject> progressQuery = ParseQuery.getQuery("Progress");
        progressQuery.getInBackground("9fgdj9XNY8", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("progress", 56);
                    object.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //*/
    }
}
