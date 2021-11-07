package com.example.gittersandsittersdatabase;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This Activity represents a ListView of all existing user HabitEvents
 * The HabitEvents are sorted in reverse chronological order
 * This Activity enables the user to view, edit, or delete a HabitEvent
 */
public class HabitEventHistoryActivity extends AppCompatActivity {

    // Declared variables for referencing
    ListView habitEventListView;
    ArrayAdapter<Habit> habitEventAdapter;
    User user;
    Habit habit;
    HabitEvent habitEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_habit_event);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

        habitEventListView = findViewById(R.id.habit_event_listview);
        // Set adapter to todayUserHabits
        habitEventAdapter = new HabitEventCustomList(this, user.getAllUserHabitEvents());
        habitEventListView.setAdapter(habitEventAdapter);




    }
}