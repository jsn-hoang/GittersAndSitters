package com.example.habittracker;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView cityList;
    ArrayAdapter<com.example.habittracker.Habit> cityAdapter;
    ArrayList<com.example.habittracker.Habit> cityDataList;
    ArrayList<DayOfWeek> weekdays;

    @RequiresApi(api = Build.VERSION_CODES.O)           // api required to implement DayOfWeek
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);

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
        weekdays.add(DayOfWeek.FRIDAY);
        String habitReason = "Get in shape";

        Habit habit = new Habit(habitName, weekdays, habitReason, true);

        cityDataList = new ArrayList<>();
        cityDataList.add(habit);


        cityAdapter = new HabitCustomList(this, cityDataList);

        cityList.setAdapter(cityAdapter);

    }


}