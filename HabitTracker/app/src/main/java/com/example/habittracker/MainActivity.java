package com.example.habittracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView habitListView;
    ArrayAdapter<com.example.habittracker.Habit> habitAdapter;
    ArrayList<com.example.habittracker.Habit> habitList;
    ArrayList<DayOfWeek> weekdays;
    User user;

    @RequiresApi(api = Build.VERSION_CODES.O)           // api required to implement DayOfWeek
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        habitListView = findViewById(R.id.habit_listview);

        /**
         * Test creating a new Habit
         *
         *         habitName;
         *         weekdays;
         *         habitReason;
         */

        String habitName = "Running";
        weekdays = new ArrayList<>();
        weekdays.add(DayOfWeek.MONDAY);
        weekdays.add(DayOfWeek.WEDNESDAY);
        weekdays.add(DayOfWeek.THURSDAY);
        String habitReason = "Get in shape";

        Habit habit = new Habit(habitName, weekdays, habitReason, true);

        /**
         *  Test creating a user and giving them a habit
         */
        user = new User("Timmy");
        user.addUserHabit(habit);


        habitList = new ArrayList<>();
        habitList.add(habit);

        // Set adapter to todayUserHabits
        habitAdapter = new HabitCustomList(this, user.getTodayUserHabits());
        habitListView.setAdapter(habitAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // todays habits
                        habitAdapter = new HabitCustomList(MainActivity.this, user.getTodayUserHabits());
                        habitListView.setAdapter(habitAdapter);
                        break;
                    case 1: // all habits
                        habitAdapter = new HabitCustomList(MainActivity.this, user.getAllUserHabits());
                        habitListView.setAdapter(habitAdapter);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final FloatingActionButton floatingActionButton = findViewById(R.id.add_habit_FAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("mode", "ADD");
                startActivity(intent);
            }
        });

        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("mode", "EDIT");
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

    }
}

    /**
     // Retains ListView when returning to MainActivity
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
     if(item.getItemId() == android.R.id.home) {
     onBackPressed();
     return true;
     }
     return super.onOptionsItemSelected(item);
     }
     */