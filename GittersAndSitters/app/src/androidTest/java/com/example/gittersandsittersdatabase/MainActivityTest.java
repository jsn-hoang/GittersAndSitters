package com.example.gittersandsittersdatabase;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private String fakeEmail = "abcd@gmail.com";
    private String fakePassword = "1379246";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUserInputScenario() {
        //input some text in the email field
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText(fakeEmail));
    }






    @After
    public void tearDown() throws Exception {
    }
}