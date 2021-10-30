package com.example.habittracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

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
    public static final int RESULT_DELETE = 2;

    User user;
    Habit habit;
    boolean allHabits;
    boolean newHabit;
    int habitIndexPosition;
    EditText habitNameEditText;
    Date habitStartDate;
    ArrayList<CheckBox> weekdayCheckBoxes;
    EditText habitReasonEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_habit);

        // get user
        user = (User) getIntent().getSerializableExtra("user");

        // allHabits enables us to return to correct habitListView later
        // allHabits = getIntent().getExtras().getBoolean("allHabits");

        // habitPosition corresponds to which ListView entry was clicked
        if (getIntent().hasExtra("position")) {
            habitIndexPosition = getIntent().getExtras().getInt("position");
            newHabit = false;
        }
        else newHabit = true;       // if no position passed, then this is a new Habit


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
                (sunday, monday, tuesday, wednesday, thursday, friday, saturday);
        weekdayCheckBoxes.addAll(checkBoxes);

        habitReasonEditText = findViewById(R.id.habit_reason_editText);
        final Button deleteButton = findViewById(R.id.delete_habit_button);
        final Button addButton = findViewById(R.id.add_habit_button);
        final Button cancelButton = findViewById(R.id.cancel_habit_button);
        final TextView header = findViewById(R.id.add_edit_habit_title_text);


        // Make activity layout correspond to mode ADD
        String mode = getIntent().getStringExtra("mode");
        if (mode.equals("ADD")) {
            header.setText("Add a New Habit");
            // deleteButton disappears, add button says CREATE
            deleteButton.setVisibility(View.GONE);
            addButton.setText("CREATE");

            //TODO: initialize StartDate to today's date
                        /*
    initialize the Startdate to today's date
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CANADA);
            date = dateFormat.format(currentDate);
            */
        }

        // Make activity layout correspond to mode EDIT
        if (mode.equals("EDIT")) {
            header.setText("Edit Habit");
            // add button says UPDATE
            addButton.setText("UPDATE");

            // Get the clicked Habit entry
            habit = user.getUserHabit(habitIndexPosition);

            // Set field entries equal to the attributes of the existing habit
            habitNameEditText.setText(habit.getHabitName());

            // Check off all CheckBoxes that correspond to current Habit weekdays
            for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
                // if the habit is scheduled on weekday(i+1)
                if (habit.getWeekdays().contains(i+1)) {    // CheckBox i corresponds to day i+1
                    // Check off box(i)
                    weekdayCheckBoxes.get(i).setChecked(true);
                }
            }
            //TODO: Get the habit StartDate

            habitReasonEditText.setText(habit.getHabitReason());

            //TODO: Get the habitPublic boolean
        }


        // This method is responsible for the logic when clicking the "OK" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {

                /**
                 * Retrieve user inputted information
                 */
                String habitName = habitNameEditText.getText().toString();
                String habitReason = habitReasonEditText.getText().toString();

                // Initialize ArrayList to be filled with integers corresponding to checked boxes
                ArrayList<Integer> weekdays = new ArrayList<>();

                // Loop through all of the checkboxes
                for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
                    // if checkBox(i) is checked
                    if (weekdayCheckBoxes.get(i).isChecked()) {
                        // add i to weekdays
                        weekdays.add(i+1);  // // CheckBox i corresponds to day i+1
                    }
                }
                
                // if "Add Habit" clicked
                if (mode.equals("ADD")) {
                    // Create a new Habit

                    Habit newHabit = new Habit(habitName, weekdays, habitReason, true);
                    user.addUserHabit(newHabit);
                }

                else { // else edit the existing habit

                    //TODO Editing an existing habit keeps crashing
                    habit.setHabitName(habitName);
                    habit.setWeekdays(weekdays);
                    habit.setHabitReason(habitReason);
                    user.setUserHabit(habitIndexPosition, habit);
                }

                // Navigate back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.deleteUserHabit(habit);

                // Navigate back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_DELETE, intent);
                finish();
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

