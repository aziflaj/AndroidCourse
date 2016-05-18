package com.aziflaj.tweeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    Button sessionBtn;
    TextView hintTextView;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionBtn = (Button) findViewById(R.id.session_btn);
        hintTextView = (TextView) findViewById(R.id.hint_text_view);
        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
    }

    public void toggleLoginSignup(View view) {
        usernameEditText.setText("");
        passwordEditText.setText("");

        String tag = sessionBtn.getTag().toString();
        if (tag.equals("login")) {
            hintTextView.setText(getString(R.string.login_hint));
            sessionBtn.setText(getString(R.string.signup_button));
            sessionBtn.setTag("signup");
        } else if (tag.equals("signup")) {
            hintTextView.setText(getString(R.string.signup_hint));
            sessionBtn.setText(getString(R.string.login_button));
            sessionBtn.setTag("login");
        }
    }

    public void handleSession(View view) {
        String tag = sessionBtn.getTag().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Username or password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tag.equals("login")) {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();

                        // startActivity
                        // finish();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (tag.equals("signup")) {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();

                        // startActivity
                        // finish();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
