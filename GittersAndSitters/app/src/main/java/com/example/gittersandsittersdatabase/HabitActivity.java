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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * This Activity represents the home page where the user can view and manage their habits.
 */
public class HabitActivity extends AppCompatActivity {

    // Declared variables for referencing
    ListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    User user;
    Habit habit;
    ArrayList<Integer> allIndexList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        allIndexList = new ArrayList<>();

        // Get the user intent
        user = (User) getIntent().getSerializableExtra("user");

        habitListView = findViewById(R.id.habit_listview);
        // Set adapter to todayUserHabits
        habitAdapter = new HabitCustomList(this, user.getTodayUserHabits(), true);
        habitListView.setAdapter(habitAdapter);


        // Initialize the tab layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // refresh current tab
                refreshCurrentTab(tabLayout, habitListView, user);
                allIndexList.clear();
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
                });


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

                // i corresponds to the ith ListView entry
                // However, we need to know if it is the ith entry in "Today's Habits" or "All Habits"
                int tabPosition = tabLayout.getSelectedTabPosition();
                // if i corresponds to "Today's Habits"
                if (tabPosition == 0)
                    // Get the clicked Habit position
                    i = getClickedHabitPosition(i);

                Intent intent = new Intent(HabitActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("position", i);
                habitActivityResultLauncher.launch(intent);
                return false;
            }
        });


        /**
         * Launches AddRemoveEventActivity when user clicks on a ListView entry
         * "Today's Habits" tab must be selected and isCompleted
         * must be false for the activity to launch.
         */
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Get integer corresponding to currently selected tab
                int tabPosition = tabLayout.getSelectedTabPosition();
                // Get habit corresponding to clicked ListView entry
                habit = (Habit) habitListView.getItemAtPosition(i);

                // make sure we're in "Today's Habits" and no Event has been added yet today.
                if (tabPosition == 0 && !habit.isCompletedToday()) {

                    Intent intent = new Intent(HabitActivity.this, AddRemoveEventActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("habit", habit);
                    habitActivityResultLauncher.launch(intent);
                } else if (tabPosition == 1 && user.getAllUserHabits().size() > 1) {
                    habitListView.setSelection(i);
                    if (!allIndexList.contains(i)) {
                        allIndexList.add(i);
                    }
                    if (allIndexList.size() == 1) {
                        Toast.makeText(HabitActivity.this,"Select another habit to swap", Toast.LENGTH_LONG).show();
                    }
                    if (allIndexList.size() == 2) {
                        swap(allIndexList);
                        habitListView.clearChoices();
                    }
                }
            }
        });
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

    /**
     * This method is used when the user opts to Edit a Habit from "Today's Habits"
     * The int position of the Habit in the "Today's Habits" ListView may not correspond
     * with the int position of the Habit in the user's entire habitList.
     * This method computes the int position of the clicked Habit within "All Habits"
     * @param i - the int position of the clicked Habit in "Today's Habits"
     * @return - the int position of the clicked Habit in the user's habitList
     */
    public int getClickedHabitPosition(int i) {
        Habit clickedHabit = user.getTodayUserHabits().get(i);
        boolean found = false;
        for (int j = 0; j < user.getAllUserHabits().size() && !found; j++) {
            Habit habit = user.getAllUserHabits().get(j);
            if (habit.getHabitID().equals(clickedHabit.getHabitID())) {
                found = true;
                i = j;
            }
        }
        return i;
    }

    /**
     * Swaps the positions of two items in the user's habit list
     * @param a_list
     *  This is an ArrayList<Integer> object that contains two elements (indices)
     */
    private void swap(ArrayList<Integer> a_list) {
        int index = a_list.get(0);
        Habit tempHabit = user.getAllUserHabits().get(index);
        user.getAllUserHabits().set(index, user.getAllUserHabits().get(a_list.get(1)));
        user.getAllUserHabits().set(a_list.get(1), tempHabit);
        a_list.clear();
        HabitCustomList habitAdapter = new HabitCustomList(HabitActivity.this, user.getAllUserHabits(), false);
        habitListView.setAdapter(habitAdapter);
        //habitAdapter.notifyDataSetChanged();
    }

    // deliver back the updated user object on back button pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HabitActivity.this, MenuPage.class);
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }
}