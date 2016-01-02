package com.aziflaj.exchangeagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final ListView usersListView = (ListView) findViewById(R.id.users_list_view);

        ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
        usersQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        usersQuery.addAscendingOrder("username");
        usersQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersList, ParseException e) {
                if (e == null) {
                    if (usersList.size() > 0) {
                        ArrayList<String> usernames = new ArrayList<>();
                        for (ParseUser user : usersList) {
                            usernames.add(user.getUsername());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                UserListActivity.this,
                                android.R.layout.simple_list_item_1,
                                usernames);
                        usersListView.setAdapter(arrayAdapter);
                    }
                }
            }
        });
    }
}
