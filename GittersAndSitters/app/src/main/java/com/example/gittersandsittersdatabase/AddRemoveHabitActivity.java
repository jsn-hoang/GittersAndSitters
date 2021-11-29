package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;


/**
 * This class is responsible for creating, editing, or deleting a Habit.
 */

public class AddRemoveHabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Declare variables for referencing
    public static final int RESULT_DELETE = 2;
    private User user;
    private Habit habit;
    private boolean isNewHabit;
    private int habitIndexPosition;
    private Calendar habitStartDate;
    private TextView habitStartDateText;
    private ArrayList<CheckBox> weekdayCheckBoxes;
    private DataUploader dataUploader;

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
        // if no position passed, then this is a new Habit
        else isNewHabit = true;


        dataUploader = new DataUploader(user.getUserID());

        // Get views that will be used for user input

        EditText habitNameEditText = findViewById(R.id.habit_name_editText);
        habitStartDateText = findViewById(R.id.habit_start_date_text);
        weekdayCheckBoxes = new ArrayList<>();
        RadioButton publicRadioButton = findViewById(R.id.public_radio_button);
        RadioButton privateRadioButton = findViewById(R.id.private_radio_button);
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

        EditText habitReasonEditText = findViewById(R.id.habit_reason_editText);
        final Button deleteButton = findViewById(R.id.delete_habit_button);
        final Button addButton = findViewById(R.id.add_habit_button);
        final Button cancelButton = findViewById(R.id.cancel_habit_button);
        final Button datePickerButton = findViewById(R.id.date_picker_button);
        final TextView header = findViewById(R.id.add_edit_habit_title_text);


        // Set up activity layout
        activityLayoutSetup(isNewHabit, header, addButton, deleteButton);

        // Set up Date field
        setDateField(isNewHabit, habitStartDateText);

        // initialize publicRadioButton to checked for new Habit
        if (isNewHabit)
            publicRadioButton.setChecked(true);

        // Set up remaining fields for existing habit
        if (!isNewHabit) {

            // Get the habit to be edited
            habit = user.getUserHabit(habitIndexPosition);

            // Set name and reason fields
            habitNameEditText.setText(habit.getHabitName());
            habitReasonEditText.setText(habit.getHabitReason());

            // Set checkboxes
            setDaysToCheckBoxes(habit, weekdayCheckBoxes);

            // Set public/private radio button
            if (habit.isPublic())
                publicRadioButton.setChecked(true);
            else privateRadioButton.setChecked(true);
        }


        // This Listener is responsible for the logic when clicking the confirm button
        addButton.setOnClickListener(v -> {

            // Retrieve user inputted data
            String habitName = habitNameEditText.getText().toString();
            String habitReason = habitReasonEditText.getText().toString();

            // Get user selected days from checked boxes
            ArrayList<Integer> weekdays = getDaysFromCheckBoxes();

            // habitStartDate is already retrieved

            // get isPublic
            boolean isPublic = publicRadioButton.isChecked();

            // Determine if user has inputted valid data
            boolean isValidInput = isValidInputChecker(habitName, habitReason);

            // if valid input
            if (isValidInput) {
                // if new Habit
                if (isNewHabit) {

                    habit = new Habit(habitName, weekdays, habitStartDate, habitReason, isPublic);
                    // Add the habit to db and get its ID
                    String habitID = dataUploader.addHabitAndGetID(habit);
                    habit.setHabitID(habitID);
                    user.addUserHabit(habit);
                } else { // else edit the existing habit
                    habit.setHabitName(habitName);
                    habit.setWeekdays(weekdays);
                    habit.setStartDate(habitStartDate);
                    habit.setHabitReason(habitReason);
                    habit.setHabitPublic(isPublic);
                    // Reset the parentHabitName of all HabitEvent's in the Habit's habitEventList
                    habit.setParentNameOfEvents();

                    // Overwrite the previous habit with the edited one
                    user.setUserHabit(habitIndexPosition, habit);
                    // Edit the document in Firestore
                    dataUploader.setHabit(habit);
                }
                // Navigate back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        cancelButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        deleteButton.setOnClickListener(view -> {

            // Delete habit from user habitList
            user.deleteUserHabit(habit);

            // Delete habit from Firebase
            dataUploader.deleteHabit(habit);

            // Navigate back to MainActivity
            Intent intent = new Intent();
            intent.putExtra("user", user);
            setResult(RESULT_DELETE, intent);
            finish();
        });

        /** This listener is responsible for the logic when clicking the date picker button
         */
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean isAlreadyStarted = false;
                // if existing Habit
                if (!isNewHabit) {
                    // Check if the Habit has already started
                    isAlreadyStarted = isHabitAlreadyStarted();
                }

                if (isAlreadyStarted)
                    Toast.makeText(AddRemoveHabitActivity.this, "Cannot change start date. This Habit has already started", Toast.LENGTH_LONG).show();

                // else Habit is new or hasn't started
                else {


                    // Create bundle to pass data
                    Bundle b = new Bundle();
                    b.putLong("date", habitStartDate.getTimeInMillis());
                    // Pass bundle to fragment
                    DialogFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setArguments(b);
                    datePickerFragment.show(getSupportFragmentManager(), "ADD_START_DATE");
                }
            }
        });
    }

    /** This method sets habitStartDate to the chosen DatePicker date
     * @param view
     * @param year
     * @param month
     * @param day
     */
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

    /**
     * This method sets the Activity and button text to the appropriate titles
     * given whether the user is creating a new habit, or editing and existing one.
     * @param isNewActivityMode - Boolean indicating whether user is creating a new habit
     * @param header        - A TextView object that displays the Title of the activity
     * @param addButton     - Button for creating or updating a habit
     * @param deleteButton  - Button for deleting an existing habit
     */
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

    /**
     * This method initializes a TextView object to a particular date
     * For an existing habit, the Textview object is set to the existing start date
     * For a new habit, the Textview object is set to today's date
     * @param isNewHabit        - boolean representing whether this is a new habit
     * @param habitStartDateText - TextView object that will be set to the habit's start date
     */
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
        else { // This is an existing Habit with an existing startDate
            Habit habit = user.getUserHabit(habitIndexPosition);
            // Get habitStartDate
            habitStartDate = habit.getStartDate();
        }
        // Convert Calendar object to String
        String dateString = DateFormat.getDateInstance().format(habitStartDate.getTime());
        // Set String representation of date to startDate TextView
        habitStartDateText.setText(dateString);
    }

    /**
     * This method sets checkboxes to checked or not, depending
     * on if the existing habit is due on that particular day
     * @param habit - The habit whose habit.getWeekdays() we are interested
     * @param checkBoxes - a list of checkboxes whose statuses (checked/unchecked)
     *                     will be determined by what weekdays the habit occurs on
     */
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
    /**
     * This method populates a list of integers depending on whether checkboxes are checked
     * If a checkbox representing a certain day is checked, then the integer representing that day
     * is added to the list (Sunday = 1, Monday = 2, ..., Saturday = 6)
     * @return - ArrayList<Integer> corresponding to days of the week
     */
    public ArrayList<Integer> getDaysFromCheckBoxes() {

        // Initialize new weekdays arraylist
        ArrayList<Integer> days = new ArrayList<>();
        // Loop through all of the checkboxes
        for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
            // if checkBox(i) is checked
            if (weekdayCheckBoxes.get(i).isChecked()) {
                // add i to weekdays
                days.add(i + 1);        // CheckBox i corresponds to day i+1
            }
        }
        return days;
    }

    /** This method determines if the user has inputted valid data for adding or editing a Habit.
     *  A boolean is returned corresponding to whether or not the inputted data is valid.
     * @param habitName - A String object of the proposed habit name
     * @param habitReason - A String object of the proposed habit reason
     * @return - a boolean
     */
    public boolean isValidInputChecker(String habitName, String habitReason){

        // initalize boolean to true
        boolean isValidInput = true;

        // Ensure user entered a habitName
        if (habitName.equals("")) {
            Toast.makeText(AddRemoveHabitActivity.this, "Please give this habit a name.", Toast.LENGTH_LONG).show();
            isValidInput = false;
        }
        // Ensure habitName <= 20 chars
        if (habitName.length() > 20) {
            Toast.makeText(AddRemoveHabitActivity.this, "Please give this habit a shorter name (maximum of 20 characters)", Toast.LENGTH_LONG).show();
            isValidInput = false;
        }

        // Ensure habitName <= 20 chars
        if (habitReason.length() > 30) {
            Toast.makeText(AddRemoveHabitActivity.this, "Please give this habit a shorter reason (maximum of 30 characters)", Toast.LENGTH_LONG).show();
            isValidInput = false;
        }

        // if non-unique habitName inputted
        if (!user.isUniqueHabitName(habitName)) {
            // if new Habit OR the non-unique name does not belong to the habit being edited
            if ((isNewHabit || !habit.getHabitName().equals(habitName))) {
                Toast.makeText(AddRemoveHabitActivity.this, "This habit already exists. Please choose a unique name.", Toast.LENGTH_LONG).show();
                isValidInput = false;
            }
        }
        // if no weekdays are selected
        if (!isAnyChecked()) {
            Toast.makeText(AddRemoveHabitActivity.this, "Please select at least one day to perform this habit on.", Toast.LENGTH_LONG).show();
            isValidInput = false;
        }
        return isValidInput;
    }

    /**
     * This method returns a boolean corresponding to whether any of the Checkboxes are checked
     * @return - a boolean
     */
    public boolean isAnyChecked() {

        // loop through all the checkboxes
        for (int i = 0; i < weekdayCheckBoxes.size(); i++) {
            // if a box is checked
            if (weekdayCheckBoxes.get(i).isChecked())
                // return true
                return true;
        }
        // if this line is reached, then no boxes are checked
        return false;
    }


    /**This method checks whether an existing Habit has already been started
     * The Habit's start date is compared to the current date
     * @return boolean corresponding to whether or not the Habit has already started
     */
    public boolean isHabitAlreadyStarted() {

        // get Habit start date
        Calendar startDate = habit.getStartDate();
        int startYear = startDate.get(Calendar.YEAR);
        int startMonth = startDate.get(Calendar.MONTH);
        int startDay = startDate.get(Calendar.DAY_OF_MONTH);
        // getCurrentDate
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // if current year doesn't match start year
        if (startYear != year)
            // return whether the Habit has already started
            return (startYear < year);
        // else current year == start year
        // if current year doesn't match start year
        if (startMonth != month)
            // return whether the Habit has already started
            return startMonth < month;
        // else year and month are equal
            // return whether the Habit has already started
        else return startDay <= day;
    }

}
