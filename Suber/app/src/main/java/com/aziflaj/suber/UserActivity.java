package com.aziflaj.suber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView usernameTextView = (TextView) findViewById(R.id.username);
        String username = ParseUser.getCurrentUser().getUsername();
        usernameTextView.setText(username);
        Toast.makeText(getApplicationContext(), "Hello, " + username, Toast.LENGTH_LONG).show();
    }
}
