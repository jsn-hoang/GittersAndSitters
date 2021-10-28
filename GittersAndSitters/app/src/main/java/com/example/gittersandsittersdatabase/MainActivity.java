package com.example.gittersandsittersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Declare variables to be referenced
    private FirebaseAuth mAuth;             // The entry point of the Firebase Authentication SDK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();     // Get an instance of Firebase Authentication SDK


    }
}