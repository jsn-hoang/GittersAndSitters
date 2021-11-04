package com.example.gittersandsittersdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;

public class HabitActivity extends AppCompatActivity {

    // Declared variables for referencing
    ListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    User user;
    Habit habit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

        habitListView = findViewById(R.id.habit_listview);
        // Set adapter to todayUserHabits
        habitAdapter = new HabitCustomList(this, user.getTodayUserHabits());
        habitListView.setAdapter(habitAdapter);


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // today's habits
                        habitAdapter = new HabitCustomList(HabitActivity.this, user.getTodayUserHabits());
                        habitListView.setAdapter(habitAdapter);
                        break;
                    case 1: // all habits
                        habitAdapter = new HabitCustomList(HabitActivity.this, user.getAllUserHabits());
                        habitListView.setAdapter(habitAdapter);
                        break;
                }
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
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        user = (User) data.getExtras().get("user");
                        // update current tab with data from updated user object
                        switch(tabLayout.getSelectedTabPosition()) {
                            case 0: // today's habits
                                habitAdapter = new HabitCustomList(HabitActivity.this, user.getTodayUserHabits());
                                habitListView.setAdapter(habitAdapter);
                                break;
                            case 1: // all habits
                                habitAdapter = new HabitCustomList(HabitActivity.this, user.getAllUserHabits());
                                habitListView.setAdapter(habitAdapter);
                                break;
                        }
                    }
                    else if (result.getResultCode() == 2) { // DELETE habit
                        Intent data = result.getData();
                        user = (User) data.getExtras().get("user");
                        // update current tab with data from updated user object
                        switch(tabLayout.getSelectedTabPosition()) {
                            case 0: // today's habits
                                habitAdapter = new HabitCustomList(HabitActivity.this, user.getTodayUserHabits());
                                habitListView.setAdapter(habitAdapter);
                                break;
                            case 1: // all habits
                                habitAdapter = new HabitCustomList(HabitActivity.this, user.getAllUserHabits());
                                habitListView.setAdapter(habitAdapter);
                                break;
                        }
                    }
                });

        // manages the result (updated habit object)
        ActivityResultLauncher<Intent> habitEventActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        habit = (Habit) data.getExtras().get("habit");
                        // update events list with data from updated habit object
                        //TODO
                    }
                });

        final FloatingActionButton floatingActionButton = findViewById(R.id.add_habit_FAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                habitActivityResultLauncher.launch(intent);
            }
        });

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

        final Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                habitActivityResultLauncher.launch(intent);
            }
        });

        // launches add habit event
//        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                habit = (Habit) habitListView.getItemAtPosition(i);
//                Intent intent = new Intent(MainActivity.this, HabitEventActivity.class);
//                intent.putExtra("habit", habit);
//                intent.putExtra("mode", "ADD");
//                intent.putExtra("position", i);
//
//            }
//        });

    }
}