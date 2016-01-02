package com.aziflaj.exchangeagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    public static final int IMAGE_RESULT = 1;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

                ParseObject image = new ParseObject("Image");
                image.put("username", ParseUser.getCurrentUser().getUsername());
                image.put("image", new ParseFile("image.png", stream.toByteArray()));
                Toast.makeText(this, "Uploading photo", Toast.LENGTH_SHORT).show();
                image.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(UserListActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserListActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (IOException e) {
                Toast.makeText(this, "Can't load photo", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent imageChooser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageChooser, IMAGE_RESULT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
