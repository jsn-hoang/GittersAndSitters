package com.example.gittersandsittersdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the activity which displays the users that the user follows
 * Clicking on a follower will show a list of the followers habits and their progress for each habit
 */
public class FollowingActivity extends AppCompatActivity {


    private ListView follow_list;
    private ArrayAdapter<String> followAdapter;
    private List<String> followList;
    private ArrayList<String> followArrayList;
    private String targetUserId;
    private String targetEmail;
    private String targetUserName;
    private User userClickedOn;
    private FirebaseAuth mAuth;
    private String current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        setTitle("Following");


        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference docRef = collectionReference.document(current_user);


        followArrayList = new ArrayList<>();
        follow_list = findViewById(R.id.following_list);
        followAdapter = new UserCustomList(FollowingActivity.this, followArrayList);

        follow_list.setAdapter(followAdapter);

        // setting snap shot listener so the follower list updates after a follow request is accepted
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }


                if (snapshot != null && snapshot.exists()) {
                    // clearing followArrayList to avoid duplicate items
                    followArrayList.clear();
                    // Storing the list of followers in followList
                    followList = (List<String>) snapshot.getData().get("following");
                    // looping through each item in followList and adding it to followArrayList
                    for (String follow : followList) {
                        followArrayList.add(follow);
                    }

                    // notifying adapter that there was changes to followArrayList
                    followAdapter.notifyDataSetChanged();


                    Log.d("TAG", "Current data: " + snapshot.getData());
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });


        // When a follower is clicked on we want a list of their habits to be displayed
        follow_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // targertUserName is the username of the follower an app user clicks on
                targetUserName = followArrayList.get(i);
                final CollectionReference collectionReference = db.collection("Users");
                collectionReference
                        .whereEqualTo("userName", targetUserName) // search for user with a username equal to targetUserName
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            // getting followers Id, Email and creating a new user object
                                            targetUserId = document.getId();
                                            targetEmail = (String) document.getData().get("email");
                                            userClickedOn = new User(targetUserId,targetUserName, targetEmail);
                                            // passing the user object to the FollowFeedActivity class
                                            Intent intent = new Intent(FollowingActivity.this, FollowFeedActivity.class);
                                            intent.putExtra("user", userClickedOn);
                                            startActivity(intent);




                                            Log.d("TAG", document.getId() + " => " + document.getData());
                                        }
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });
    }
}


