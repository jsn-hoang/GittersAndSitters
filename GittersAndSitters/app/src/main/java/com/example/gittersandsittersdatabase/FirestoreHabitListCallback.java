package com.example.gittersandsittersdatabase;

import java.util.ArrayList;

// https://www.youtube.com/watch?v=0ofkvm97i0s&ab_channel=AlexMamo
public interface FirestoreHabitListCallback {
    void onCallback(ArrayList<Habit> habitList);
}