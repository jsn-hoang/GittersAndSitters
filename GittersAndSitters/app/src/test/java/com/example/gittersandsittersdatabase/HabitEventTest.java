package com.example.gittersandsittersdatabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class HabitEventTest {


    private Habit habit;
    private String habitID;
    private HabitEvent habitEvent;
    private String habitEventID;
    private String habitName;
    private String habitEventName;
    private Calendar habitEventDate;
    private String habitEventComment;

    // Method for constructing a mockHabit
    private Habit mockHabit() {
        ArrayList<Integer> weekdays = new ArrayList<>();
        Integer []l = {2,5,6}; // Monday, Friday, Saturday
        Collections.addAll(weekdays, l);
        habitID = "habitID";
        habitName = "habitName";


        habit = new Habit(habitID,habitName, weekdays, Calendar.getInstance(), "mock habit reason", true);
        return habit;
    }

    // Method for constructing a mock HabitEvent
    private HabitEvent mockHabitEvent(Habit habit) {
        habitEventID = "habitEventID";
        habitEventName = "testName";
        habitEventDate = Calendar.getInstance();
        habitEventComment = "habitEventComment";
        habitEvent = new HabitEvent(habitEventID, habit.getHabitID(), habitEventName, habitEventDate, habitEventComment);
        return habitEvent;
    }


    /**
     * This test determines whether a HabitEvent is generated with the desired fields
     */
    @Test
    public void testGenerateHabitEvent() {
        habit = mockHabit();
        HabitEvent habitEvent = mockHabitEvent(habit);

        // Test that all habitEvent attributes were generated properly
        assertEquals(habitEventID, habitEvent.getEventID());
        assertEquals(habitEvent.getParentHabitID(), habitID);
        assertEquals(habitEventName, habitEvent.getEventName());
        assertEquals(habitEventDate, habitEvent.getEventDate());
        assertEquals(habitEventComment, habitEvent.getEventComment());
    }

    /**
     * This test determines whether a HabitEvent can be set correctly
     */
    @Test
    public void testSetHabitEvent() {
        habit = mockHabit();
        HabitEvent habitEvent = mockHabitEvent(habit);

        // Test that all habitEvent attributes were generated properly
        String newName = "newName";
        habitEvent.setEventName(newName);
        assertEquals(newName, habitEvent.getEventName());

        Calendar newDate = Calendar.getInstance();
        habitEvent.setEventDate(newDate);
        assertEquals(habitEventDate, newDate);

        String newComment = "newComment";
        habitEvent.setEventComment(newComment);
        assertEquals(newComment, habitEvent.getEventComment());
    }

    /**
     * This test attempts to add a HabitEvent to a Habit's eventList
     */
    @Test
    public void addHabitEventToHabitTest() {
        habit = mockHabit();
        HabitEvent habitEvent = mockHabitEvent(habit);
        habit.addHabitEvent(habitEvent);
        // The Habit's eventList should have size == 1
        assertEquals(1, habit.getHabitEvents().size());
    }

    /**
     * This test atempts to delete a HabitEvent from a Habit's HabitEventList
     */
    @Test
    public void deleteHabitEventFromHabitList() {
        habit = mockHabit();
        habitEvent = mockHabitEvent(habit);
        habit.addHabitEvent(habitEvent);
        // The Habit's eventList should have size == 1
        assertEquals(1, habit.getHabitEvents().size());
        habit.deleteHabitEvent(habitEvent);
        assertEquals(0, habit.getHabitEvents().size());
    }


}
