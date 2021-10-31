package com.example.gittersandsittersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class represents the activity which displays the users that the user follows
 */
public class FollowingActivity extends AppCompatActivity {
    public static final String EXTRA_INDEX = "com.example.gittersandsittersdatabase.INDEX";

    ArrayList<User> userList;
    ListView followingList;
    ArrayAdapter<User> followingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        // Get list of users that the user follows
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userList = user.getFollowing();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followingList = findViewById(R.id.following_list);
        followingAdapter = new UserCustomList(this, userList);
        followingList.setAdapter(followingAdapter);

        followingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FollowingActivity.this, FollowFeedActivity.class);
                intent.putExtra(EXTRA_INDEX, i);
                startActivity(intent);
            }
        });

    }
}