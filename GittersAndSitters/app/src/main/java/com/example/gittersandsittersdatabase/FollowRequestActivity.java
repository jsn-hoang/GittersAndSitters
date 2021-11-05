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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    private ArrayList<String> requestList;

    private DatabaseReference current_user_ref;
    private FirebaseAuth mAuth;
    private String current_user;
    private String current_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        DocumentReference docRef = collectionReference.document(current_user);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        requestList = (ArrayList<String>) document.getData().get("requests");
                        current_username = (String) document.getData().get("userName");

                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        requestList = new ArrayList<>();
        requestList.add("rocketman111");
        request_list = findViewById(R.id.request_list);
        requestAdapter = new RequestCustomList(this, requestList);
        request_list.setAdapter(requestAdapter);


        follow_request_banner = findViewById(R.id.follow_request_banner);
        search_username = findViewById(R.id.search_username);
        send_request_button = findViewById(R.id.send_request_button);
        follow_request_banner.setText("Follow Requests");
        send_request_button.setText("Send Request");


    }

    /**
     * Called when the send request button is clicked
     * @param view
     */
    public void sendRequest(View view) {
        search_username = (EditText) findViewById(R.id.search_username);
        String user_name = search_username.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference currentUser = collectionReference.document(mAuth.getCurrentUser().getUid());

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : snapshotList) {
                    if (user_name == snapshot.get("userName")) {
                        ArrayList<String> newRequests = (ArrayList<String>) snapshot.get("requests");
                        newRequests.add(current_username);
                        Toast.makeText(FollowRequestActivity.this, "Follow request sent", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}