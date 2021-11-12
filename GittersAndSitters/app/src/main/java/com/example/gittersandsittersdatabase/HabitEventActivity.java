package com.example.gittersandsittersdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

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
    ArrayList<HabitEvent> habitEventList;   // ArrayList of all user habitEvents


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");
        // get habitEventList
        habitEventList = user.getAllHabitEvents();

        habitEventListView = findViewById(R.id.habit_event_listview);
        // Set adapter to habitEventList
        habitEventAdapter = new HabitEventCustomList(this, habitEventList);
        habitEventListView.setAdapter(habitEventAdapter);

        // LONG CLICK a HabitEvent to edit it
        habitEventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // i is the position of the ith habitEvent in the habitEventList

                // Get the long-clicked habitEvent
                HabitEvent habitEvent = habitEventList.get(i);
                // get the parent Habit of the long-clicked HabitEvent
                habit = user.getParentHabitOfHabitEvent(habitEvent);
                // set i to the HabitEvent's position in it's parent Habit's habitEventList
                i = habit.getHabitEventPosition(habitEvent);

                Intent intent = new Intent(HabitEventActivity.this,
                        AddRemoveEventActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("habit", habit);
                intent.putExtra("position", i);
                startActivity(intent);
                return true;
            }
        });


        /** NOT SURE WHAT I'M DOING HERE
         *
        // manages the result (updated user object)
        ActivityResultLauncher<Intent> habitActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) { // a habitEvent was added or updated
                        Intent data = result.getData();
                        user = (User) data.getExtras().get("user");
                        // update current tab with data from updated user object
                        //refreshCurrentTab(tabLayout, habitListView, user);
                    }
                });
         */
    }
}