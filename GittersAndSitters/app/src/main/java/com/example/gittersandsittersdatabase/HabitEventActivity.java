package com.example.gittersandsittersdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This Activity represents a ListView of all existing user HabitEvents
 * The HabitEvents are sorted in reverse chronological order
 * This Activity enables the user to view, edit, or delete a HabitEvent
 */
public class HabitEventActivity extends AppCompatActivity {

    // Declared variables for referencing
    ListView habitEventListView;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    User user;
    Habit habit;
    HabitEvent habitEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

        habitEventListView = findViewById(R.id.habit_event_listview);
        // Set adapter to all of the user's habitEvents
        habitEventAdapter = new HabitEventCustomList(this, user.getAllUserHabitEvents());
        habitEventListView.setAdapter(habitEventAdapter);

        // LONG CLICK a HabitEvent to edit it
        habitEventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(HabitEventActivity.this,
                        AddRemoveEventActivity.class);
                intent.putExtra("user", user);
                // i is the index position of the long-clicked HabitEvent in the ListView
                intent.putExtra("position", i);
                startActivity(intent);
                return true; // Why do we return boolean?
            }
        });

    }
}