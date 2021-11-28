package com.example.gittersandsittersdatabase;

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


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, HabitActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, HabitEventActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, FollowFeedActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, FollowRequestActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPage.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        //
    }
    
}
      
