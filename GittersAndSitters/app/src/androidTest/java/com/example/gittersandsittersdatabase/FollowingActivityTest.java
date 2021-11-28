package com.example.gittersandsittersdatabase;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FollowingActivityTest {
    @Rule
    // ActivityTestRule provides functional testing of a single activity.
    // Rule will be launched before each test
    public ActivityTestRule<FollowingActivity> FollowingActivityTestRule =
            new ActivityTestRule<FollowingActivity>(FollowingActivity.class, true, true) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, FollowingActivity.class);

                    return result;
                }
            };


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFollowActivityList(){
        Espresso.onView(withText("mmmcgoey")).perform(ViewActions.click());
    }







    @After
    public void tearDown() throws Exception {
    }
}