package com.example.gittersandsittersdatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class is responsible for a user of the HabitTracker app.
 */
public class User implements Serializable{

    // Declare attributes
    private String userID;
    private String username;
    private String email;
    private ArrayList<String> following;
    private ArrayList<String> requests;
    private ArrayList<Habit> habitList;
//    private ArrayList<HabitEvent> habitEventList;


    // User constructors
    public User(){

    }

    public User(String userID, String username, String email){
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.following = new ArrayList<>();     // initialize empty following list
        this.requests = new ArrayList<>();      // initialize empty requests list
        this.habitList = new ArrayList<>();     // initialize empty Habit list
//        this.habitEventList = new ArrayList<>();     // initialize empty HabitEvent list
    }

    public User(String userID, String username, String email, ArrayList<String> following,
                ArrayList<String> requests, ArrayList<Habit> habitList /*ArrayList<HabitEvent> habitEventList*/) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.following = following;
        this.requests = requests;
        this.habitList = habitList;
//        this.habitEventList = habitEventList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
     * Returns the position of a specified Habit in habitList
     * Habit's are compared by habitName (a unique attribute)
     * @param habit - the Habit we are searching for
     * @return - an int representing the position of the Habit in habitList
     */
    public int getUserHabitPosition(Habit habit) {

        boolean isFound = false;
        int position = 0;
        for (int i = 0; i < habitList.size() && !isFound; i++) {
            Habit habitI = habitList.get(i);
            
            // See if habitName matches the name we're looking for
            if (habitI.getHabitName().equals(habit.getHabitName())) {
                isFound = true;
                position = i;
            }
        }
        return position;
    }

    /**
     * This method replaces a previous habit with a newly edited one
     * @param i     An int representing the ith habit
     * @param habit The Habit that will replace the former Habit at the ith position
     */
    public void setUserHabit(int i, Habit habit) {
        habitList.set(i, habit);
    }

    /**
     * Gets all the User's Habits
     * @return ArrayList of Habit objects
     */
    public ArrayList<Habit> getAllUserHabits(){
        return habitList;
    }

    public void setAllUserHabits(ArrayList<Habit> habitList) {
        this.habitList = this.habitList;
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
     * all of the habits that are declared as public
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
    public ArrayList<HabitEvent> getAllHabitEvents() {

        // Initialize empty habitEventList
        ArrayList<HabitEvent> habitEventList = new ArrayList<>();
        // Iterate through all user habits
        for (int i = 0; i< habitList.size(); i++) {
            // get the current Habit
            Habit habit = habitList.get(i);
            // add all HabitEvents from current habit
            habitEventList.addAll(habit.getHabitEvents());
        }
        // return the sorted habitEventList
        Collections.sort(habitEventList, Collections.reverseOrder());
        return habitEventList;

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
        return habit;
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

    /** These methods rely on user.habitEventList, which will probably not be implemented. */
    //    /**
//     * Returns a habitEvent from habitEventList (specified by integer)
//     * @param i - int/index position of the habitEvent in habitEventList
//     * @return habitEvent
//     */
//    public HabitEvent getHabitEvent(Habit habit, Integer i) {
//        return habit.getHabitEvent(i);
//    }
//
//    /**
//     * This method overwrites a previous HabitEvent with a newly edited one.
//     * The HabitEvent is overwritten in both the User HabitEventList
//     * as well as the parent Habit's HabitEventList
//     * @param i
//     * @param newHabitEvent
//     */
//    public void setHabitEvent(Integer i, HabitEvent newHabitEvent) {
//
//        // Get the previous HabitEvent
//        HabitEvent oldHabitEvent = habitEventList.get(i);
//        // Set the new HabitEvent to habitEventList
//        habitEventList.set(i, newHabitEvent);
//        // Get the Habit that corresponds to this habitEvent
//        Habit habit = getParentHabitOfHabitEvent(newHabitEvent);
//        // Replace the HabitEvent in the Habit's habitEventList
//        habit.setHabitEvent(oldHabitEvent, newHabitEvent);
//    }

//    /**
//     * This method adds a HabitEvent to the user's habitEventList
//     * It also adds a HabitEvent to the parent Habit's habitEventList
//     * @return ArrayList<HabitEvent>
//     */
//    public void addHabitEvent(Habit habit, HabitEvent habitEvent) {
//
//        // Add to User's habitEvent list
//        habitEventList.add(habitEvent);
//        // Add to Habit's habitEvent list
//        habit.addHabitEvent(habitEvent);
//    }
//
//    /**
//     * This method returns an ArrayList of all of the user's HabitEvents.
//     * (As a result of appending HabitEvents, they are sorted in reverse chronological order.)
//     * @return ArrayList<HabitEvent>
//     *
//    public ArrayList<HabitEvent> setUserHabitEvent() {
//        return habitEventList;
//    }
//    */
//
//
//    /**
//     * This method deletes a HabitEvent from the user's habitEventList
//     * It also deletes a HabitEvent from the correct Habit's habitEventList
//     * @return ArrayList<HabitEvent>
//     *
//    public void deleteHabitEvent(Habit habit, HabitEvent habitEvent) {
//        habitEventList.remove(habitEvent);
//        habit.deleteHabitEvent(habitEvent);
//    }
//     */


}