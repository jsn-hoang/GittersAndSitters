package com.example.gittersandsittersdatabase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String currentUserId = mAuth.getCurrentUser().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference collectionRef = db.collection("Users");
                DocumentReference docRef = collectionRef.document(currentUserId);

                String targetUserName = requests.get(position);
                docRef.update("requests", FieldValue.arrayRemove(targetUserName));

                collectionRef
                        .whereEqualTo("userName", targetUserName) // <-- This line
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String targetUserId;
                                    for (DocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            targetUserId = document.getId();
                                            DocumentReference targetUserReference = collectionRef.document(targetUserId);
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            String current_username = (String) document.get("userName");
                                                            targetUserReference.update("following", FieldValue.arrayUnion(current_username));

                                                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                                        } else {
                                                            Log.d("TAG", "No such document");
                                                        }
                                                    } else {
                                                        Log.d("TAG", "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                            Log.d("TAG", document.getId() + " => " + document.getData());
                                        }
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });


        Button denyButton = view.findViewById(R.id.button_deny);
        denyButton.setText("Deny");
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String currentUserId = mAuth.getCurrentUser().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference collectionRef = db.collection("Users");
                DocumentReference docRef = collectionRef.document(currentUserId);
                docRef.update("requests", FieldValue.arrayRemove(requests.get(position)));

            }
        });

        return view;
    }

}