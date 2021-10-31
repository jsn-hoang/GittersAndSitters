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

public class UserCustomList extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public UserCustomList(Context context, ArrayList<User> users) {
        super(context, 0, users);
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

        User user = users.get(position);

        TextView userName = view.findViewById(R.id.user_name);

        userName.setText(user.getUsername());

        return view;
    }

}