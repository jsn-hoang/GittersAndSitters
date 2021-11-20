package com.example.gittersandsittersdatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class represents a Habit in the HabitTracker app.
 */
public class Habit implements Serializable {

    private String habitName;
    private ArrayList<Integer> weekdays;    // 1 -> Sunday, 2 -> Monday, ..., 7 -> Saturday
    private Calendar startDate;
    private String habitReason;
    private int progress;
    private boolean habitPublic; // public vs private to users that follow
    private ArrayList<HabitEvent> habitEventList;

    /**
     * Constructor
     * @param habitName Title of habit: String
     * @param weekdays ArrayList containing Integer that represent days of the week (1=Sun : 6=Sat)
     * @param startDate Date to start: Calendar object
     * @param habitReason Reason for habit: String
     * @param habitPublic Public visibility of habit: boolean
     */
    public Habit(String habitName, ArrayList<Integer> weekdays, Calendar startDate, String habitReason, boolean habitPublic) {
        this.habitName = habitName;
        this.weekdays = weekdays;
        this.startDate = startDate;
        this.habitReason = habitReason;
        this.progress = 0;                        // Initialize progress to 0
        this.habitPublic = habitPublic;
        this.habitEventList = new ArrayList<>();
    }

    /**
     * Updates the progress bar value of this habit (0 - 100)
     * based on HabitEvents completed since the start date
     */
    private void updateProgress() {
        //TODO
    }

    /**
     * Checks if this habit has been completed today
     * @return Whether or not it was completed today: boolean
     */
    public boolean isCompletedToday() {

        // check if the Habit's eventList is empty
        if (habitEventList.isEmpty())
            return false;

        // The newest habitEvent will be at the end of habitEventList
        // (This is the habitEvent we are interested in)
        int last = habitEventList.size() - 1;
        HabitEvent habitEvent = habitEventList.get(last);
        // get the Date of the newest habitEvent
        Calendar habitEventDate = habitEvent.getEventDate();

        // Get today's date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Return boolean indicating whether habitEventDate is today's date
        return (habitEventDate.get(Calendar.YEAR) == year
                && habitEventDate.get(Calendar.MONTH) == month
                && habitEventDate.get(Calendar.DAY_OF_MONTH) == day);
    }

    /**
     * Returns a particular habitEvent from habitEventList
     * @param i Integer corresponding to the HabitEvent at position i of habitEventList
     * @return habitEvent
     */
    public HabitEvent getHabitEvent(Integer i) {
        return habitEventList.get(i);
    }


    /**
     * Returns the position of a specified HabitEvent in habitEventList
     * @param habitEvent - the HabitEvent we are searching for
     * @return - an int representing the position of the habitEvent in habitEventList
     */
    public int getHabitEventPosition(HabitEvent habitEvent) {

        boolean isFound = false;
        int position = -1;
        for (int i = 0; i < habitEventList.size() && !isFound; i++) {
            if (habitEventList.get(i) == habitEvent) {
                isFound = true;
                position = i;
            }
        }
        return position;
    }


    /**
     * Overwrites a chosen habitEventList object with a new one
     * @param i - int that represents the position of the HabitEvent to be overwritten
     * @param habitEvent - the new HabitEvent that will overwrite the previous
     */
    public void setHabitEvent(int i, HabitEvent habitEvent) {
        habitEventList.set(i, habitEvent);
    }

    /**
     * Overwrites a chosen habitEventList object with a new one
     * @param oldHabitEvent - the habitEvent to be overwritten
     * @param newHabitEvent - the habitEvent we will overwrite with
     */
    public void setHabitEvent(HabitEvent oldHabitEvent, HabitEvent newHabitEvent) {

        boolean habitEventFound = false;
        // search habitEventList until we find the HabitEvent we are searching for
        for (int i = 0; i < habitEventList.size() && !habitEventFound; i++) {
            HabitEvent habitEvent = habitEventList.get(i);
            // if we find the habitEvent we are looking for
            if (habitEvent == oldHabitEvent) {
                // set the oldHabitEvent to newHabitEvent
                setHabitEvent(i, newHabitEvent);
                habitEventFound = true;
            }
        }
    }

    /**
     * Adds a HabitEvent object and updates the habit progress
     * @param habitEvent habitEvent to add
     */
    public void addHabitEvent(HabitEvent habitEvent) {
        habitEventList.add(habitEvent);
        updateProgress();
    }

    /**
     * Deletes a HabitEvent object from this habit and updates the habit progress
     * @param habitEvent habitEvent to delete
     */
    public void deleteHabitEvent(HabitEvent habitEvent) {
        habitEventList.remove(habitEvent);
        updateProgress();
    }

    /**
     * @return Number of habitsEvents corresponding to this habit: int
     */
    public int countHabitEvents(){
        return habitEventList.size();
    }

    /**
     * @return ArrayList of HabitEvents corresponding to this habit: ArrayList<HabitEvent>
     */
    public ArrayList<HabitEvent> getHabitEvents() {
        return habitEventList;
    }



    // Getters and Setters

    public boolean isHabitPublic() {
        return habitPublic;
    }

    public void setHabitPublic(boolean habitPublic) {
        this.habitPublic = habitPublic;
    }

    /**
     * Returns a list of integers representing days of the week
     * (i.e. 1 = Sunday, 2 = Monday, ..., 7 = Saturday)
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(ArrayList<Integer> weekdays) {
        this.weekdays = weekdays;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * Sets Habit startDate to the one chosen by the user in the date picker fragment.
     * min habitStart date == today's date
     * The HabitStart date cannot be changed after the Habit has started
     * @param startDate a Calendar object
     */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public String getHabitReason() {
        return habitReason;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }
    //TODO

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }




}