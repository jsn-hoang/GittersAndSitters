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

import com.google.android.material.navigation.NavigationView;

public class MenuPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

//        Toolbar toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);

//        drawer = findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        //
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(MenuPage.this, HabitActivity.class).putExtra("user", user));
                break;
            case R.id.nav_event_history:
                startActivity(new Intent(MenuPage.this, HabitEventActivity.class).putExtra("user", user));
                break;
            case R.id.nav_friends:
                startActivity(new Intent(MenuPage.this, FollowFeedActivity.class).putExtra("user", user));
                break;
            case R.id.nav_requests:
                startActivity(new Intent(MenuPage.this, FollowRequestActivity.class).putExtra("user", user));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(MenuPage.this, ProfileActivity.class).putExtra("user", user));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

}