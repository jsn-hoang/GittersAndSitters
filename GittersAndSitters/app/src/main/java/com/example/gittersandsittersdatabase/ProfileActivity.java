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

    // Declare variables to be referenced
    private String userID;
    FirebaseAuth mAuth;         // The entry point of the Firebase Authentication SDK
    FirebaseFirestore fStore;   // The entry point for all Cloud Firestore operations
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        logout = findViewById(R.id.logout);

        // This listener handles the logic when the logout button is pressed
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Signs out the current user and clears them from disk cache
                FirebaseAuth.getInstance().signOut();

                // Return to login screen
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        // Get the string that uniquely identifies this user in the Firestore
        userID = mAuth.getCurrentUser().getUid();
        //userID = user.getUid();

        // Get document reference for document with this unique UserID
        DocumentReference docRef = fStore.collection("Users").document(userID);
        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        final TextView userNameTextView = (TextView) findViewById(R.id.userName);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress);

        // This listener reads the document referenced by docRef
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the value to which the specified keys are mapped
                        String username = (String) document.getData().get("userName");
                        String email = (String) document.getData().get("email");

                        greetingTextView.setText("Welcome, " + username);
                        userNameTextView.setText(username);
                        emailTextView.setText(email);

                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }

        });

    }
}
