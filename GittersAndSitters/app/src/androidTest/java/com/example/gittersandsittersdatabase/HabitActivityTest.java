package com.example.gittersandsittersdatabase;

import android.content.Context;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.gittersandsittersdatabase.HabitActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class HabitActivityTest {

    @Rule
    public final ActivityTestRule<HabitActivity> main = new ActivityTestRule<>(HabitActivity.class, true);

    /*
    @Before
    public void init() {

    }
    @Test
    public void listCount() {

    }


    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Assert.assertEquals("in.curioustools.aad_x_testing2", appContext.getPackageName());
        System.out.println("useAppContext : Test Ran");
    }
     */
}