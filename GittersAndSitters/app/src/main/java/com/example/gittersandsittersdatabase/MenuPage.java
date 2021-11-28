package com.example.gittersandsittersdatabase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class MenuPage extends AppCompatActivity {

    // private DrawerLayout drawer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

        final Button homeButton = findViewById(R.id.nav_home);
        final Button eventButton = findViewById(R.id.nav_event_history);
        final Button friendButton = findViewById(R.id.nav_friends);
        final Button requestButton = findViewById(R.id.nav_requests);
        final Button logOutButton = findViewById(R.id.nav_logout);

        // manages the result (updated user object)
        ActivityResultLauncher<Intent> menuActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {

                    if (result.getResultCode() != RESULT_CANCELED) { // if user object was updated
                        Intent data = result.getData();
                        user = (User) data.getExtras().get("user");
                    }
                });


        //home button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, HabitActivity.class);
                intent.putExtra("user", user);
                menuActivityResultLauncher.launch(intent);
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, HabitEventActivity.class);
                intent.putExtra("user", user);
                menuActivityResultLauncher.launch(intent);
            }
        });

        //friends button
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, FollowingActivity.class);
                intent.putExtra("user", user);
                menuActivityResultLauncher.launch(intent);
            }
        });

        //friend requests button
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, FollowRequestActivity.class);
                intent.putExtra("user", user);
                menuActivityResultLauncher.launch(intent);
            }
        });

        //log out button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
    
    //to avoid going back to log in page when pressing back 
    @Override
    public void onBackPressed() {
        //
    }
    
}
      
