package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/** This class enables the addition, deletion of Habits and HabitEvents in the Firestore
 */

public class DataUploader implements Serializable, FirestoreHabitCallback { // FirestoreHabitCallback

    private FirebaseFirestore db;
    private String userID;
    private Habit habit;
    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;
    private CollectionReference collectionRef;
    private String habitID;

    // Constructor for DataUploader
    DataUploader(String userID) {
        this.userID = userID;
    }


    /**
     * This method adds a newly created habit to the logged in user's Habit collection in the database
     */
    public void addHabitAndGetID(FirestoreHabitCallback firestoreHabitCallback, Habit habit) {

        // Set the Collection Reference correctly
        setCollectionReference(true, habit);

        HashMap<String, Object> data = new HashMap<>();
        // Habit(String habitName, ArrayList<Integer> weekdays, Calendar startDate, String habitReason, boolean habitPublic)
        data.put("habitName", habit.getHabitName());
        data.put("weekdays", habit.getWeekdays());
        // Convert startDate to type long for database storage
        long longDate = habit.getStartDate().getTimeInMillis();
        data.put("longDate", longDate);
        data.put("reason", habit.getHabitReason());
        data.put("isPublic", habit.isHabitPublic());

        // Add habit to the User's Habit Collection
        collectionRef.add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    habit.setHabitID(documentReference.getId());

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        // Perform Callback to get habit with habitID
        firestoreHabitCallback.onHabitCallback(habit);
    }

    /**
     * This method deletes a specified Habit from the user's Habit Collection
     * It also calls another method to delete all of HabitEvent documents
     * within specified Habit's sub-collection
     */
    public void deleteHabit(Habit habit) {

        //TODO
        // Delete all documents within this Habit's "HabitEvents" sub-collection
//        deleteHabitEventSubCollection(new FirestoreHabitCallback() {
//            @Override
//            public void onHabitCallback(Habit habit) {
//                // Wait for sub-collection to be deleted before deleting the Habit document
//                getReference(true, habit);
//            }
//        }, habit);

        // Set the Collection Reference correctly
        setCollectionReference(true, habit);

        collectionRef.document(habit.getHabitID())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Data has been deleted successfully!"))
                .addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));
    }



    /** This method deletes all documents within a specified Habit's "HabitEvents" sub-collection
     */
    public void deleteHabitEventSubCollection(FirestoreHabitCallback firestoreHabitCallback, Habit habit) {

        setCollectionReference(false, habit);

        // Iterate through the "HabitEvents" collection for this Habit, deleting all documents

        // return to caller to delete the parent document of this sub-collection
        firestoreHabitCallback.onHabitCallback(habit);
        }

    /**
     * This method replaces a Habit in the logged in user's Firestore "Habit" collection
     */
    public void setHabitInDB(Habit habit) {

        setCollectionReference(true, habit);
        // Convert date to type long
        long longDate = habit.getStartDate().getTimeInMillis();

        DocumentReference docRef = collectionRef.document(habit.getHabitID());
        docRef.update(
                "habitName", habit.getHabitName(),
                "weekdays", habit.getWeekdays(),
                "longDate", longDate,
                "reason", habit.getHabitReason(),
                "isPublic", habit.isHabitPublic())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }
    
    /**
     * This method obtains the proper Firestore CollectionReference
     * @param isForHabit - Boolean indicating whether we need Habit or HabitEvent CollectionRef
     */
    public void setCollectionReference(boolean isForHabit, Habit habit) {

        // Get an instance of the Firebase Firestore
        db = FirebaseFirestore.getInstance();
        if (isForHabit)
            collectionRef = db.collection("Users/" + userID + "/Habits/");
        else // else get collectionRef for HabitEvents
            collectionRef = db.collection("Users/" + userID + "/Habits/" + habit.getHabitID() + "/HabitEvents/");
    }

    @Override
    public void onHabitCallback(Habit habit) {

    }
}
