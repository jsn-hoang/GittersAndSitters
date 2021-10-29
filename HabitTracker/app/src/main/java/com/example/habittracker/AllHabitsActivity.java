package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AllHabitsActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference them later.
    ListView habitList;
    ArrayAdapter<Habit> allHabitsAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habits);

        habitList = findViewById(R.id.all_habits_list);
        // Fetch user Object from .MainActivity
        user = (User) getIntent().getSerializableExtra("user");

        // Set cityList contents to .allUserHabits()
        allHabitsAdapter = new HabitCustomList(this, user.getAllUserHabits());
        habitList.setAdapter(allHabitsAdapter);

        // Logic to return to .MainActivity
        final Button todayHabitsButton = findViewById(R.id.all_habits_back_button);
        todayHabitsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AllHabitsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Button for creating a habit
        final Button addHabitButton = findViewById(R.id.all_add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AllHabitsActivity.this, AddRemoveHabitActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
}