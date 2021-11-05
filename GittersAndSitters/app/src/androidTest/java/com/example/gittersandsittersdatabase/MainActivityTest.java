package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
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
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest{

    // Declared variables for referencing
    private String fakeEmail = "abcd@gmail.com";
    private String fakePassword = "1379246";

    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<MainActivity> MainActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    // Create and put a User object before launching ProfileActivity
                    User user = new User("testUsername","testEmail");
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra("user", user);
                    return result;
                }
            };

    @Before
    public void setUp() throws Exception {
    }

    // Test that a non-registered user can't login
    @Test
    public void testNonRegisteredUserLogin() {
        //check to see if correct email has been displayed
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText(fakeEmail));
        Espresso.onView(withId(R.id.login_password)).perform(typeText(fakePassword));
        Espresso.closeSoftKeyboard();
        // Click login button
        Espresso.onView(withId(R.id.login_button)).perform(click());
        // Ensure activity hasn't changed
        Espresso.onView(withId(R.id.activity_main_id)).check(matches(isDisplayed()));
    }


    /**
     * This first set of tests exhausts all of the
     * various inputs that trigger the EditText seterror()
     */
    @Test
    public void testEmptyEmailField() {

        // Click login button
        Espresso.onView(withId(R.id.login_button)).perform(click());
        // Check that correct error is displayed
        Espresso.onView(withId(R.id.login_emailAddress))
                .check(matches(hasErrorText("Email is required")));
    }
    @Test
    public void testInvalidEmail() {
        // Input invalid email type
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText("aaaaa"));
        Espresso.closeSoftKeyboard();
        // Click login button
        Espresso.onView(withId(R.id.login_button)).perform(click());
        // Check that correct error is displayed
        Espresso.onView(withId(R.id.login_emailAddress))
                .check(matches(hasErrorText("Please enter a valid email")));
    }
    @Test
    public void testEmptyPassword() {
        // Input valid email type, but no password
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText("abc@gmail.com"));
        Espresso.closeSoftKeyboard();
        // Click login button
        Espresso.onView(withId(R.id.login_button)).perform(click());
        // Check that correct error is displayed
        Espresso.onView(withId(R.id.login_password))
                .check(matches(hasErrorText("Password is required!")));
    }
    @Test
    public void testInvalidPasswordLength() {
        // Input valid email type
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText("abc@gmail.com"));
        // Input < min password
        Espresso.onView(withId(R.id.login_password)).perform(typeText("1"));
        Espresso.closeSoftKeyboard();
        // Click login button
        Espresso.onView(withId(R.id.login_button)).perform(click());
        // Check that correct error is displayed
        Espresso.onView(withId(R.id.login_password))
                .check(matches(hasErrorText("Min password length is 6 characters!")));
    }


    // Test sign up button functionality
    @Test
    public void testSignUpButton(){
        // Click register button
        Espresso.onView(withId(R.id.register)).perform(click());
        // Test if Activity switched to Register
        Espresso.onView(withId(R.id.activity_register_id)).check(matches(isDisplayed()));
    }

    // Finally, test logging into the app with a registered username and password
    @Test
    public void zzzTestCorrectLogin() throws InterruptedException {

        // Enter valid username and password
        Espresso.onView(withId(R.id.login_emailAddress)).perform(typeText("askiba@ualberta.ca"));
        Espresso.onView(withId(R.id.login_password)).perform(typeText("123456"));
        Espresso.closeSoftKeyboard();
        // Click login button
        Espresso.onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(10000);
        // Test if Activity switched to MainActivity
        Espresso.onView(withId(R.id.activity_habit_id)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }
}