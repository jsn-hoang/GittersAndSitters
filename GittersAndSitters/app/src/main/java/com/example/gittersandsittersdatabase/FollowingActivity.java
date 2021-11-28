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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the activity which displays the users that the user follows
 */
public class FollowingActivity extends AppCompatActivity {
    //public static final String EXTRA_INDEX = "com.example.gittersandsittersdatabase.INDEX";


    TextView followingBanner;

    private ListView follow_list;
    private ArrayAdapter<String> followAdapter;
    private List<String> followList;
    private ArrayList<String> followArrayList;
    private String targetUserId;
    private String current_username;
    private String targetEmail;
    private String targetUserName;
    private User userClickedOn;


    private FirebaseAuth mAuth;
    private String current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);


        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference docRef = collectionReference.document(current_user);


        followArrayList = new ArrayList<>();
        follow_list = findViewById(R.id.following_list);
        followAdapter = new UserCustomList(FollowingActivity.this, followArrayList);

        follow_list.setAdapter(followAdapter);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    followArrayList.clear();
                    followList = (List<String>) snapshot.getData().get("following");
                    for (String follow : followList) {
                        followArrayList.add(follow);
                    }


                    followAdapter.notifyDataSetChanged();


                    Log.d("TAG", "Current data: " + snapshot.getData());
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });


        followingBanner = findViewById(R.id.following_banner);
        followingBanner.setText("Following");


        follow_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                targetUserName = followArrayList.get(i);
                final CollectionReference collectionReference = db.collection("Users");
                collectionReference
                        .whereEqualTo("userName", targetUserName) // <-- This line
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            targetUserId = document.getId();
                                            targetEmail = (String) document.getData().get("email");
                                            userClickedOn = new User(targetUserId,targetUserName, targetEmail);
                                            //System.out.println("User clicked on " + userClickedOn.getUsername());
                                            Intent intent = new Intent(FollowingActivity.this, FollowFeedActivity.class);
                                            intent.putExtra("user", userClickedOn);
                                            startActivity(intent);

                                            //DocumentReference targetUserReference = collectionReference.document(targetUserId);
                                            //targetUserReference.update("requests", FieldValue.arrayUnion(current_username));


                                            Log.d("TAG", document.getId() + " => " + document.getData());
                                        }
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //System.out.println("User clicked on " + userClickedOn.getUsername());
                //Intent intent = new Intent(FollowingActivity.this, FollowFeedActivity.class);
                //intent.putExtra("user", userClickedOn);
                //startActivity(intent);
            }
        });
    }
}


