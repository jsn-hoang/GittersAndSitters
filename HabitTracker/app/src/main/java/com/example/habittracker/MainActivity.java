package com.example.habittracker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView cityList;
    ArrayAdapter<com.example.habittracker.Habit> cityAdapter;
    ArrayList<com.example.habittracker.Habit> cityDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);

        String []cities = {"Edmonton", "Vancouver", "Toronto", "Hamilton", "Denver", "Los Angeles"};
        String []provinces = {"AB", "BC", "ON", "ON", "CO", "CA"};

        cityDataList = new ArrayList<>();

//        for (int i = 0; i < cities.length; i++) {
//            cityDataList.add(new com.example.habittracker.Habit(cities[i], provinces[i]));
//        }

        cityAdapter = new HabitCustomList(this, cityDataList);

        cityList.setAdapter(cityAdapter);

    }


}