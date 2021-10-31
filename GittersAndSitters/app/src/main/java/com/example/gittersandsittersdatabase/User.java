package com.example.gittersandsittersdatabase;

import java.util.ArrayList;

/**
 * This class is responsible for a user of the HabitTracker app.
 */
public class User {

    // Declare attributes
    private String username;
    private String email;
    private ArrayList<User> following = new ArrayList<>();
    private ArrayList<User> requests = new ArrayList<>();
    //private HabitCustomList habitList = new HabitCustomList();


    // User constructors
    public User(){

    }

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
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

    public ArrayList<User> getFollowing() {
        return following;
    }

    public void addFollowing(User user) {
        if (!following.contains(user)) {
            following.add(user);
        }
    }

    public ArrayList<User> getRequests() {
        return requests;
    }

    public void addRequests(User user) {
        if (!requests.contains(user)) {
            requests.add(user);
        }
    }

}