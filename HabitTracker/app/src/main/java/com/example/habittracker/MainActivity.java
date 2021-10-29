package com.example.habittracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the MainActivity for the HabitTracker app.
 * (It is set to display today's habits by default.)
 */
public class MainActivity extends AppCompatActivity {

    // Declare variables for referencing
    ListView habitListView;
    ArrayAdapter<com.example.habittracker.Habit> todayHabitsAdapter;
    ArrayAdapter<com.example.habittracker.Habit> allHabitsAdapter;
    User user;
    boolean allHabits = false;      // tracks if ListView is displaying todayHabits or allHabits

    @RequiresApi(api = Build.VERSION_CODES.O)           // api required to implement DayOfWeek
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitListView = findViewById(R.id.habit_list);

        // This conditional checks whether or not there is an existing user.
        if (getIntent().hasExtra("user"))
            user = (User) getIntent().getSerializableExtra("user");
        else user = new User("Timmy");

        // This conditional enables us to use allHabits
        // to set habitListView to the last chosen adapter
        if (getIntent().hasExtra("allHabits"))
            // Display
            allHabits = getIntent().getExtras().getBoolean("allHabits");



        // Having two adapters enables us to set habitListView to "Today's Habits" or "All Habits"
        todayHabitsAdapter = new HabitCustomList(this, user.getTodayUserHabits());
        allHabitsAdapter = new HabitCustomList(this, user.getAllUserHabits());
        // This conditional sets habitListView to the correct adapter
        if (allHabits)
            habitListView.setAdapter(allHabitsAdapter);
        else
            habitListView.setAdapter(todayHabitsAdapter);

        // This method is responsible for the logic when clicking an existing habit
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("position", position);
                intent.putExtra("allHabits", allHabits);
                startActivity(intent);

            }
        });

        // This method is responsible for the logic when clicking the "Add Habit" button
        final Button addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("allHabits", allHabits);
                startActivity(intent);
            }
        });

        // This method is responsible for the logic when clicking the "Today's Habits" button
        final Button todayHabitsButton = findViewById(R.id.today_habits_button);
        todayHabitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (allHabits) {
                    allHabits = false;
                    habitListView.setAdapter(todayHabitsAdapter);
                }
            }
        });

        // This method is responsible for the logic when clicking the "All Habits" button
        final Button allHabitsButton = findViewById(R.id.all_habits_button);
        allHabitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!allHabits) {
                    allHabits = true;
                    habitListView.setAdapter(allHabitsAdapter);
                }
            }
        });
    }
}