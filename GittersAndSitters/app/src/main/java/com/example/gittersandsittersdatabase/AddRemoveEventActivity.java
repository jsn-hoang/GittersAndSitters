package com.example.gittersandsittersdatabase;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;


import java.io.File;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This Activity is responsible for creating, editing, or deleting an Event.
 * This Activity has two "modes": newEvent, and editEvent
 * newEvent mode starts when the the user navigates to this Activity from HabitActivity
 * editEvent mode starts when the the user navigates to this Activity from EventHistoryActivity
 */

//TODO Fragments for habitEventPhoto and habitEventLocation

public class AddRemoveEventActivity extends AppCompatActivity {

    // Declare variables for referencing
    private FirebaseFirestore db;
    private CollectionReference collectionRef;
    public static final int PERMISSIONS_REQUEST_CODE_FINE_LOCATION = 1;
    public static final int REQUEST_CODE_CAMERA = 3;
    public static final int REQUEST_CODE_SELECTLOC = 4;
    public static final int RESULT_DELETE = 2;
    User user;
    Habit habit;                   // The parent Habit of the HabitEvent
    HabitEvent habitEvent;
    Calendar habitEventDate;
    Location habitEventLocation = null;
    Bitmap habitEventPhoto = null;
    boolean isNewHabitEvent;
    int habitListIndex;            // index position of the Habit in the User's habitList
    int habitEventListIndex;       // index position of the HabitEvent in the Habit's habitEventList
    private DataUploader dataUploader;
    ImageView imageView;
    Double userLat;
    Double userLong;

