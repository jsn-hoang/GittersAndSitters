package com.example.gittersandsittersdatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is responsible for a user of the HabitTracker app.
 */
public class User implements Serializable {

    // Declare attributes
    private String username;
    private String email;
    private ArrayList<String> following;
    private ArrayList<String> requests;
    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> eventList;


    // User constructors
    public User(){

    }

    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.following = new ArrayList<>();     // initialize empty following list
        this.requests = new ArrayList<>();      // initialize empty requests list
        this.habitList = new ArrayList<>();     // initialize empty Habit list
        this.eventList = new ArrayList<>();     // initialize empty HabitEvent list
    }

    public User(String username, String email, ArrayList<String> following, ArrayList<String> requests, ArrayList<Habit> habitList) {
        this.username = username;
        this.email = email;
        this.following = following;
        this.requests = requests;
        this.habitList = habitList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void addFollowing(String user) {
        if (!following.contains(user)) {
            following.add(user);
        }
    }

    public void removeFollowing(String user) {
        if (following.contains(user)) {
            following.remove(user);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public void addRequests(String user) {
        if (!requests.contains(user)) {
            requests.add(user);
        }
    }

    public void removeRequests(String user) {
        if (requests.contains(user)) {
            requests.remove(user);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Habit getters and setters
     */
    public void addUserHabit(Habit habit) {
        habitList.add(habit);
    }

    public void deleteUserHabit(Habit habit) {
        habitList.remove(habit);
    }
    public Habit getUserHabit(Integer i) {
        return habitList.get(i);
    }

    /**
     * This method replaces a previous habit with a newly edited one
     * @param i     An integer representing the ith habit
     * @param habit The habit that will be placed at the ith position
     */
    public void setUserHabit(Integer i, Habit habit) {
        habitList.set(i, habit);
    }

    public ArrayList<Habit> getAllUserHabits(){
        return habitList;
    }

    /**
     * This method searches through the full habit list and selects
     * all of the habits that are to be performed today.
     * @return - An arrayList of Habits that are to be performed today
     */
    public ArrayList<Habit> getTodayUserHabits(){

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

    /**
     * This method searches through the full habit list and selects
     * all of the habits that declared as public
     * @return - An arrayList of Habits that are to be performed today
     */
    public ArrayList<Habit> getPublicHabits(){
        ArrayList<Habit> tempList = new ArrayList<>();

        for (int i=0; i<habitList.size(); i++){
            if (habitList.get(i).isHabitPublic()) {
                tempList.add(habitList.get(i));
            }
        }
        return tempList;
    }

    /*
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
*/

}