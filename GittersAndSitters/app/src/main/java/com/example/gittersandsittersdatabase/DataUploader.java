package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/** This class enables the addition, deletion, and updating of Habits and HabitEvents in the Firestore
 */

public class DataUploader implements Serializable, FirestoreCallback{

    private final String userID;
    private CollectionReference collectionRef;

    // Constructor for DataUploader
    DataUploader(String userID) {
        this.userID = userID;
    }

    /**
     * This method adds a Habit to its correct location in the Firestore
     * @param habit - The habit we are uploading into the database
     */
    public String addHabitAndGetID(Habit habit) {

        // Set the Collection Reference correctly
        setCollectionReference(true, habit);

        // Reference for document fields:
        // "habitName"
        // "weekdays"
        // "longDate"
        // "reason"
        // "isPublic"
        // "progress"

        HashMap<String, Object> data = new HashMap<>();
        data.put("habitName", habit.getHabitName());
        data.put("weekdays", habit.getWeekdays());
        // Convert startDate to type long for database storage
        long longDate = habit.getStartDate().getTimeInMillis();
        data.put("longDate", longDate);
        data.put("reason", habit.getHabitReason());
        data.put("isPublic", habit.isHabitPublic());

        //TODO .put() progress
        //data.put("progress", habitEvent.getProgress());

        DocumentReference docRef = collectionRef.document();
        String docID = docRef.getId();
        // habit.setHabitID(docID);

        // Add habit to the User's Habit Collection
        docRef.set(data);
        return docID;
    }

    /**
     * This method adds a HabitEvent to its proper location in the Firestore
     * @param habitEvent    - the HabitEvent we are adding to the Firestore
     * @param habit         - The parent Habit of habitEvent
     */
    public String addHabitEventAndGetID(HabitEvent habitEvent, Habit habit) {

        // Set collectionRef
        setCollectionReference(false, habit);

        // Reference for document fields:
        // "habitID"
        // "eventName"
        // "longDate"
        // "eventComment"
        // "eventLocation"
        // "eventPhoto"

        // .put() the habitEvent attributes
        HashMap<String, Object> data = new HashMap<>();
        data.put("habitID", habitEvent.getParentHabitID());
        data.put("eventName", habitEvent.getEventName());

        // Convert startDate to type long for database storage
        long longDate = habitEvent.getEventDate().getTimeInMillis();
        data.put("longDate", longDate);
        data.put("eventComment", habitEvent.getEventComment());

        //TODO upload event and Location (optional attributes)
        //data.put("eventPhoto", habitEvent.getEventPhoto());
        //data.put("eventLocation", habitEvent.getEventLocation());

        DocumentReference docRef = collectionRef.document();
        String docID = docRef.getId();
        // Add habitEvent to the Habit's "HabitEvents" Collection
        docRef.set(data);
        return docID;
    }

    /**
     * This method deletes a Habit from the Firestore. However, we must first delete all of the
     * HabitEvent documents relating to this Habit
     * @param habit - The Habit to be deleted
     */
    public void deleteHabit(Habit habit) {

        // Delete this Habit's "HabitEvents" subcollection
        deleteHabitCollection(habit, () -> {
            // Callback ensures all documents in "HabitEvents" collection are deleted
            //TODO Ensure that this callback doesn't slow down the app.
            // (User must wait for entire "HabitEvents" collection to be deleted from Firestore)

            // Set collectionRef to delete Habit
            setCollectionReference(true, habit);
            // delete the Habit
            collectionRef.document(habit.getHabitID())
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Data has been deleted successfully!"))
                    .addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));

        });
    }

    /**
     * This method deletes a Habit's "HabitEvents" subcollection from Firestore
     * @param habit - The Habit that is to be deleted (along with its "HabitEvents" subcollection)
     * @param firestoreCallback - Interface for handling Firestore's asynchronous behaviour. Ensures
     *                            all "HabitEvents" docs are deleted before the parent Habit doc
     */
    public void deleteHabitCollection(Habit habit, FirestoreCallback firestoreCallback) {

        // set CollectionReference to this Habit's "HabitEvents" collection
        setCollectionReference(false, habit);

        collectionRef.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot snapshot: Objects.requireNonNull(task.getResult())){
                collectionRef.document(snapshot.getId()).delete();
            }
            firestoreCallback.onCallback();
        });
    }

    /**
     * This method deletes a HabitEvent from Firestore
     * @param habit - The parent Habit of the HabitEvent
     * @param habitEvent - the HabitEvent to be deleted
     */
    public void deleteHabitEvent(Habit habit, HabitEvent habitEvent) {

        // set collection reference for HabitEvent deletion
        setCollectionReference(false, habit);

        // Delete the HabitEvent
        collectionRef.document(habitEvent.getEventID())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Data has been deleted successfully!"))
                .addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));
    }

    /** This method updates a Habit in the Firestore
     */
    public void setHabit(Habit habit) {

        // Set to the correct collectionRef
        setCollectionReference(true, habit);
        // Convert date to type long
        long longDate = habit.getStartDate().getTimeInMillis();

        // Reference for document fields:
        // "habitName"
        // "weekdays"
        // "longDate"
        // "reason"
        // "isPublic"
        // "progress"

        DocumentReference docRef = collectionRef.document(habit.getHabitID());
        docRef.update(
                "habitName", habit.getHabitName(),
                "weekdays", habit.getWeekdays(),
                "longDate", longDate,
                "reason", habit.getHabitReason(),
                "isPublic", habit.isHabitPublic())
                // "progress", habit.getProgress())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    /** This method updates a HabitEvent in the Firestore
     */
    public void setHabitEvent(Habit habit, HabitEvent habitEvent) {

        // Reference for document fields:
        // "eventName"
        // "longDate"
        // "eventComment"
        // "eventLocation"
        // "eventPhoto"

        // Set to the correct CollectionRef
        setCollectionReference(false, habit);
        long longDate = habitEvent.getEventDate().getTimeInMillis();
        DocumentReference docRef = collectionRef.document(habitEvent.getEventID());
        docRef.update(
                "eventName", habitEvent.getEventName(),
                "longDate", longDate,
                "eventComment", habitEvent.getEventComment())
                // "eventLocation", habitEvent.getLocation(),
                // "eventPhoto", habitEvent.getPhoto())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    /**
     * This method obtains the proper Firestore CollectionReference
     * @param isForHabit - Boolean indicating whether we need Habit or HabitEvent CollectionRef
     */
    public void setCollectionReference(boolean isForHabit, Habit habit) {

        // Get an instance of the Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (isForHabit)
            collectionRef = db.collection("Users/" + userID + "/Habits/");
        else // else get collectionRef for HabitEvents
            collectionRef = db.collection("Users/" + userID + "/Habits/" + habit.getHabitID() + "/HabitEvents/");
    }

    @Override
    public void onCallback() {
    }
}
