package com.example.gittersandsittersdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gittersandsittersdatabase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * This class is responsible for allowing a user to register for the HabitTracker app
 */
public class Register extends AppCompatActivity implements View.OnClickListener{

    // Declare variables to be referenced
    TextView banner, registerUser;

    EditText emailName;
    EditText userName;
    EditText userPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        emailName = findViewById(R.id.editTextTextEmailAddress);
        userName = findViewById(R.id.editTextUserName);
        userPassword = findViewById(R.id.editTextUserPassword);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = emailName.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (email.isEmpty()){
            emailName.setError("Email is required");
            emailName.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailName.setError("Please provide valid email!");
            emailName.requestFocus();
            return;
        }

        if(password.isEmpty()){
            userPassword.setError("Password is required");
            userPassword.requestFocus();
        }

        if(password.length() < 6){
            userPassword.setError("Minimum password length is 6 characters");
            userPassword.requestFocus();
            return;
        }


    }


}



