    // location
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_habit_event);

        user = (User) getIntent().getSerializableExtra("user");
        // create dataUploader instance
        dataUploader = new DataUploader(user.getUserID());
        // get the HabitEvent's associated Habit
        habit = (Habit) getIntent().getSerializableExtra("habit");
        // get the index position of the parentHabit in habitList
        habitListIndex = user.getUserHabitPosition(habit);
        // get instance of FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        // get collection reference
        collectionRef = db.collection("Users/" + user.getUserID() + "/Habits/" + habit.getHabitID() + "/HabitEvents/");

        // position intent is only available for an existing HabitEvent
        if (getIntent().hasExtra("position")) {
            isNewHabitEvent = false;
            habitEventListIndex = getIntent().getExtras().getInt("position");
            // get the HabitEvent to be edited
            habitEvent = habit.getHabitEvent(habitEventListIndex);
        }
        // else this is a new HabitEvent
        else isNewHabitEvent = true;


        // Declare variables for xml object referencing
        EditText habitEventNameEditText = findViewById(R.id.event_name_editText);
        EditText habitEventCommentEditText = findViewById(R.id.event_comment_editText);
        final Button deleteButton = findViewById(R.id.delete_event_button);
        final Button addButton = findViewById(R.id.add_event_button);
        final Button cancelButton = findViewById(R.id.cancel_event_button);
        final Button locationButton = findViewById(R.id.event_location_button);
        final TextView header = findViewById(R.id.add_edit_event_title_text);
        final TextView eventDateText = findViewById(R.id.event_date_text);
        final Button eventLocationButton = findViewById(R.id.event_location_button);
        final ImageButton eventPhotoButton = findViewById(R.id.event_photo_button);
        imageView = findViewById(R.id.imageView);


        // setup location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up activity layout
        activityLayoutSetup(isNewHabitEvent, header, addButton, deleteButton);

        // Set HabitEvent date and date TextView
        setHabitEventDateAndField(eventDateText);

        // Set up remaining fields for existing HabitEvent
        if (!isNewHabitEvent) {

            // Set name and comment fields
            habitEventNameEditText.setText(habitEvent.getEventName());
            habitEventCommentEditText.setText(habitEvent.getEventComment());

            //set the location and photo fields
            Location habitEventLocation = habitEvent.getEventLocation();
            Bitmap habitEventPhoto = habitEvent.getEventPhoto();

        }

        // Listener for image button
        // requests image permissions
        eventPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get permissions if not granted already
                if (ContextCompat.checkSelfPermission(AddRemoveEventActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddRemoveEventActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                }
                launchCamera(); // launches if permission granted
            }
        });

        // Listener for location button
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLocation();
            }
        });

        // This Listener is responsible for the logic when clicking the "OK" button
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Retrieve user inputted data
                String habitEventName = habitEventNameEditText.getText().toString();
                String habitEventComment = habitEventCommentEditText.getText().toString();

                // if this is a new HabitEvent
                if (isNewHabitEvent) {

                    // Create the HabitEvent
                    habitEvent = new HabitEvent(habit.getHabitID(), habitEventName,
                            habitEventDate, habitEventComment);
                    
                    if (habitEventPhoto != null) {
                        habitEvent.setEventPhoto(habitEventPhoto);
                    }

                    if (habitEventLocation != null) {
                        habitEvent.setEventLocation(habitEventLocation);
                    }

                    // Add the habitEvent to Firestore and getID
                    String habitEventID = dataUploader.addHabitEventAndGetID(habitEvent, habit);
                    habitEvent.setEventID(habitEventID);
                    habit.addHabitEvent(habitEvent);


                    // Add the new HabitEvent to the Habit's habitEventList
                    habit.addHabitEvent(habitEvent);

                }
                else { // else edit the existing HabitEvent

                    String previousEventName = habitEvent.getEventName();
                    habitEvent.setEventName(habitEventName);
                    habitEvent.setEventLocation(habitEventLocation);
                    habitEvent.setEventComment(habitEventComment);
                    habitEvent.setEventPhoto(habitEventPhoto);
                    // Overwrite the edited HabitEvent
                    habit.setHabitEvent(habitEventListIndex, habitEvent);
                    // Update the edited HabitEvent in FireStore
                    dataUploader.setHabitEvent(habit, habitEvent);
                }

                // Overwrite the edited user Habit
                user.setUserHabit(habitListIndex, habit);   // executed whether we're adding or editing

                // Navigate back to launcher Activity (HabitActivity or HabitEventActivity)
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // remove the habitEvent from the corresponding Habit
                habit.deleteHabitEvent(habitEvent);
                // overwrite the edited userHabit
                user.setUserHabit(habitListIndex, habit);

                // Delete the habitEvent from Firestore
                dataUploader.deleteHabitEvent(habit, habitEvent);


                // Navigate back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_DELETE, intent);
                finish();
            }
        });

    }

    private void launchCamera() {
        if (ContextCompat.checkSelfPermission(
                AddRemoveEventActivity.this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

            // start camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(AddRemoveEventActivity.this, Manifest.permission.CAMERA)) {
            // explain to the user why your app requires this permission for a specific feature to behave as expected.
            new AlertDialog.Builder(this)
                    .setTitle("Required Camera Permission")
                    .setMessage("You need camera permission to access the in-app camera")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(AddRemoveEventActivity.this,
                                    new String[] { Manifest.permission.CAMERA },
                                    REQUEST_CODE_CAMERA);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            // Directly ask for the permission.
            ActivityCompat.requestPermissions(AddRemoveEventActivity.this,
                    new String[] { Manifest.permission.CAMERA },
                    REQUEST_CODE_CAMERA);
        }

    }



    private void fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                AddRemoveEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
//                                if (location != null) {
                                    // Logic to handle location object
                                    Double userLat = location.getLatitude();
                                    Double userLong = location.getLongitude();

                                    // give coordinates to mapsActivity
                                    Intent intent = new Intent(AddRemoveEventActivity.this, MapsActivity.class);
                                    intent.putExtra("LONGITUDE", userLong);
                                    intent.putExtra("LATITUDE", userLat);
                                    startActivityForResult(intent, REQUEST_CODE_SELECTLOC);
                                }
                            }
                        })
                        // often fails if emulator doesn't have a location set
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddRemoveEventActivity.this, "Location could not be found on this device!", Toast.LENGTH_SHORT).show();
                            }
                        });


        } else if (ActivityCompat.shouldShowRequestPermissionRationale(AddRemoveEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // explain to the user why your app requires this permission for a specific feature to behave as expected.

            new AlertDialog.Builder(this)
                    .setTitle("Required Location Permission")
                    .setMessage("You need location permission to access the map")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(AddRemoveEventActivity.this,
                                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                    PERMISSIONS_REQUEST_CODE_FINE_LOCATION);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            // Directly ask for the permission.
            ActivityCompat.requestPermissions(AddRemoveEventActivity.this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSIONS_REQUEST_CODE_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE_FINE_LOCATION) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // permission not granted
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // permission not granted
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            habitEventPhoto = bitmap;
        }

        if (requestCode == REQUEST_CODE_SELECTLOC && resultCode == RESULT_OK) {
            // user chose a location on map
            userLat = (Double) data.getExtras().get("LATITUDE");
            userLong = (Double) data.getExtras().get("LONGITUDE");

            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(userLat);
            location.setLongitude(userLong);
            habitEventLocation = location;
        }
    }

    /**
     * This method sets the Activity and button text to the appropriate titles
     * given whether the user is creating a new habit, or editing and existing one.
     * @param isNewActivityMode - Boolean indicating whether user is creating a new event
     * @param header        - A TextView object that displays the Title of the activity
     * @param addButton     - Button for creating or updating a event
     * @param deleteButton  - Button for deleting an existing event
     */
    private void activityLayoutSetup(boolean isNewActivityMode,
                                     TextView header, Button addButton, Button deleteButton) {

        // get the parent Habit name (to be displayed in header)
        String habitName = habit.getHabitName();

        if (isNewActivityMode){
            // Make activity layout correspond to mode ADD
            header.setText("Add a New " + habitName + " Event");
            // deleteButton disappears, add button says CREATE
            deleteButton.setVisibility(View.GONE);
            addButton.setText("CREATE");
        }
        else {  // Make activity layout correspond to mode EDIT
            header.setText("Edit " + habitName + " Event");
            // add button says UPDATE
            addButton.setText("UPDATE");
        }
    }

    /**
     * This method initializes a TextView object to a particular date
     * For an existing HabitEvent, the Textview object is set to the existing date
     * For a new HabitEvent, the Textview object is set to today's date
     * @param eventDateText - TextView object that will be set to the HabitEvent's start date
     */
    public void setHabitEventDateAndField(TextView eventDateText) {

        // Create Calendar object
        Calendar c = Calendar.getInstance();

        // for new HabitEvent set c to today's date
        if (isNewHabitEvent) {
            // Get today's date
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // set c to today's date;
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

        }
        else { // for existing HabitEvent, set c to existing HabitEvent date
            c = habitEvent.getEventDate();
        }
        // Assign c to habitEventDate
        habitEventDate = c;
        // Convert Calendar object to String
        String dateString = DateFormat.getDateInstance().format(c.getTime());
        // Set String representation of date to eventDateText
        eventDateText.setText(dateString);
    }
}

