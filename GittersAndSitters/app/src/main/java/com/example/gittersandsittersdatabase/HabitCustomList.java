package com.example.gittersandsittersdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class provides the logic for displaying Habit objects as ListView entries.
 */

// TODO implement logic so checkboxes appear only on entries due today
public class HabitCustomList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habitList;
    private Context context;
    private boolean isTodayHabitsTabSelected;

    public HabitCustomList(Context context, ArrayList<Habit> habitList, boolean isTodayHabitsTabSelected){
        super(context,0, habitList);
        this.habitList = habitList;
        this.context = context;
        this.isTodayHabitsTabSelected = isTodayHabitsTabSelected;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_content, parent,false);
        }
        Habit habit = habitList.get(position);
        TextView habitName = view.findViewById(R.id.habit_name_text);
//      TextView habitReason = view.findViewById(R.id.habit_reason_text);
        ProgressBar habitProgress = view.findViewById(R.id.progressBar);
        ImageView habitIcon = view.findViewById(R.id.habit_icon);


        // if in "All Habit's" mode
        if (!isTodayHabitsTabSelected)
            habitIcon.setVisibility(View.GONE); // hide icons

        // else display icon corresponding to whether today's Habit isCompleted
        else {
            if (habit.isCompletedToday())
                habitIcon.setImageResource(R.drawable.habit_done_icon);
            else
                habitIcon.setImageResource(R.drawable.habit_due_icon);
        }


        habitName.setText(habit.getHabitName());
//      habitReason.setText(habit.getHabitReason());
        habitProgress.setProgress(habit.getProgress());

        return view;
    }


}