package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitActivityTest {


    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<HabitActivity> HabitActivityTestRule =
            new ActivityTestRule<HabitActivity>(HabitActivity.class, true, true) {

        @Override
        protected Intent getActivityIntent() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Create and put a User object before launching HabitActivity
        User user = new User("testID","testUsername","testEmail");
        Intent result = new Intent(targetContext, HabitActivity.class);
        result.putExtra("user", user);
        return result;
        }
    };


    @Before
    public void setUp() throws Exception {
    }

    // Test functionality of the floating action button
    @Test
    public void testFloatingActionButton() throws InterruptedException {
        // Click the floating action button
        Espresso.onView(withId(R.id.add_habit_FAB)).perform(click());
        // Pause for activity change
        Thread.sleep(1500);
        // Check that the activity has changed to AddRemoveHabitActivity
        Espresso.onView(withId(R.id.activity_add_remove_habit_id)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchingTodayHabitsAndAllHabitsTabs() {
        Espresso.onView(ViewMatchers.withText("All Habits")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Today's Habits")).perform(ViewActions.click());
    }


    // Test functionality of the logout button
    @Test
    public void testLogoutButton() throws InterruptedException {
        // Click logout button
        Espresso.onView(withId(R.id.nav_logout)).perform(click());
        // Pause for activity change
        Thread.sleep(1500);
        // Test if Activity switched to LogoutActivity
        Espresso.onView(withId(R.id.activity_profile_id)).check(matches(isDisplayed()));

    }

    @After
    public void tearDown() throws Exception {
    }
}