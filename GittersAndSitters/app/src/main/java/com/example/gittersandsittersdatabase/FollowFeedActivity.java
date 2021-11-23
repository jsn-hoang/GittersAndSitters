package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class represents the activity which displays the habits of a user that the user follows
 */
public class FollowFeedActivity extends AppCompatActivity {
    private TextView feedBanner;
    private ListView feedList;
    private FirebaseFirestore fStore;
    private String userID;
    private FirebaseAuth mAuth;
    private User targetUserName;
    private String targetUserId;
    private ListView followHabit_list;
    private ArrayAdapter<Habit> followHabitAdapter;
    private List<String> followList;
    private ArrayList<Habit> followHabitArrayList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_feed);
        // TO DO

        feedBanner = findViewById(R.id.feed_banner);
        //feedBanner.setText("");

        fStore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        user = (User) getIntent().getSerializableExtra("user");
        targetUserName = (User) getIntent().getSerializableExtra("userClickedOn");

        targetUserId = targetUserName.getUserID();


        //followHabitArrayList = new ArrayList<>();
        //followHabit_list = findViewById(R.id.feed_list);

        //followHabitAdapter = new FollowFeedCustomList(FollowFeedActivity.this,followHabitArrayList);
        //followHabit_list.setAdapter(followHabitAdapter);


        final CollectionReference collectionRef = fStore.collection("Users");
        DocumentReference targetUserReference = collectionRef.document(targetUserId);
        CollectionReference habitCollectionReference = fStore.collection("Users").document(targetUserId).collection("Habits");
        followHabit_list = findViewById(R.id.feed_list);

        followHabitArrayList = new ArrayList<>();
        followHabitAdapter = new FollowFeedCustomList(FollowFeedActivity.this,followHabitArrayList);
        followHabit_list.setAdapter(followHabitAdapter);
        habitCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                followHabitArrayList.clear();

                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, doc.getId() + " => " + doc.getData());
                    // Set habitName as document ID
                    String habitName = doc.getId();

                    // Get remaining Habit attributes from document
                    String reason = (String) doc.getData().get("reason");
                    boolean isPublic = (boolean) doc.getData().get("isPublic");


                    // weekdays are stored as type long
                    List<Long> longDays = (List<Long>) doc.getData().get("weekdays");
                    // Initialize weekdays arraylist
                    ArrayList<Integer> weekdays = new ArrayList<>();
                    // convert long objects to Integers and add them to weekdays
                    for (Long day : longDays) {
                        Integer i = (int) (long) day;
                        weekdays.add(i);
                    }

                    // Convert long object to type Calendar
                    long longDate = (long) doc.getData().get("longDate");
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTimeInMillis(longDate);
                    //Calendar startDate = Calendar.getInstance();

                    Habit habit = new Habit(habitName, weekdays, startDate, reason, isPublic);

                    //followHabitArrayList.add(habit);
                    //Add Habit to logged in user
                    if (isPublic) {
                        targetUserName.addUserHabit(habit);
                        followHabitArrayList.add(habit);
                    }
                    //followHabitArrayList = targetUserName.getAllUserHabits();
                }


                //followHabitArrayList = new ArrayList<>();
                //followHabit_list = findViewById(R.id.feed_list);

                //followHabitAdapter = new FollowFeedCustomList(FollowFeedActivity.this,targetUserName.getAllUserHabits());
                //followHabit_list.setAdapter(followHabitAdapter);
                followHabitAdapter.notifyDataSetChanged();


            }
        });

        // Button goes to Habit Activity
        final Button habitActivityButton = findViewById(R.id.follow_feed_to_habits_button);
        habitActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FollowFeedActivity.this, HabitActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        /**

        collectionRef
                .whereEqualTo("userName", targetUserName) // <-- This line
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String targetUserId;
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    targetUserId = document.getId();
                                    DocumentReference targetUserReference = collectionRef.document(targetUserId);
                                    CollectionReference habitCollectionReference = fStore.collection("Users").document(targetUserId).collection("Habits");

                                    habitCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            for (QueryDocumentSnapshot doc : value) {
                                                Log.d(TAG, doc.getId() + " => " + doc.getData());
                                                // Set habitName as document ID
                                                String habitName = doc.getId();

                                                // Get remaining Habit attributes from document
                                                String reason = (String) doc.getData().get("reason");
                                                boolean isPublic = (boolean) doc.getData().get("isPublic");


                                                // weekdays are stored as type long
                                                List<Long> longDays = (List<Long>) document.getData().get("weekdays");
                                                // Initialize weekdays arraylist
                                                ArrayList<Integer> weekdays = new ArrayList<>();
                                                // convert long objects to Integers and add them to weekdays
                                                for (Long day : longDays) {
                                                    Integer i = (int) (long) day;
                                                    weekdays.add(i);
                                               }

                                                // Convert long object to type Calendar
                                                long longDate = (long) document.getData().get("longDate");
                                                Calendar startDate = Calendar.getInstance();
                                                startDate.setTimeInMillis(longDate);
                                                //Calendar startDate = Calendar.getInstance();

                                                Habit habit = new Habit(habitName, weekdays, startDate, reason, isPublic);

                                                followHabitArrayList.add(habit);
                                                 //Add Habit to logged in user
                                                //targetUserName.addUserHabit(habit);
                                            }
                                            followHabitAdapter.notifyDataSetChanged();


                                        }
                                    });


                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
         **/
    }

    }
