package com.aziflaj.exchangeagram;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
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
    public static final int IMAGE_FROM_GALLERY_RESULT = 1;
    public static final int IMAGE_FROM_CAMERA_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        final ArrayList<String> usernames = new ArrayList<>();
        final ListView usersListView = (ListView) findViewById(R.id.users_list_view);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserListActivity.this, UserFeedActivity.class);
                intent.putExtra("username", usernames.get(position));
                startActivity(intent);
            }
        });

        ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
        usersQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        usersQuery.addAscendingOrder("username");
        usersQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersList, ParseException e) {
                if (e == null) {
                    if (usersList.size() > 0) {
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

        if (requestCode == IMAGE_FROM_GALLERY_RESULT && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

                ParseFile parsePhoto = new ParseFile("image.png", stream.toByteArray());

                ParseObject image = new ParseObject("Image");
                image.put("username", ParseUser.getCurrentUser().getUsername());
                image.put("image", parsePhoto);

                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(false);
                image.setACL(acl);

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
        } else if (requestCode == IMAGE_FROM_CAMERA_RESULT && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (photo != null) {
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                ParseFile parsePhoto = new ParseFile("image.png", stream.toByteArray());

                ParseObject image = new ParseObject("Image");
                image.put("username", ParseUser.getCurrentUser().getUsername());
                image.put("image", parsePhoto);

                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(false);
                image.setACL(acl);

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
            case R.id.logout:
                ParseUser.logOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void handlePhotoUpload(View view) {
        switch (view.getId()) {
            case R.id.fab_camera:

                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Toast.makeText(UserListActivity.this, "You can't use the camera", Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent fromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(fromCamera, IMAGE_FROM_CAMERA_RESULT);
                break;

            case R.id.fab_gallery:
                Intent imageChooser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageChooser, IMAGE_FROM_GALLERY_RESULT);
                break;
        }
    }
}
