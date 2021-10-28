package com.example.habittracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_main);

        habitListView = findViewById(R.id.today_habits_list);

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

        final Button allHabitsButton = findViewById(R.id.all_habits_button);
        allHabitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                habitAdapter = new HabitCustomList(MainActivity.this, user.getAllUserHabits());
                habitListView.setAdapter(habitAdapter);
            }
        });

        final Button todaysHabitsButton = findViewById(R.id.todays_habits_button);
        todaysHabitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                habitAdapter = new HabitCustomList(MainActivity.this, user.getTodayUserHabits());
                habitListView.setAdapter(habitAdapter);
            }
        });

//        final Button allHabitsButton = findViewById(R.id.all_habits_button);
//        allHabitsButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AllHabitsActivity.class);
//                //ArrayList<Habit> allHabits = user.getAllUserHabits();
//                intent.putExtra("user", user);
//                startActivity(intent);
//            }
//        });
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