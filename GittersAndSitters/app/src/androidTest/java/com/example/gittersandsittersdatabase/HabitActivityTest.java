package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitActivityTest {


    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    public ActivityTestRule<HabitActivity> HabitActivityActivityTestRule = new ActivityTestRule<>(HabitActivity.class, true, false);

    private String email = "askiba@ualberta.ca";
    private String password = "123456";


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUserInputScenario() {
        //input some text in the email field
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText(email));
        Espresso.onView(withId(R.id.login_password)).perform(typeText(password), closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_button)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
    }
}