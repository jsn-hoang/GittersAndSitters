package com.example.gittersandsittersdatabase;

import static androidx.core.content.res.TypedArrayUtils.getText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static org.hamcrest.core.StringContains.containsString;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * AddRemoveHabitActivity has separate functionality for adding a new habit vs editing an existing one.
 * These tests are focused on adding a new habit.
 */
public class EditHabitActivityTest {


    // Declare variables for referencing
    String habitName;
    ArrayList<Integer> weekdays;
    Calendar startDate;
    String startDateString;
    String habitReason;
    String isHabitPublic;   //TODO implement this attribute so it can be tested
    ArrayList<CheckBox> weekdayCheckBoxes;


    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<AddRemoveHabitActivity> EditHabitActivityTestRule =
            new ActivityTestRule<AddRemoveHabitActivity>(AddRemoveHabitActivity.class, true, true) {


                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


                    // Create and put a User object before launching AddRemoveHabitActivity
                    User user = new User("testUsername", "testEmail");

                    // Create and give the user a habit
                    // Create an arraylist of weekdays
                    weekdays = new ArrayList<>();
                    weekdays.add(1);
                    // Create a startDate
                    Calendar c = Calendar.getInstance();
                    // Get today's date
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    // set cal to today's date;
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, day);
                    startDate = c;
                    // Convert to Calendar object to String
                    startDateString = DateFormat.getDateInstance().format(startDate.getTime());
                    // Set String representation of date to startDate TextView



                    habitName = "habit name";
                    habitReason = "habit reason";
                    Habit habit = new Habit("habit name", weekdays, startDate, "habit reason", true);
                    // Give the habit to the user
                    user.addUserHabit(habit);

                    Intent result = new Intent(targetContext, AddRemoveHabitActivity.class);
                    result.putExtra("user", user);
                    // position of the habit to be edited
                    result.putExtra("position", 0);
                    return result;
                }
            };

    // Test for correct title and button displays in "Edit Habit" mode
    @Test
    public void testEditHabitFields() {

        // Test that correct title text is displayed
        Espresso.onView(withId(R.id.add_edit_habit_title_text)).check(matches(withText(("Edit Habit"))));
        // Test that correct button texts are displayed
        Espresso.onView(withId(R.id.add_habit_button)).check(matches(withText(("UPDATE"))));
        Espresso.onView(withId(R.id.cancel_habit_button)).check(matches(withText(("CANCEL"))));
    }

    // Test for correct Habit input field displays in "Edit Habit" mode
    @Test
    public void testHabitInputFields() {

        // Check that the correct habit name is displayed
        Espresso.onView(withId(R.id.habit_name_editText)).check(matches(withText((habitName))));
        // Check that the correct habit reason is displayed
        Espresso.onView(withId(R.id.habit_reason_editText)).check(matches(withText((habitReason))));
        // Check that the correct date is displayed
        Espresso.onView(withId(R.id.habit_start_date_text)).check(matches(withText((startDateString))));
        // Check that the correct checkboxes are checked
        // Checkboxes are booleans (isChecked)
        // weekdays are integers (Sunday = 1, Monday = 2, ..., Saturday = 7)
        if (weekdays.contains(1))
            Espresso.onView(withId(R.id.sunday_checkbox)).check(matches(isChecked()));
        if (weekdays.contains(2))
            Espresso.onView(withId(R.id.monday_checkbox)).check(matches(isChecked()));
        if (weekdays.contains(3))
            Espresso.onView(withId(R.id.tuesday_checkbox)).check(matches(isChecked()));
        if (weekdays.contains(4))
            Espresso.onView(withId(R.id.wednesday_checkbox)).check(matches(isChecked()));
        if (weekdays.contains(5))
            Espresso.onView(withId(R.id.thursday_checkbox)).check(matches(isChecked()));
        if (weekdays.contains(6))
            Espresso.onView(withId(R.id.friday_checkbox)).check(matches(isChecked()));
        if (weekdays.contains(7))
            Espresso.onView(withId(R.id.saturday_checkbox)).check(matches(isChecked()));
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }



}