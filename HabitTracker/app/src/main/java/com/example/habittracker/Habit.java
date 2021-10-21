package com.example.habittracker;

import java.util.Date;

public class Habit {

    private enum Weekdays {
        MON, TUES, WED, THURS, FRI, SAT, SUN
    }
    private Weekdays weekdays;
    private String habitName;
    private Date startDate;
    private String habitReason;
    private HabitEventCustomList habitEventList;

    public Habit(Weekdays weekdays, String habitName, Date startDate, String habitReason) {
        this.weekdays = weekdays;
        this.habitName = habitName;
        this.startDate = startDate;
        this.habitReason = habitReason;
    }



    // Getters and Setters
    public Weekdays getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(Weekdays weekdays) {
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
}