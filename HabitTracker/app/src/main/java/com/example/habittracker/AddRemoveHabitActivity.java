package com.example.habittracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * This class is responsible for creating, editing, or deleting a Habit.
 */

//TODO Enable user to specify whether a Habit is public or private

public class AddRemoveHabitActivity extends AppCompatActivity {

    // Declare variables for referencing
    User user;
    Habit habit;
    boolean allHabits;
    int habitPosition;
    EditText habitNameEditText;
    Date habitStartDate;
    ArrayList<CheckBox> weekdayCheckBoxes;
    EditText habitReasonEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_habit);

        // get user Object
        user = (User) getIntent().getSerializableExtra("user");
        // Getting this boolean enables us to return to correct habitListView later
        allHabits = getIntent().getExtras().getBoolean("allHabits");

        // This conditional determines whether user clicked an existing habit or "Add Habit"
        if (getIntent().hasExtra("position"))
            habitPosition = getIntent().getExtras().getInt("allHabits");
        // if habitPosition == -1 -> user clicked "Add Habit"
        else habitPosition = -1;



        // Get views that will be used for user input
        habitNameEditText = findViewById(R.id.habit_name_editText);
        //habitStartDate = ;

        weekdayCheckBoxes = new ArrayList<>();
        CheckBox monday = findViewById(R.id.monday_checkbox);
        CheckBox tuesday = findViewById(R.id.tuesday_checkbox);
        CheckBox wednesday = findViewById(R.id.wednesday_checkbox);
        CheckBox thursday = findViewById(R.id.thursday_checkbox);
        CheckBox friday = findViewById(R.id.friday_checkbox);
        CheckBox saturday = findViewById(R.id.saturday_checkbox);
        CheckBox sunday = findViewById(R.id.sunday_checkbox);

        // Populate weekdayCheckBoxes ArrayList
        List<CheckBox> checkBoxes = Arrays.asList
                (monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        weekdayCheckBoxes.addAll(checkBoxes);

        habitReasonEditText = findViewById(R.id.habit_reason_editText);

        // if habit already exists
        if (habitPosition != -1) {
            // Get the clicked Habit entry
            Habit habit = user.getAllUserHabits().get(habitPosition);

            // Set field entries equal to the attributes of the existing habit
            habitNameEditText.setText(habit.getHabitName());

            // Loop through all of the checkboxes
            for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
                // if the habit is scheduled on weekday(i)
                if (habit.getWeekdays().contains(i)) {
                    // check off weekday(i)
                    weekdayCheckBoxes.get(i).setChecked(true);
                }
            }
            //TODO: Get the habit StartDate

            habitReasonEditText.setText(habit.getHabitReason());

            //TODO: Get the habitPublic boolean
        }

        else {   // User selected "Add Habit

            //TODO: initialize StartDate to today's date
            /*
    initialize the Startdate to today's date
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CANADA);
            date = dateFormat.format(currentDate);
            */
        }


        // This method is responsible for the logic when clicking the "OK" button
        final Button createButton = findViewById(R.id.add_habit_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {

                /**
                 * Retrieve user inputted information
                 */
                String habitName = habitNameEditText.getText().toString();

                ArrayList<DayOfWeek> weekdays = new ArrayList<>();
                /*
                // Loop through all of the checkboxes
                for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
                    // if checkBox(i) is checked
                    if (weekdayCheckBoxes.get(i).isChecked()) {
                        // add weekday(i) to weekdays
                        weekdays.add(DayOfWeek.of(i));
                    }
                }
                */

                String habitReason = habitReasonEditText.getText().toString();

                // if "Add Habit" clicked
                if (habitPosition == -1) {
                    // Create a new Habit
                    weekdays.add(DayOfWeek.MONDAY);

                    Habit habit = new Habit(habitName, weekdays, habitReason, true);
                    user.addUserHabit(habit);
                }

                else { // else edit the existing habit
                    habit.setHabitName(habitName);
                    habit.setWeekdays(weekdays);
                    habit.setHabitReason(habitReason);
                }


                // Finally,navigate back to MainActivity
                Intent intent = new Intent(AddRemoveHabitActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("allHabits", allHabits);
                startActivity(intent);
            }
        });
    }
}


        /*
        // Logic to return to .MainActivity
        final Button todayHabitsButton = findViewById(R.id.add_to_today_habits_button);
        todayHabitsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddRemoveHabitActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Button for navigating to allHabits
        final Button allHabitsButton = findViewById(R.id.add_to_all_habits_button);
        allHabitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddRemoveHabitActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

         */

