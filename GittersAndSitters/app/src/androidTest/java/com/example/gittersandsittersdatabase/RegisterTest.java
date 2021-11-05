
package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Assert;
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
                    // Create and put a User object before launching HabitActivity
                    //User user = new User("testUsername","testEmail");
                    Intent result = new Intent(targetContext, Register.class);
                    //result.putExtra("user", user);
                    return result;
                }
            };


    @Before
    public void setUp() throws Exception {
    }

    
    @Test
    public void testFieldIsValid(){
        Espresso.onView(withId(R.id.userName)).perform(typeText("wizendeye232"));
        Espresso.onView(withId(R.id.userName)).check(matches(not(withText(("")))));

        Espresso.onView(withId(R.id.email)).perform(typeText("wiz@wizmail.com"));
        Espresso.onView(withId(R.id.email)).check(matches(not(withText(("")))));


        Espresso.onView(withId(R.id.password)).perform(typeText("123456"));
        Espresso.onView(withId(R.id.password)).check(matches(not(withText(("")))));



    }




    @After
    public void tearDown() throws Exception {
    }
}
