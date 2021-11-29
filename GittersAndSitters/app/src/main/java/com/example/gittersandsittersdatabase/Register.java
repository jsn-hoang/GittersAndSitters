package com.example.gittersandsittersdatabase;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is responsible for allowing a user to register for the HabitTracker app
 */
public class Register extends AppCompatActivity{

    // Declare variables to be referenced
    private TextView banner, registerUser;
    private Button loginButton;
    private EditText emailName;
    private EditText userName;
    private EditText userPassword;
    private ProgressBar progressBar;
    private String userID;
    private FirebaseFirestore fStore;
    private ArrayList<String> following;
    private ArrayList<String> requests;
    private ArrayList<String> habitList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        emailName = findViewById(R.id.email);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        // FAB to add a habit
        final Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // FAB to add a habit
        final Button loginButton = findViewById(R.id.return_to_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Register.this, MainActivity.class));
            }
        });



    }

    /**
     * We are implementing onClick to check the id.
     * If the id is registerUser we call the registerUser method
     * If the id is banner we want to go the main activity
     */

    /**
     * registerUser() does error checking to make sure the user is not missing any fields
     * an error is displayed if the user name, email or password field is empty
     * if the password length < 6 or the email address is not registered an error is displayed
     * We are creating a user with email and password that is being stored in the authentication section in firebase
     * if a user is successfully created then a collection is created with the collection path set as Users.
     * If a user is not successfully created an error message is displayed
     * The document path is set as the users unique id.
     * Each user id will have email, following, habitList, requests and username as a field
     * after all fields are put inside an object we have a addOnSuccessListener which will display a
     * success message to the LogCat on success. If it fails to add data to database an error message is displayed
     * @return nothing is returned
     */
    private void registerUser() {
        String email = emailName.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String uname = userName.getText().toString().trim();
        following = new ArrayList<>();
        requests = new ArrayList<>();
        habitList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        collectionReference
                .whereEqualTo("userName", uname) // <-- This line
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().size() != 0) {
                                userName.setError("Username is already taken");
                                userName.requestFocus();
                            } else {
                                /**
                                 * Require user to enter username
                                 */

                                if (uname.isEmpty()) {
                                    userName.setError("Username is required");
                                    userName.requestFocus();
                                    return;
                                }

                                /**
                                 * Require user to enter email
                                 */

                                if (email.isEmpty()) {
                                    emailName.setError("Email is required");
                                    emailName.requestFocus();
                                    return;
                                }

                                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    emailName.setError("Please provide valid email!");
                                    emailName.requestFocus();
                                    return;
                                }

                                /**
                                 * Require user to enter password
                                 */

                                if (password.isEmpty()) {
                                    userPassword.setError("Password is required");
                                    userPassword.requestFocus();
                                    return;
                                }

                                /**
                                 * Require password to have a minimum length of 6
                                 */

                                if (password.length() < 6) {
                                    userPassword.setError("Minimum password length is 6 characters");
                                    userPassword.requestFocus();
                                    return;
                                }
                                // the progress bar shows that our app is loading
                                progressBar.setVisibility(View.VISIBLE);
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                //if user is successfully created and added to authentication section then execute code below

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                                    userID = mAuth.getCurrentUser().getUid();
                                                    fStore = FirebaseFirestore.getInstance();

                                                    // Creating a document reference with Users as collection path and the user id as the document reference
                                                    DocumentReference documentReference = fStore.collection("Users").document(userID);

                                                    // Creating a hash map and adding username, email following, requests and habitList to the object
                                                    Map<String, Object> user = new HashMap<>();

                                                    user.put("userName", uname);
                                                    user.put("email", email);
                                                    user.put("following", Arrays.asList());
                                                    user.put("requests",Arrays.asList());
                                                    user.put("habitList",Arrays.asList());

                                                    // adding user information to firestore database.
                                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                        /**
                                                         * If user is successfully added to firestore display a toast message on screen and in the log
                                                         * Set the progress bar visibility to gone since the app has finished creating user
                                                         */
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Register.this, "User has been successfully registered", Toast.LENGTH_LONG).show();

                                                            progressBar.setVisibility(View.GONE);
                                                            Log.d("TAG", "Data has been added successfully!");

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        /**
                                                         * If user is not successfully added to firestore display a toast message on screen and in the log
                                                         * Set the progress bar visibility to gone since the app has finished its task
                                                         */
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Log.d("TAG", "Data could not be added!" + e.toString());
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    });
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                    // if user is not created and added to authentication section display an error message
                                                    // A possible cause of this failure is that the user is already created
                                                } else {
                                                    Toast.makeText(Register.this, "Failed to register user! Please try again!", Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }

                                            }
                                        });
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}



