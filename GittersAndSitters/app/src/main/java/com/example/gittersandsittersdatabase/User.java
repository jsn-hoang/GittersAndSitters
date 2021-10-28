package com.example.gittersandsittersdatabase;

/**
 * This class is responsible for a user of the HabitTracker app.
 */
public class User {

    // Declare attributes
    private String username;
    private String useremail;

    // User constructors
    public User(){
    }
    public User(String username, String useremail){
        this.username = username;
        this.useremail = useremail;
    }
}