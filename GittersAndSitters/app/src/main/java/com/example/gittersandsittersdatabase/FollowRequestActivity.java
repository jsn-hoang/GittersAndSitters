package com.example.gittersandsittersdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class represents the activity which displays follow requests from other users and
 * allows the user to request to follow other users
 */
public class FollowRequestActivity extends AppCompatActivity {

    private TextView follow_request_banner;
    private EditText search_username;
    private Button send_request_button;
    private ListView request_list;
    private ArrayAdapter<String> requestAdapter;
    private List<String> requestList;
    private ArrayList<String> requestArrayList;
    private User user;

    private FirebaseAuth mAuth;
    private String current_user;
    private String current_username;
    private String targetUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        user = (User) getIntent().getSerializableExtra("user");

        follow_request_banner = findViewById(R.id.follow_request_banner);
        send_request_button = findViewById(R.id.send_request_button);
        follow_request_banner.setText("Follow Requests");
        send_request_button.setText("Send Request");

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference docRef = collectionReference.document(current_user);

        requestArrayList = new ArrayList<>();
        request_list = findViewById(R.id.request_list);
        requestAdapter = new RequestCustomList(FollowRequestActivity.this, requestArrayList);
        request_list.setAdapter(requestAdapter);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    requestArrayList.clear();
                    requestList = (List<String>) snapshot.getData().get("requests");
                    for (String request : requestList) {
                        requestArrayList.add(request);

                    }
                    requestAdapter.notifyDataSetChanged();

                    Log.d("TAG", "Current data: " + snapshot.getData());
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });


    }

    /**
     * Called when the send request button is clicked
     * @param view
     *  This is the send request button
     */
    public void sendRequest(View view) {
        search_username = (EditText) findViewById(R.id.search_username);
        String user_name = search_username.getText().toString();
        current_username = user.getUsername();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference currentUser = collectionReference.document(mAuth.getCurrentUser().getUid());

        currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> followingList = (List<String>) document.get("following");
                        collectionReference
                                .whereEqualTo("userName", user_name) // <-- This line
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().size() != 0) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    if (document.exists()) {
                                                        String targetUserName = (String) document.get("userName");
                                                        if (followingList.contains(targetUserName)) {
                                                            Toast.makeText(FollowRequestActivity.this, "You are already following this person", Toast.LENGTH_LONG).show();
                                                        } else if (targetUserName.equals(current_username)) {
                                                            Toast.makeText(FollowRequestActivity.this, "You cannot send a request to yourself", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            targetUserId = document.getId();
                                                            DocumentReference targetUserReference = collectionReference.document(targetUserId);
                                                            targetUserReference.update("requests", FieldValue.arrayUnion(current_username));
                                                            Toast.makeText(FollowRequestActivity.this, "Follow request sent", Toast.LENGTH_LONG).show();
                                                        }

                                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(FollowRequestActivity.this, "This user does not exist", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    // deliver back the updated user object on back button pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FollowRequestActivity.this, MenuPage.class);
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}