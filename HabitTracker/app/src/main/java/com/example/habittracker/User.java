package com.example.habittracker;

import android.graphics.Picture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a user of the HabitTracker app.
 */
public class User implements Serializable {

    // Declare variables for referencing
    private String uname;
    private Picture picture;
    private ArrayList<String> following;
    private ArrayList<String> requests;
    private ArrayList<Habit> habitList;

    public User(String uname) {
        this.uname = uname;
        this.following = new ArrayList<>();     // initialize empty following list
        this.requests = new ArrayList<>();      // initialize empty requests list
        this.habitList = new ArrayList<>();     // initialize empty Habit list
    }

    public void addUserHabit(Habit habit) {
        habitList.add(habit);
    }

    public void deleteUserHabit(Habit habit) {
        habitList.remove(habit);
    }
    public Habit getUserHabit(Integer i) {
        return habitList.get(i);
    }

    public void setUserHabit(Integer i, Habit habit) {
        habitList.set(i, habit);
    }

    public ArrayList<Habit> getAllUserHabits(){
        return habitList;
    }

    public ArrayList<Habit> getTodayUserHabits(){
        //TODO needs to be fixed.
        ArrayList<Habit> tempList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i=0; i<habitList.size(); i++){
            if (habitList.get(i).getWeekdays().contains(day)) {
                tempList.add(habitList.get(i));
            }
        }
        return tempList;
    }

    public ArrayList<Habit> getPublicHabits(){
        ArrayList<Habit> tempList = new ArrayList<>();

        for (int i=0; i<habitList.size(); i++){
            if (habitList.get(i).isHabitPublic()) {
                tempList.add(habitList.get(i));
            }
        }
        return tempList;
    }

    public void sendReq(String uname) {
        //TODO DB management
        if (requests.contains(uname)) {
            throw new IllegalArgumentException();
        }
        requests.add(uname);
    }

    public void acceptReq(String uname) {
        //TODO
    }

    public void rejectReq(String uname) {
        //TODO
    }

    public void follow(String uname) {
        //TODO DB managment
        if (following.contains(uname)) {
            throw new IllegalArgumentException();
        }
        following.add(uname);
    }

    public void unfollow(String uname) {
        //TODO DB managment
        if (!following.contains(uname)) {
            throw new IllegalArgumentException();
        }
        following.remove(uname);
    }

}
