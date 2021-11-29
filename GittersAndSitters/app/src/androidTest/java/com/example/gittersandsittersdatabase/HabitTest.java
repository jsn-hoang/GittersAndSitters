package com.example.gittersandsittersdatabase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class HabitTest {

    private Habit habit;
    private HabitEvent habitEvent;

    // Method for constructing a mockHabit
    private Habit mockHabit() {
        ArrayList<Integer> weekdays = new ArrayList<>();
        Integer []l = {2,5,6}; // Monday, Friday, Saturday
        Collections.addAll(weekdays, l);

        habit = new Habit("mockHabitID","mockHabitName", weekdays, Calendar.getInstance(), "mock habit reason", true);
        return habit;
    }

    // Method for constructing a mock HabitEvent
    private HabitEvent mockHabitEvent() {

        habitEvent = new HabitEvent("mockEventID", habit.getHabitID(), "mockEventName", Calendar.getInstance(),"mockEventComment");
        return habitEvent;
    }



    @Test
    public void testAddEvent() {
        Habit habit = mockHabit();
        HabitEvent habitEvent = mockHabitEvent();
        habit.addHabitEvent(habitEvent);
        assertTrue(habit.getHabitEvents().contains(habitEvent));
        assertEquals(1, habit.getHabitEvents().size());
    }

    @Test
    public void testDeleteEvent() {
        Habit habit = mockHabit();
        HabitEvent habitEvent = mockHabitEvent();
        habit.addHabitEvent(habitEvent);
        assertTrue(habit.getHabitEvents().contains(habitEvent));
        habit.deleteHabitEvent(habitEvent);
        assertFalse(habit.getHabitEvents().contains(habitEvent));
        assertEquals(0, habit.getHabitEvents().size());
    }

    @Test
    public void testIsCompletedToday() {
        Habit habit = mockHabit();
        HabitEvent habitEvent = mockHabitEvent();
        habit.addHabitEvent(habitEvent);
        assertTrue(habit.isCompletedToday());
        habit.deleteHabitEvent(habitEvent);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);

        habitEvent.setEventDate(cal);
        habit.addHabitEvent(habitEvent);

        assertFalse(habit.isCompletedToday());
    }
}
