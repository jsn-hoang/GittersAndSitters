package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FollowFeedCustomList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitList;
    private Context context;
    private FirebaseFirestore db;

    public FollowFeedCustomList(Context context, ArrayList<Habit> habitList){
        super(context,0, habitList);
        this.habitList = habitList;
        this.context = context;


    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit_content, parent, false);
        }
        Habit habit = habitList.get(position);
        TextView habitName = view.findViewById(R.id.habit_name_text);
//      TextView habitReason = view.findViewById(R.id.habit_reason_text);
        ProgressBar habitProgress = view.findViewById(R.id.progressBar);
        //ImageView habitIcon = view.findViewById(R.id.habit_icon);


        habitName.setText(habit.getHabitName());
//      habitReason.setText(habit.getHabitReason());


        //habitEventList = new ArrayList<>();
        // Get an instance of the Firebase Firestore
        //db = FirebaseFirestore.getInstance();

        /**
        db.collectionGroup("HabitEvents").get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                // For document in collectionGroup
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // Reference for document fields:
                    // "eventID" <- the ID of the document
                    // "habitID"
                    // "eventName"
                    // "longDate"
                    // "eventComment"
                    // "eventLocation"
                    // "eventPhoto"

                    // Convert document fields to HabitEvent attributes
                    String eventID = document.getId();
                    String parentHabitID = (String) document.getData().get("habitID");
                    String eventName = (String) document.getData().get("eventName");

                    // Convert long object to type Calendar
                    long longDate = (long) document.getData().get("longDate");
                    Calendar eventDate = Calendar.getInstance();
                    eventDate.setTimeInMillis(longDate);

                    String eventComment = (String) document.getData().get("eventComment");

                    // TODO get Location and Photo

                    // Create HabitEvent object and add to habitEventList
                    HabitEvent habitEvent = new HabitEvent
                            (eventID, parentHabitID, eventName, eventDate, eventComment);
                    habit.addHabitEvent(habitEvent);
                }
                System.out.println("Habit name "+habit.getHabitEvents().get(0).getEventName());
                habit.calculateProgress();
                System.out.println("Habit progress "+habit.getProgress());
                habitProgress.setMax(100);

                habitProgress.setProgress(habit.getProgress());

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }

            // Callback enables us to get all of the downloaded HabitEvents

        });
         **/

        //System.out.println("Habit name "+habit.getHabitEvents().get(0).getEventName());
        habit.calculateProgress();
        System.out.println("Habit progress "+habit.getProgress());
        habitProgress.setMax(100);

        habitProgress.setProgress(habit.getProgress());


        return view;
    }

}

