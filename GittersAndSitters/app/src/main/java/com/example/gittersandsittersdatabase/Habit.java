package com.example.gittersandsittersdatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a Habit in the HabitTracker app.
 */
public class Habit implements Serializable {

    private String habitName;
    private ArrayList<Integer> weekdays;    // 1 -> Sunday, 2 -> Monday, ..., 6 -> Saturday
    private Calendar startDate;
    private String habitReason;
    private int progress;
    private boolean habitPublic; // public vs private to users that follow
    private ArrayList<HabitEvent> habitEventList;

    public Habit(String habitName, ArrayList<Integer> weekdays, Calendar startDate, String habitReason, boolean habitPublic) {
        this.habitName = habitName;
        this.weekdays = weekdays;
        this.startDate = startDate;
        this.habitReason = habitReason;
        this.progress = 0;                        // Initialize progress to 0
        this.habitPublic = habitPublic;
        this.habitEventList = new ArrayList<>();
    }


    private void updateProgress() {
        //TODO
    }

    public boolean isCompletedToday() {
        Calendar today = Calendar.getInstance();
        for (int i=0; i<habitEventList.size(); i++) {
            if (habitEventList.get(i).getEventDate().compareTo(today)==0) {
                return true;
            }
        }
        return false;
    }

    public void addHabitEvent(HabitEvent habitEvent) {
        habitEventList.add(habitEvent);
        updateProgress();
    }

    public void deleteHabitEvent(int index) {
        habitEventList.remove(index);
        updateProgress();
    }

    public void deleteHabitEvent(HabitEvent habitEvent) {
        habitEventList.remove(habitEvent);
        updateProgress();
    }

    public int countHabitEvents(){
        return habitEventList.size();
    }

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