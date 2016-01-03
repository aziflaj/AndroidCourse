package com.aziflaj.exchangeagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {
    LinearLayout feedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        feedLayout = (LinearLayout) findViewById(R.id.feed_layout);

        String username = getIntent().getStringExtra("username");
        setTitle(username + " Feed");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.whereEqualTo("username", username);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject obj : objects) {
                            ParseFile imageFile = (ParseFile) obj.get("image");

                            imageFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    Bitmap imageBmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView iv = new ImageView(UserFeedActivity.this);
                                    iv.setImageBitmap(imageBmp);
                                    iv.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));

                                    feedLayout.addView(iv);
                                }
                            });

                        }
                    }
                } else {
                    Log.d("TAG", "done: error");
                }
            }
        });
    }
}
