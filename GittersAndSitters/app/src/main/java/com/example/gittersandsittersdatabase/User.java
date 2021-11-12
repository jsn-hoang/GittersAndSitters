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
    private ArrayList<HabitEvent> habitEventList;


    // User constructors
    public User(){

    }

    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.following = new ArrayList<>();     // initialize empty following list
        this.requests = new ArrayList<>();      // initialize empty requests list
        this.habitList = new ArrayList<>();     // initialize empty Habit list
        this.habitEventList = new ArrayList<>();     // initialize empty HabitEvent list
    }

    public User(String username, String email, ArrayList<String> following,
                ArrayList<String> requests, ArrayList<Habit> habitList /*ArrayList<HabitEvent> habitEventList*/) {
        this.username = username;
        this.email = email;
        this.following = following;
        this.requests = requests;
        this.habitList = habitList;
        this.habitEventList = habitEventList;
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
     * Adds a Habit to this user
     * @param habit habit to add
     */
    public void addUserHabit(Habit habit) {
        habitList.add(habit);
    }

    /**
     * Deletes a Habit from this user
     * @param habit habit to delete
     */
    public void deleteUserHabit(Habit habit) {
        habitList.remove(habit);
    }

    /**
     * Gets the habit object at the specified position in List
     * @param i specified index: Integer
     * @return Habit object
     */
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

    /**
     * Gets all the User's Habits
     * @return ArrayList of Habit objects
     */
    public ArrayList<Habit> getAllUserHabits(){
        return habitList;
    }


    /**
     * This method returns a boolean indicating whether
     * the user has any habits to be performed today
     * @return - A boolean representing whether there are any habits to be performed today
     */
    public boolean userHasTodayHabits() {
        ArrayList<Habit> todayList = getTodayUserHabits();
        // return true if list IS NOT empty, else return false
        return !todayList.isEmpty();
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

    /**
     * This method generates and returns an ArrayList of all of the user's HabitEvents.
     * The list is sorted in reverse chronological order.
     * @return ArrayList<HabitEvent>
     */
    public ArrayList<HabitEvent> getAllUserHabitEvents() {
        return habitEventList;

        /** This code is only needed if we do not implement habitEventList as a User attribute */
        /*
        // Initialize empty habitEventList
        ArrayList<HabitEvent> habitEventList = null;
        // Iterate through all user habits
        for (int i = 0; i< habitList.size(); i++) {
            // get the ith Habit
            Habit habit = habitList.get(i);
            // add all HabitEvents from the ith habit
            habitEventList.addAll(habit.getHabitEvents());
        }
        // sort all of the HabitEvents in reverse chronological order
        // return habitEventList
        */
    }

    /**
     * Returns a habitEvent from habitEventList (specified by integer)
     * @param i - int/index position of the habitEvent in habitEventList
     * @return habitEvent
     */
    public HabitEvent getHabitEvent(Integer i) {
        return habitEventList.get(i);
    }

    /**
     * This method overwrites a previous HabitEvent with a newly edited one.
     * The HabitEvent is overwritten in both the User HabitEventList
     * as well as the parent Habit's HabitEventList
     * @param i
     * @param newHabitEvent
     */
    public void setHabitEvent(Integer i, HabitEvent newHabitEvent) {

        // Get the previous HabitEvent
        HabitEvent oldHabitEvent = habitEventList.get(i);
        // Set the new HabitEvent to habitEventList
        habitEventList.set(i, newHabitEvent);
        // Get the Habit that corresponds to this habitEvent
        Habit habit = getParentHabitOfHabitEvent(newHabitEvent);
        // Replace the HabitEvent in the Habit's habitEventList
        habit.setHabitEvent(oldHabitEvent, newHabitEvent);

    }

    /**
     * This method returns the parent Habit of an existing HabitEvent
     * @param habitEvent
     * @return The parent Habit of the passed HabitEvent
     */
    public Habit getParentHabitOfHabitEvent(HabitEvent habitEvent) {

        // Get the name of the Habit we are looking for
        String habitName = habitEvent.getParentHabitName();
        boolean parentHabitFound = false;
        Habit habit = null;
        // iterate through habitList until we find the Habit we are looking for
        for (int i = 0; i < habitList.size() && !parentHabitFound; i++) {
            habit = habitList.get(i);
            if (habitName.equals(habit.getHabitName()))
                parentHabitFound = true;
        }
        return habit; // Inner loop
    }

    /**
     * This method adds a HabitEvent to the user's habitEventList
     * It also adds a HabitEvent to the parent Habit's habitEventList
     * @return ArrayList<HabitEvent>
     */
    public void addHabitEvent(Habit habit, HabitEvent habitEvent) {

        // Add to User's habitEvent list
        habitEventList.add(habitEvent);
        // Add to Habit's habitEvent list
        habit.addHabitEvent(habitEvent);
    }

    /**
     * This method returns an ArrayList of all of the user's HabitEvents.
     * (As a result of appending HabitEvents, they are sorted in reverse chronological order.)
     * @return ArrayList<HabitEvent>
     *
    public ArrayList<HabitEvent> setUserHabitEvent() {
        return habitEventList;
    }
    */


    /**
     * This method deletes a HabitEvent from the user's habitEventList
     * It also deletes a HabitEvent from the correct Habit's habitEventList
     * @return ArrayList<HabitEvent>
     *
    public void deleteHabitEvent(Habit habit, HabitEvent habitEvent) {
        habitEventList.remove(habitEvent);
        habit.deleteHabitEvent(habitEvent);
    }
     */

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