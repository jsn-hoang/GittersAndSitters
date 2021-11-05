package com.example.habittracker;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class HabitTest {
    private Habit mockHabit() {

        ArrayList<Integer> weekdays = new ArrayList<>();
        Integer []l = {2,5,6}; // Monday, Friday, Saturday
        for (Integer i : l) {
            weekdays.add(i);
        }
        Habit habit = new Habit("MockHabit", weekdays, Calendar.getInstance(), "Mock habit reason", true);
        return habit;
    }

    private HabitEvent mockHabitEvent() {
        HabitEvent habitEvent = new HabitEvent("MockEventName", Calendar.getInstance());
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
