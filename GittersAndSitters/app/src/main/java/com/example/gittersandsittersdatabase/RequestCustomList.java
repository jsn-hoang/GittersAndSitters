package com.example.gittersandsittersdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class represents a CustomList which displays follow requests
 */
public class RequestCustomList extends ArrayAdapter<String> {
    private ArrayList<String> requests;
    private Context context;

    public RequestCustomList(Context context, ArrayList<String> requests) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.request_content, parent, false);
        }

        String userName = requests.get(position);

        TextView user = view.findViewById(R.id.user_name);

        user.setText(userName);

        Button acceptButton = view.findViewById(R.id.button_accept);
        acceptButton.setText("Accept");

        Button denyButton = view.findViewById(R.id.button_deny);
        denyButton.setText("Deny");

        return view;
    }

}