package com.example.gittersandsittersdatabase;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the activity which displays the users that the user follows
 */
public class FollowingActivity extends AppCompatActivity {
    //public static final String EXTRA_INDEX = "com.example.gittersandsittersdatabase.INDEX";

    ArrayList<User> userList;
    ListView followingList;
    TextView followingBanner;
    ArrayAdapter<User> followingAdapter;

    private ListView request_list;
    private ArrayAdapter<String> requestAdapter;
    private List<String> requestList;
    private ArrayList<String> requestArrayList;

    private ListView follow_list;
    private ArrayAdapter<String> followAdapter;
    private List<String> followList;
    private ArrayList<String> followArrayList;


    private FirebaseAuth mAuth;
    private String current_user;
    private String current_username;
    private String targetUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        // Get list of users that the user follows
        //DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
        //        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
       // userRef.addListenerForSingleValueEvent(new ValueEventListener() {
       //     @Override
       //     public void onDataChange(DataSnapshot dataSnapshot) {
       //         User user = dataSnapshot.getValue(User.class);
      //          //userList = user.getFollowing();
      //      }

     //       @Override
      //      public void onCancelled(DatabaseError databaseError) {

     //       }
     //   });

      //  FirebaseFirestore db = FirebaseFirestore.getInstance();
      //  final CollectionReference collectionReference = db.collection("Users");
      //  DocumentReference docRef = collectionReference.document(current_user);

      //  requestArrayList = new ArrayList<>();
      //  request_list = findViewById(R.id.request_list);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference docRef = collectionReference.document(current_user);






        followArrayList = new ArrayList<>();
        follow_list = findViewById(R.id.following_list);


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        followList = (List<String>) document.getData().get("following");

                        for (String follow : followList){
                            followArrayList.add(follow);
                        }

                        followAdapter = new UserCustomList(FollowingActivity.this, followArrayList);
                        follow_list.setAdapter(followAdapter);

                        //System.out.println(requestArrayList.get(0));
                        //System.out.println(current_username);
                        //System.out.println(requestList.get(0));

                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


        //followingBanner = findViewById(R.id.following_banner);
        //followingBanner.setText("Following");

        //followingList = findViewById(R.id.following_list);
       // followingAdapter = new UserCustomList(this, userList);
        //followingList.setAdapter(followingAdapter);

        //followingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //        Intent intent = new Intent(FollowingActivity.this, FollowFeedActivity.class);
        //        intent.putExtra(EXTRA_INDEX, i);
        //        startActivity(intent);
        //    }
        //});

        //follow_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //        Intent intent = new Intent(FollowingActivity.this, FollowFeedActivity.class);
        //        intent.putExtra(EXTRA_INDEX, i);
             //   startActivity(intent);
      //      }
      //  });

    }
}