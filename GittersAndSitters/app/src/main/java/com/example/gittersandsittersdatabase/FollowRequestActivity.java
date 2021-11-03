package com.example.gittersandsittersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FollowRequestActivity extends AppCompatActivity {

    private TextView follow_request_banner;
    private EditText search_username;
    private Button send_request_button;

    private DatabaseReference current_user_ref;
    private FirebaseAuth mAuth;
    private String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();
        current_user_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);
        follow_request_banner = findViewById(R.id.follow_request_banner);
        search_username = findViewById(R.id.search_username);
        send_request_button = findViewById(R.id.send_request_button);

    }
}