package com.example.gittersandsittersdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is responsible for logging out of the HabitTracker app.
 */
public class ProfileActivity extends AppCompatActivity {

    // Declared variables for referencing
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");


        // Fields for displaying customized logout message
        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        final TextView userNameTextView = (TextView) findViewById(R.id.userName);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress);
        greetingTextView.setText("Are you sure you want to logout " + user.getUsername()+ "?");
        userNameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());


        /**
         * This listener is responsible for the logic when the logout button is clicked
         */
        final Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the current user and clears them from disk cache
                FirebaseAuth.getInstance().signOut();
                // Return to login screen
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        /**
         * This listener is responsible for the logic when the cancel button is clicked
         */
        final Button cancelButton = findViewById(R.id.cancel_logout_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MenuPage.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

    }
}
