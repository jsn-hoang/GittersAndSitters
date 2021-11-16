package com.example.gittersandsittersdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
            view = LayoutInflater.from(context).inflate(R.layout.habit_event_content, parent,false);
        }
        HabitEvent habitEvent = habitEventList.get(position);
        TextView habitEventNameText = view.findViewById(R.id.event_name_text);
        TextView habitEventDateText = view.findViewById(R.id.event_date_text);

        habitEventNameText.setText(habitEvent.getEventName());

        // Convert Calendar object to String
        Calendar habitEventDate = habitEvent.getEventDate();
        String s = DateFormat.getDateInstance().format(habitEventDate.getTime());
        // Set s to TextView
        habitEventDateText.setText(s);

        return view;
    }
}