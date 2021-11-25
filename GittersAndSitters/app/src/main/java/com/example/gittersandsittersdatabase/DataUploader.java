package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/** This class enables the addition, deletion of Habits and HabitEvents in the Firestore
 */

public class DataUploader implements Serializable, FirestoreHabitCallback, FirestoreEventCallback {

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

        HashMap<String, Object> data = new HashMap<>();
        data.put("habitName", habit.getHabitName());
        data.put("weekdays", habit.getWeekdays());
        // Convert startDate to type long for database storage
        long longDate = habit.getStartDate().getTimeInMillis();
        data.put("longDate", longDate);
        data.put("reason", habit.getHabitReason());
        data.put("isPublic", habit.isHabitPublic());

        DocumentReference docRef = collectionRef.document();
        String docID = docRef.getId();
        // habit.setHabitID(docID);

        // Add habit to the User's Habit Collection
        docRef.set(data);
        return docID;
    }
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

    /**
     * This method adds a HabitEvent to its proper location in the Firestore
     * @param habitEvent    - the HabitEvent we are adding to the Firestore
     * @param habit         - The parent Habit of habitEvent
     */
    public String addHabitEventAndGetID(HabitEvent habitEvent, Habit habit) {

        // Set collectionRef
        setCollectionReference(false, habit);

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

//        collectionRef.add(data)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//                        // setID attribute for habitEvent
//                        habitEvent.setEventID(documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

    /**
     * This method deletes a Habit from the Firestore. However, we must first delete all of the
     * HabitEvent documents relating to this Habit
     * @param habit - The Habit to be deleted
     */
    public void deleteHabit(Habit habit) {


        // Set collectionRef
        setCollectionReference(true, habit);
        // delete the Habit
        collectionRef.document(habit.getHabitID())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Data has been deleted successfully!"))
                .addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));


//        deleteHabitEventSubCollection(new FirestoreHabitCallback() {
//            @Override
//            // Wait for sub-collection to be deleted before deleting the Habit document
//            public void onHabitCallback(Habit habit) {
//
//                // Set collectionRef
//                setCollectionReference(true, habit);
//                // delete the Habit
//                collectionRef.document(habit.getHabitID())
//                        .delete()
//                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Data has been deleted successfully!"))
//                        .addOnFailureListener(e -> Log.d(TAG, "Data could not be deleted!" + e.toString()));
//
//            }
//        }, habit);
//
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
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be deleted!" + e.toString());
                    }
                });
    }

    /** This method deletes all documents within a specified Habit's "HabitEvents" sub-collection
     */
//    public void deleteHabitEventSubCollection(FirestoreHabitCallback firestoreHabitCallback, Habit habit) {
//
//        setCollectionReference(false, habit);
//
//        // Iterate through the "HabitEvents" collection for this Habit, deleting all documents
//
//
//        // return to caller to delete the parent document of this sub-collection
//        firestoreHabitCallback.onHabitCallback(habit);
//        }

    /** This method updates a Habit in the Firestore
     */
    public void setHabit(Habit habit) {

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

    /** This method updates a HabitEvent in the Firestore
     */
    public void setHabitEvent(HabitEvent habitEvent) {

        long longDate = habitEvent.getEventDate().getTimeInMillis();
        DocumentReference docRef = collectionRef.document(habitEvent.getEventID());
        docRef.update(
                "eventName", habitEvent.getEventName(),
                "longDate", longDate,
                "eventComment", habitEvent.getEventComment())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
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







    // https://stackoverflow.com/questions/49125183/how-delete-a-collection-or-subcollection-from-firestore
    public void deleteCollection(final CollectionReference collection, Executor executor) {
        Tasks.call(executor, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int batchSize = 10;
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);
                List<DocumentSnapshot> deleted = DataUploader.this.deleteQueryBatch(query); // call deleteQueryBatch

                while (deleted.size() >= batchSize) {
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId()).startAfter(last.getId()).limit(batchSize);

                    deleted = DataUploader.this.deleteQueryBatch(query);
                }

                return null;
            }
        });
    }

    // Called by deleteCollection
    @WorkerThread
    public List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (DocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }





    @Override
    public void onHabitCallback(Habit habit) {
    }
    @Override
    public void onHabitEventCallback(HabitEvent habitEvent) {

    }
}
