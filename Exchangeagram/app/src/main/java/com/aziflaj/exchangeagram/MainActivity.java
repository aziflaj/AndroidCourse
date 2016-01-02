package com.aziflaj.exchangeagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mSessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, UserListActivity.class));
            finish();
        }

        mUsernameEditText = (EditText) findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mSessionButton = (Button) findViewById(R.id.session_button);

        mUsernameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    mPasswordEditText.requestFocus();
                }
                return true;
            }
        });

        mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    handleSignUpOrLogIn(mSessionButton);
                }
                return true;
            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void handleSignUpOrLogIn(View view) {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Username or password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (view.getTag().toString().equals("login")) {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, UserListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Signed up", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, UserListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void switchSession(View view) {
        String sessionTag = mSessionButton.getTag().toString();
        TextView self = (TextView) view;

        if (sessionTag.equals("login")) {
            // make signup
            mSessionButton.setText(R.string.session_button_signup);
            self.setText(R.string.session_text_login);
            mSessionButton.setTag("signup");
        } else {
            mSessionButton.setText(R.string.session_button_login);
            self.setText(R.string.session_text_signup);
            mSessionButton.setTag("login");
        }
    }
}
