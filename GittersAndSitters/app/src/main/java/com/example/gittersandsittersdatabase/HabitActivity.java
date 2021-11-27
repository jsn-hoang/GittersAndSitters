package com.example.gittersandsittersdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * This Activity represents the main activity after the user logs in.
 */
public class HabitActivity extends AppCompatActivity {

    // Declared variables for referencing
    ListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    User user;
    Habit habit;

    private DrawerLayout drawer;
//    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

        habitListView = findViewById(R.id.habit_listview);
        // Set adapter to todayUserHabits
        habitAdapter = new HabitCustomList(this, user.getTodayUserHabits(), true);
        habitListView.setAdapter(habitAdapter);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

//        viewPager = (ViewPager) findViewById(R.id.)

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize the tab layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // refresh current tab
                refreshCurrentTab(tabLayout, habitListView, user);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        // manages the result (updated user object)
        ActivityResultLauncher<Intent> habitActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {

                    if (result.getResultCode() != RESULT_CANCELED) { // if user object was updated
                        Intent data = result.getData();
                        user = (User) data.getExtras().get("user");
                        // update current tab with data from updated user object
                        refreshCurrentTab(tabLayout, habitListView, user);
                    }

//                    if (result.getResultCode() == Activity.RESULT_OK) { // a habit was added or updated
//                        Intent data = result.getData();
//                        user = (User) data.getExtras().get("user");
//                        // update current tab with data from updated user object
//                        refreshCurrentTab(tabLayout, habitListView, user);
//                    }
//                    else if (result.getResultCode() == 2) { // DELETE habit
//                        Intent data = result.getData();
//                        user = (User) data.getExtras().get("user");
//                        // update current tab with data from updated user object
//                        refreshCurrentTab(tabLayout, habitListView, user);
//                    }
                });

//        // manages the result (updated habit object)
//        ActivityResultLauncher<Intent> habitEventActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        habit = (Habit) data.getExtras().get("habit");
//                        // update events list with data from updated habit object
//                        //TODO
//                    }
//                });

        // FAB to add a habit
        final FloatingActionButton floatingActionButton = findViewById(R.id.add_habit_FAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                habitActivityResultLauncher.launch(intent);
            }
        });

        // LONG CLICK a habit to edit it
        habitListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HabitActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("position", i);
                habitActivityResultLauncher.launch(intent);
                return false;
            }
        });

        // logout button goes to logout screen (ProfileActivity) to confirm or cancel
        final Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }

        });
        // Event History button goes to HabitEventActivity
        final Button eventHistoryButton = findViewById(R.id.event_history_button);
        eventHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HabitActivity.this, HabitEventActivity.class);
                intent.putExtra("user", user);
                //startActivity(intent);
                habitActivityResultLauncher.launch(intent);
            }
        });

        /**
         * Launches AddRemoveEventActivity when user clicks on a ListView entry
         * "Today's Habits" tab must be selected for Activity to launch
         */
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Get integer corresponding to currently selected tab
                int tabPosition = tabLayout.getSelectedTabPosition();

                if (tabPosition == 0) { // true iff "Today's Habits" tab is currently selected


                    habit = (Habit) habitListView.getItemAtPosition(i); // Get the clicked habit
                    Intent intent = new Intent(HabitActivity.this, AddRemoveEventActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("habit", habit);
                    habitActivityResultLauncher.launch(intent);
                }
            }
        });

        }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    /**
     * Refreshes the tab that is currently selected. A user method is called to get the updated habit list.
     * @param tabLayout tabLayout
     * @param listView ListView to update
     * @param user User that holds updated data
     */
    protected void refreshCurrentTab(TabLayout tabLayout, ListView listView, User user) {
        // update current tab with data from updated user object
        HabitCustomList habitAdapter;
        switch(tabLayout.getSelectedTabPosition()) {
            case 0: // today's habits
                habitAdapter = new HabitCustomList(HabitActivity.this, user.getTodayUserHabits(), true);
                listView.setAdapter(habitAdapter);
                break;
            case 1: // all habits
                habitAdapter = new HabitCustomList(HabitActivity.this, user.getAllUserHabits(), false);
                listView.setAdapter(habitAdapter);
                break;
        }
    }
}