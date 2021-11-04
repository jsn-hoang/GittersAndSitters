package com.example.gittersandsittersdatabase;

import static androidx.core.content.res.TypedArrayUtils.getText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * AddRemoveHabitActivity has separate functionality for adding a new habit vs editing an existing one.
 * These tests are focused on adding a new habit.
 */
public class AddHabitActivityTest {

    // Declare variables for referencing
    Calendar startDate;
    String startDateString;

    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<AddRemoveHabitActivity> NewHabitActivityTestRule =
            new ActivityTestRule<AddRemoveHabitActivity>(AddRemoveHabitActivity.class, true, true) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

                    // Create a startDate
                    Calendar c = Calendar.getInstance();
                    // Get today's date (to test startDate field)
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

                    // Create and put a User object before launching AddRemoveHabitActivity
                    User user = new User("testUsername","testEmail");
                    Intent result = new Intent(targetContext, AddRemoveHabitActivity.class);
                    result.putExtra("user", user);
                    // By not including putExtra("position", position) this will activate new habit mode
                    return result;
                }
            };

    // Test for correct title and button displays in "New Habit" mode
    @Test
    public void testNewHabitFields() {

        // Test that correct title text is displayed
        Espresso.onView(withId(R.id.add_edit_habit_title_text)).check(matches(withText(("Add a New Habit"))));
        // Test that correct button texts are displayed
        Espresso.onView(withId(R.id.add_habit_button)).check(matches(withText(("CREATE"))));
        Espresso.onView(withId(R.id.cancel_habit_button)).check(matches(withText(("CANCEL"))));
    }

    // Test for correct initial Habit input field displays in "New Habit" mode
    @Test
    public void testInitialAddHabitInputFields() {

        // Check that a blank string is displayed
        Espresso.onView(withId(R.id.habit_name_editText)).check(matches(withText((""))));
        // Check that a blank string is displayed
        Espresso.onView(withId(R.id.habit_reason_editText)).check(matches(withText((""))));
        // Check that the correct date is displayed
        Espresso.onView(withId(R.id.habit_start_date_text)).check(matches(withText((startDateString))));

        // Check that all of the checkboxes are not checked
        Espresso.onView(withId(R.id.sunday_checkbox)).check(matches(isNotChecked()));
        Espresso.onView(withId(R.id.monday_checkbox)).check(matches(isNotChecked()));
        Espresso.onView(withId(R.id.tuesday_checkbox)).check(matches(isNotChecked()));
        Espresso.onView(withId(R.id.wednesday_checkbox)).check(matches(isNotChecked()));
        Espresso.onView(withId(R.id.thursday_checkbox)).check(matches(isNotChecked()));
        Espresso.onView(withId(R.id.friday_checkbox)).check(matches(isNotChecked()));
        Espresso.onView(withId(R.id.saturday_checkbox)).check(matches(isNotChecked()));
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }



}