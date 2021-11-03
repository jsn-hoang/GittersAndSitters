package com.example.gittersandsittersdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class provides the logic for displaying HabitEvent objects as ListView entries.
 */
public class HabitEventCustomList extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> habitEventList;
    private Context context;

    public HabitEventCustomList(Context context, ArrayList<HabitEvent> habitEventList){
        super(context,0, habitEventList);
        this.habitEventList = habitEventList;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        HabitEvent habitEvent = habitEventList.get(position);
        //TextView habitName = view.findViewById(R.id.event_name);

        //habitName.setText(habitEvent.getEventName());

        return view;
    }
}