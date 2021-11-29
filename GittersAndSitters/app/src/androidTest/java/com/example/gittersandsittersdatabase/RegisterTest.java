
package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;


import org.junit.After;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisterTest {

    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<Register> RegisterTestRule =
            new ActivityTestRule<Register>(Register.class, true, true) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, Register.class);

                    return result;
                }
            };


    @Before
    public void setUp() throws Exception {
    }




    @Test
    public void testErrorCheckingUserName(){
        Espresso.onView(withId(R.id.userName)).perform(typeText(""));
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.email)).perform(typeText("wiz@wizmail.com"));
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.password)).perform(typeText("123456"));
        Espresso.closeSoftKeyboard();


        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.register_button)).perform(click());
        Espresso.closeSoftKeyboard();
        //Since the user did not add a username the account should not be created
        // If account was created then the activity would change to activity_main
        // Since the account was not created the activity should be activity_register_id
        Espresso.onView(withId(R.id.activity_register_id)).check(matches(isDisplayed()));

        Espresso.closeSoftKeyboard();


    }
    @Test
    public void testErrorCheckingEmail(){
        Espresso.onView(withId(R.id.userName)).perform(typeText("wizendeye232@gmail.com"));

        Espresso.closeSoftKeyboard();

        // Email field left empty so account should not be created
        Espresso.onView(withId(R.id.email)).perform(typeText(""));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.password)).perform(typeText("123456"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.register_button)).perform(click());

        Espresso.closeSoftKeyboard();
        //Since the user did not add an email the account should not be created
        // If account was created then the activity would change to activity_main
        // Since the account was not created the activity should be activity_register_id
        Espresso.onView(withId(R.id.activity_register_id)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.userName)).perform(clearText());
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.email)).perform(clearText());
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.password)).perform(clearText());
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.userName)).perform(typeText("wizendeye232@gmail.com"));
        Espresso.closeSoftKeyboard();
        // Email field incorrect so account should not be created
        Espresso.onView(withId(R.id.email)).perform(typeText("mark"));

        Espresso.closeSoftKeyboard();


        Espresso.onView(withId(R.id.password)).perform(typeText("123456"));

        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.register_button)).perform(click());

        Espresso.closeSoftKeyboard();
        //Since the user did not add a valid email address the account will not be created
        // If account was created then the activity would change to activity_main
        // Since the account was not created the activity should be activity_register_id
        Espresso.onView(withId(R.id.activity_register_id)).check(matches(isDisplayed()));

        Espresso.closeSoftKeyboard();


    }

    @Test
    public void testErrorCheckingPassword(){
        Espresso.onView(withId(R.id.userName)).perform(typeText("wizendeye232@gmail.com"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.email)).perform(typeText("wiz@wizmail.com"));

        Espresso.closeSoftKeyboard();

        // account should not be created since password length is less than 6
        Espresso.onView(withId(R.id.password)).perform(typeText("1"));


        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.register_button)).perform(click());

        Espresso.closeSoftKeyboard();
        //Since the user did not add a password with length >6 account should not be created
        // If account was created then the activity would change to activity_main
        // Since the account was not created the activity should be activity_register_id
        Espresso.onView(withId(R.id.activity_register_id)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.userName)).perform(clearText());
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.email)).perform(clearText());
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.password)).perform(clearText());
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.userName)).perform(typeText("wizendeye232@gmail.com"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.email)).perform(typeText("wiz@wizmail.com"));
        Espresso.closeSoftKeyboard();
        // account should not be created since password field is empty
        Espresso.onView(withId(R.id.password)).perform(typeText(""));

        Espresso.closeSoftKeyboard();
        //Since the user did not add a password with length >6 account should not be created
        // If account was created then the activity would change to activity_main
        // Since the account was not created the activity should be activity_register_id
        Espresso.onView(withId(R.id.register_button)).perform(click());
        Espresso.closeSoftKeyboard();

    }




    @After
    public void tearDown() throws Exception {
    }
}
