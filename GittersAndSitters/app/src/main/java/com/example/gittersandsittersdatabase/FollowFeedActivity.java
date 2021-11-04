package com.example.gittersandsittersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class represents the activity which displays the habits of a user that the user follows
 */
public class FollowFeedActivity extends AppCompatActivity {
    private TextView feedBanner;
    private ListView feedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_feed);
        // TO DO

        feedBanner = findViewById(R.id.feed_banner);
        //feedBanner.setText("");
    }
}