package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class provides the logic for displaying Habit objects as ListView entries.
 */
public class HabitCustomList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habitList;
    private Context context;

    public HabitCustomList(Context context, ArrayList<Habit> habitList){
        super(context,0, habitList);
        this.habitList = habitList;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        Habit habit = habitList.get(position);
        TextView habitName = view.findViewById(R.id.habit_name_text);
        TextView habitReason = view.findViewById(R.id.habit_reason_text);
        ProgressBar habitProgress = view.findViewById(R.id.progressBar);
        //TODO icon <-> Habit.isCompletedToday()

        habitName.setText(habit.getHabitName());
        habitReason.setText(habit.getHabitReason());
        habitProgress.setProgress(habit.getProgress());

        return view;
    }


}