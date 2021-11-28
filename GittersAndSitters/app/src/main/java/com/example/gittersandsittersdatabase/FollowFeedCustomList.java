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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FollowFeedCustomList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitList;
    private Context context;
    private FirebaseFirestore db;
    private String userid;

    public FollowFeedCustomList(Context context, ArrayList<Habit> habitList,String userId){
        super(context,0, habitList);
        this.habitList = habitList;
        this.context = context;
        this.userid = userId;


    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit_content, parent, false);
        }
        Habit habit = habitList.get(position);
        TextView habitName = view.findViewById(R.id.habit_name_text);
        ProgressBar habitProgress = view.findViewById(R.id.progressBar);



        habitName.setText(habit.getHabitName());






        habitProgress.setMax(100);
        habitProgress.setProgress(habit.getProgress());




        return view;
    }

}
