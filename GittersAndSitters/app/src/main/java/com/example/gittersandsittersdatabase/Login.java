package com.example.gittersandsittersdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailName;
    EditText userPassword;
    Button loginButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        emailName = findViewById(R.id.login_emailAddress);
        userPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailName.getText().toString().trim();
                String passWord = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailName.setError("Email is required to log in");
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    userPassword.setError("Password is required to log in");
                    return;
                }

            }
        });
    }
}
