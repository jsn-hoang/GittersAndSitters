package com.example.gittersandsittersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Declare variables to be referenced
    //private FirebaseAuth mAuth;             // The entry point of the Firebase Authentication SDK
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        //mAuth = FirebaseAuth.getInstance();     // Get an instance of Firebase Authentication SDK


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,Register.class));
                break;
        }
    }
}