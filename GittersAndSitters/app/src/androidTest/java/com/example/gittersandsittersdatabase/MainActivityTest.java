package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest{

    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<MainActivity> MainActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    // Create and put a User object before launching ProfileActivity
                    //User user = new User("testUsername","testEmail");
                    Intent result = new Intent(targetContext, ProfileActivity.class);
                    //result.putExtra("user", user);
                    return result;
                }
            };

    //private String fakeEmail = "abcd@gmail.com";
    //private String fakePassword = "1379246";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUserInputScenario() {
        //check to see if correct email has been displayed
        Espresso.onView(withId(R.id.login_emailAddress)).check(matches(withText("abc@gmail.com")));

        //Check to see if correct password is returned
        Espresso.onView(withId(R.id.login_password)).check(matches(withText("1379246")));

    }

    @Test
    public void testLoginButton(){
        //Test to see if login button works
        Espresso.onView(withId(R.id.login_button)).perform(click());
        // Test if Activity switched to MainActivity
        Espresso.onView(withId(R.id.activity_main_id)).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {
    }
}