package com.example.habittracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;


import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/**
 * This class is responsible for creating, editing, or deleting a Habit.
 */

//TODO Enable user to specify whether a Habit is public or private

public class AddRemoveHabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Declare variables for referencing
    public static final int RESULT_DELETE = 2;

    User user;
    Habit habit;
    boolean isNewHabit;
    int habitIndexPosition;
    EditText habitNameEditText;
    Calendar habitStartDate;
    TextView habitStartDateText;
    ArrayList<CheckBox> weekdayCheckBoxes;
    EditText habitReasonEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_habit);

        // get user
        user = (User) getIntent().getSerializableExtra("user");

        // habitPosition corresponds to which ListView entry was clicked
        if (getIntent().hasExtra("position")) {
            habitIndexPosition = getIntent().getExtras().getInt("position");
            isNewHabit = false;
        }
        else isNewHabit = true;     // if no position passed, then this is a new Habit


        // Get views that will be used for user input
        habitNameEditText = findViewById(R.id.habit_name_editText);
        habitStartDateText = findViewById(R.id.habit_start_date_text);
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
        final Button datePickerButton = findViewById(R.id.date_picker_button);
        final TextView header = findViewById(R.id.add_edit_habit_title_text);

        /**
         * Set up activity layout and fields
         */

        // Set up activity layout
        activityLayoutSetup(isNewHabit, header, addButton, deleteButton);

        // Set up Date field
        setDateField(isNewHabit, habitStartDateText);

        // Set up remaining fields for existing habit
        if (!isNewHabit) {

            // Get the habit to be edited
            habit = user.getUserHabit(habitIndexPosition);

            // Set name and reason fields
            habitNameEditText.setText(habit.getHabitName());
            habitReasonEditText.setText(habit.getHabitReason());

            // Set checkboxes
            setDaysToCheckBoxes(habit, weekdayCheckBoxes);

            //TODO: Set the habitPublic boolean
        }


        // This method is responsible for the logic when clicking the "OK" button
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /**
                 * Retrieve user inputted information
                 */

                String habitName = habitNameEditText.getText().toString();
                String habitReason = habitReasonEditText.getText().toString();

                // Get user selected days from checked boxes
                ArrayList<Integer> weekdays = getDaysFromCheckBoxes(weekdayCheckBoxes);

                // habitStartDate is already retrieved

                //TODO: Get the habitPublic boolean

                /**
                 * Create new or edit existing Habit
                 */
                if (isNewHabit) {
                    // Create a new Habit
                    Habit newHabit = new Habit(habitName, weekdays, habitStartDate, habitReason, true);
                    // Add habit to userHabitList
                    user.addUserHabit(newHabit);
                }
                else { // else edit the existing habit

                    habit.setHabitName(habitName);
                    habit.setWeekdays(weekdays);
                    habit.setStartDate(habitStartDate);
                    habit.setHabitReason(habitReason);
                    // Overwrite the previous habit with the edited one
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

        // This method is responsible for the logic when clicking the date picker button
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create bundle to pass data
                Bundle b = new Bundle();
                b.putLong("habitStartDate", habitStartDate.getTimeInMillis());
                // Pass bundle to fragment
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setArguments(b);
                datePickerFragment.show(getSupportFragmentManager(), "ADD_START_DATE");
            }
        });
    }

    // This method sets habitStartDate to the chosen DatePicker date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){

        // Get chosen habitStartDate
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        habitStartDate = cal;

        // Convert to Calendar object to String
        String dateString = DateFormat.getDateInstance().format(cal.getTime());
        // Set dateString to TextView
        habitStartDateText.setText(dateString);
    }

    private void activityLayoutSetup(boolean isNewActivityMode,
                                     TextView header, Button addButton, Button deleteButton) {

        if (isNewActivityMode){

            // Make activity layout correspond to mode ADD
            header.setText("Add a New Habit");
            // deleteButton disappears, add button says CREATE
            deleteButton.setVisibility(View.GONE);
            addButton.setText("CREATE");
        }
        else {  // Make activity layout correspond to mode EDIT
            header.setText("Edit Habit");
            // add button says UPDATE
            addButton.setText("UPDATE");
        }
    }

    public void setDateField(boolean isNewHabit, TextView habitStartDateText) {

        if (isNewHabit) {
            // Initialize habitStartDate TextView to today's date
            habitStartDate = Calendar.getInstance();
            // Get today's date
            int year = habitStartDate.get(Calendar.YEAR);
            int month = habitStartDate.get(Calendar.MONTH);
            int day = habitStartDate.get(Calendar.DAY_OF_MONTH);
            // set cal to today's date;
            habitStartDate.set(Calendar.YEAR, year);
            habitStartDate.set(Calendar.MONTH, month);
            habitStartDate.set(Calendar.DAY_OF_MONTH, day);
        }
        else { // This is an existing Habit with a set startDate
            Habit habit = user.getUserHabit(habitIndexPosition);
            // Get habitStartDate
            habitStartDate = habit.getStartDate();
        }
        // Convert to Calendar object to String
        String dateString = DateFormat.getDateInstance().format(habitStartDate.getTime());
        // Set String representation of date to startDate TextView
        habitStartDateText.setText(dateString);
    }

    public void setDaysToCheckBoxes(Habit habit, ArrayList<CheckBox> checkBoxes) {

        // Check off all CheckBoxes that correspond to current Habit weekdays
        for (int i = 0; i < checkBoxes.size(); i++) {
            // if the habit is scheduled on weekday(i+1)
            if (habit.getWeekdays().contains(i + 1)) {    // CheckBox i corresponds to day i+1
                // Check off box(i)
                checkBoxes.get(i).setChecked(true);
            }
        }
    }

    public ArrayList<Integer> getDaysFromCheckBoxes(ArrayList<CheckBox> checkBoxes) {

        // Initialize new weekdays arraylist
        ArrayList<Integer> days = new ArrayList<>();
        // Loop through all of the checkboxes
        for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
            // if checkBox(i) is checked
            if (weekdayCheckBoxes.get(i).isChecked()) {
                // add i to weekdays
                days.add(i + 1);  // // CheckBox i corresponds to day i+1
            }
        }
        return days;
    }
}
