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
 * This class represents a CustomList which holds User objects
 */
public class UserCustomList extends ArrayAdapter<String> {
    private ArrayList<String> users;
    private Context context;

    public UserCustomList(Context context, ArrayList<String> users) {
        //super(context, 0, users);
        super(context,0,users);
        this.users = users;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.following_content, parent, false);
        }




        // Get the next User
        //User user = users.get(position);
        String user = users.get(position);
        TextView userName = view.findViewById(R.id.user_name);
        userName.setText(user);



        return view;
    }

}