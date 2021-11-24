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
 * This Class loads all of the Habit and HabitEvent data corresponding the the logged in user.
 */
public class DataDownloader implements FirestoreHabitCallback, FirestoreHabitListCallback{

    private CollectionReference collectionRef;
    private FirebaseFirestore db;
    private final String userID;
    private Habit habit;
    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;
    private int numHabits;
    private int x;

    // Constructor for DataDownloader
    DataDownloader(String userID) {
        this.userID = userID;
    }

    /**
     * This method searches the logged in user's Habit Collection
     * All of the documents within the collection are converted
     * to Habit objects and added to an ArrayList<Habit>
     * The HabitEvents corresponding to each Habit are also loaded
     * @param firestoreHabitListCallback - Interface for handling Firestore's asynchronous behaviour
     *                                     Enables us to get the data from Firestore
     */
    public void getUserHabits(FirestoreHabitListCallback firestoreHabitListCallback) {

        // Initialize a new array
        habitList = new ArrayList<>();
        numHabits = 0;


        // Get an instance of the Firebase Firestore
        db = FirebaseFirestore.getInstance();
        // Get a reference to the logged in user's Habit collection
        collectionRef = db.collection("Users").document(userID).collection("Habits");
        numHabits = 0;
        x = 0;
        // Attempt to get HabitCollection corresponding to userID
        collectionRef.get().addOnCompleteListener(task -> {

            // If Habit documents exist
            if (task.isSuccessful()) {
                // For document in collection
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    numHabits += 1;
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // get convert document fields to Habit attributes
                    String habitID = document.getId();
                    String habitName = (String) document.getData().get("habitName");
                    String reason = (String) document.getData().get("reason");
                    boolean isPublic = (boolean) document.getData().get("isPublic");

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

                    // Create habit from converted document fields
                    habit = new Habit(habitID, habitName, weekdays, startDate, reason, isPublic);
                    //habitList.add(habit);

                    // get HabitEvents pertaining to this Habit
                    getHabitEvents(new FirestoreHabitCallback() {
                        @Override
                        // Call back enables us to get the downloaded Habit with its HabitEvents
                        public void onCallback(Habit habitWithEvents) {
                            x+=1;

                            habitList.add(habitWithEvents);

                            if(x == numHabits)
                            // executed after all habitWithEvents are added

                                firestoreHabitListCallback.onCallback(habitList);
                           // if (habitList.size() == numHabits) {
                                // Called after all habits are added to HabitList
                                //firestoreHabitListCallback.onCallback(habitList);
                           // }
                        }
                    },habit);
                    //firestoreHabitListCallback.onCallback(habitList);
                }
//                firestoreHabitListCallback.onCallback(habitList);
            }
        });
    }

    /**
     * This method searches the logged in user's HabitEvent collection
     * corresponding to a specified Habit.
     * All of the documents within the collection are converted
     * to HabitEvent objects and added to the parent Habit's HabitList
     * @param firestoreHabitCallback - Interface for handling Firestore's asynchronous behaviour
     *                                 Enables us to get the data from Firestore
     */
    public void getHabitEvents (FirestoreHabitCallback firestoreHabitCallback, Habit passedHabit) {

        habit = passedHabit;
        habitEventList = new ArrayList<>();
        // Get an instance of the Firebase Firestore
        db = FirebaseFirestore.getInstance();
        // Get the collection reference for this Habit's HabitEvents
        collectionRef =
                collectionRef.document(habit.getHabitID()).collection("HabitEvents");

        // Attempt to get HabitEvent collection for this Habit
        collectionRef.get().addOnCompleteListener(task -> {

            // If collection is found
            if (task.isSuccessful()) {
                // For document in collection
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // Convert document fields to HabitEvent attributes
                    String eventID = document.getId();
                    String eventName = (String) document.getData().get("eventName");
                    String parentHabitName = habit.getHabitName();

                    // Convert long object to type Calendar
                    //long longDate = (long) document.getData().get("longDate");
                    Calendar eventDate = Calendar.getInstance();
                    //eventDate.setTimeInMillis(longDate);

                    String eventComment = (String) document.getData().get("eventComment");

                    // TODO get Location and Photo

                    // Create HabitEvent object
                    HabitEvent habitEvent = new HabitEvent
                            (eventID, eventName, parentHabitName, eventDate, eventComment);
                    // Add HabitEvent to the corresponding Habit
                    habit.addHabitEvent(habitEvent);
                }
                firestoreHabitCallback.onCallback(habit);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    @Override
    public void onCallback(ArrayList<Habit> habitList) {
    }
    @Override
    public void onCallback(Habit habit) {
    }

}
