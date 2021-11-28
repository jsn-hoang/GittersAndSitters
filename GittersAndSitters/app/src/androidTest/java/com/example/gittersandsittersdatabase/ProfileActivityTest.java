package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {

    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<ProfileActivity> ProfileActivityTestRule =
            new ActivityTestRule<ProfileActivity>(ProfileActivity.class, true, true) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    // Create and put a User object before launching ProfileActivity
                    User user = new User("testID","testUsername","testEmail");
                    Intent result = new Intent(targetContext, ProfileActivity.class);
                    result.putExtra("user", user);
                    return result;
                }
            };


    @Before
    public void setUp() throws Exception {
    }

    // Test functionality of the cancel button
    @Test
    public void testCancelButton() {
        // Click the cancel button
        Espresso.onView(withId(R.id.cancel_logout_button)).perform(click());
        // Check that the activity has changed to HabitActivity
        Espresso.onView(withId(R.id.activity_habit_id)).check(matches(isDisplayed()));
    }

    // Test functionality of the logout button
    @Test
    public void testLogoutButton() {
        // Click the logout button
        Espresso.onView(withId(R.id.logout)).perform(click());
        // Test if Activity switched to MainActivity
        Espresso.onView(withId(R.id.activity_main_id)).check(matches(isDisplayed()));

    }

    // Test for displaying username and email of user
    @Test
    public void testDisplayInfo() {
        // Check for correct username
        Espresso.onView(withId(R.id.userName)).check(matches(withText("testUsername")));
        // Check for correct email
        Espresso.onView(withId(R.id.emailAddress)).check(matches(withText("testEmail")));
    }

    @After
    public void tearDown() throws Exception {
    }
}
