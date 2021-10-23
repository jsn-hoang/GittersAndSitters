package com.example.habittracker;

import java.util.ArrayList;
import java.util.Date;

public class Habit {

    private enum Weekdays {
        MON, TUES, WED, THURS, FRI, SAT, SUN
    }
    private ArrayList<Weekdays> weekdays;
    private String habitName;
    private Date startDate;
    private String habitReason;
    private Double progress;
    private ArrayList<HabitEvent> habitEventList;

    public Habit(ArrayList<Weekdays> weekdays, String habitName, Date startDate, String habitReason) {
        this.weekdays = weekdays;
        this.habitName = habitName;
        this.startDate = startDate;
        this.habitReason = habitReason;
        this.progress = 0.0;
        this.habitEventList = new ArrayList<>();
    }

    public boolean isCompletedToday() {
        Date today = new Date();
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

    private void updateProgress() {
        //TODO
    }

    // Getters and Setters

    public ArrayList<Weekdays> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(ArrayList<Weekdays> weekdays) {
        this.weekdays = weekdays;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getHabitReason() {
        return habitReason;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}