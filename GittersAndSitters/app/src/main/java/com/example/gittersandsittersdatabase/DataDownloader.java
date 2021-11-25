package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * This Class gets all Habit and HabitEvent data corresponding to the logged in user.
 */
public class DataDownloader implements FirestoreHabitListCallback, FirestoreEventListCallback {

    private FirebaseFirestore db;
    private final String userID;
    private Habit habit;
    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;

    // Constructor for DataDownloader
    DataDownloader(String userID) {
        this.userID = userID;
    }


    /**
     * This method searches the logged in user's Habit Collection. All of the documents within the
     * collection are converted to Habit objects and added to an ArrayList<Habit>
     * @param firestoreHabitListCallback - Interface for handling Firestore's asynchronous behaviour
     *                                     Enables us to get the data from Firestore
     */
    public void getUserHabits(FirestoreHabitListCallback firestoreHabitListCallback) {

        habitList = new ArrayList<>();

        // Get an instance of the Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get a reference to the logged in user's Habit collection
        CollectionReference collectionRef = db.collection("Users").document(userID).collection("Habits");
        // Attempt to get HabitCollection corresponding to userID
        collectionRef.get().addOnCompleteListener(task -> {

            // If Habit documents exist
            if (task.isSuccessful()) {
                // For document in collection
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // get convert document fields to Habit attributes
                    String habitID = document.getId();
                    String habitName = (String) document.getData().get("habitName");
                    String reason = (String) document.getData().get("reason");
                    boolean isPublic = (boolean) document.getData().get("isPublic");

                    ArrayList<Integer> weekdays = new ArrayList<>();
                    // weekdays are stored as type long
                    List<Long> longDays = (List<Long>) document.getData().get("weekdays");
                    // convert long objects to Integers and add them to weekdays
                    for (Long day : longDays) {
                        Integer i = (int) (long) day;
                        weekdays.add(i);
                    }

                    // Convert long object to type Calendar
                    long longDate = (long) document.getData().get("longDate");
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTimeInMillis(longDate);

                    // Create habit from converted document fields and add to habitList
                    habit = new Habit(habitID, habitName, weekdays, startDate, reason, isPublic);
                    habitList.add(habit);
                }
            }
            firestoreHabitListCallback.onHabitListCallback(habitList);
        });
    }

    /**
     * This method searches all of the user's "HabitEvents" collections (one for each Habit).
     * All of the documents within the collections are converted to HabitEvent objects and added to
     * an ArrayList<HabitEvent>
     * @param firestoreHabitCallback - Interface for handling Firestore's asynchronous behaviour
     *                                 Enables us to get the data from Firestore
     */
    public void getHabitEvents (FirestoreEventListCallback firestoreHabitCallback) {

        // Initialize habitEventList
        habitEventList = new ArrayList<>();
        // Get an instance of the Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Attempt to get all of the "HabitEvents" collections
        db.collectionGroup("HabitEvents").get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                // For document in collectionGroup
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // Convert document fields to HabitEvent attributes
                    String eventID = document.getId();
                    String eventName = (String) document.getData().get("eventName");
                    String parentHabitName = (String) document.getData().get("habitName");

                    // Convert long object to type Calendar
                    //long longDate = (long) document.getData().get("longDate");
                    Calendar eventDate = Calendar.getInstance();
                    //eventDate.setTimeInMillis(longDate);

                    String eventComment = (String) document.getData().get("eventComment");

                    // TODO get Location and Photo

                    // Create HabitEvent object and add to habitEventList
                    HabitEvent habitEvent = new HabitEvent
                            (eventID, eventName, parentHabitName, eventDate, eventComment);
                    habitEventList.add(habitEvent);
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }

            // Callback enables us to get all of the downloaded HabitEvents
            firestoreHabitCallback.onEventListCallback(habitEventList);
        });
    }

    @Override
    public void onEventListCallback(ArrayList<HabitEvent> eventList) {
    }
    @Override
    public void onHabitListCallback(ArrayList<Habit> habitList) {
    }
}

