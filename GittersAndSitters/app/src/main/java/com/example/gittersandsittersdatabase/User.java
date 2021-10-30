package com.example.gittersandsittersdatabase;

/**
 * This class is responsible for a user of the HabitTracker app.
 */
public class User {

    // Declare attributes
    public String username;
    public String email;

    // User constructors
    public User(){


    }

    public User(String username, String email,String password){
        this.username = username;
        this.email = email;
    }
}