package com.example.gittersandsittersdatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gittersandsittersdatabase.databinding.ActivityMapsBinding;

/**
 * MapsActivity loads and displays the mapFragment for the Google Maps API. All background API
 * actions are done inside the fragment (loading tiles, requesting data updates as the user pans
 * around, etc). The map places a pin at the user's current location by default and this pin can
 * be moved by the user. The Done button returns the location selected.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Double userLat, userLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // get the lat and long
        Intent intent = getIntent();
        userLat = intent.getDoubleExtra("LATITUDE", 0);
        userLong = intent.getDoubleExtra("LONGITUDE", 0);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button doneButton = findViewById(R.id.DoneButton);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Done Button returns the location to calling activity
        // the marker's current Lat and Long are returned through intent
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("LATITUDE", userLat);
                intent.putExtra("LONGITUDE", userLong);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * A default marker is placed on the location passed by intent, and camera is centered there. Default zoom is set.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));

        // Add a marker at user location and move the camera
        LatLng defaultLoc = new LatLng(userLat, userLong);
        mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
    }

    /**
     * On Click, the previous marker is removed. A new marker is added at the click location
     * with a distinct label. The camera is smoothly animated to the new marker and the new location
     * is stored to be passed back to intent.
     * @param latLng LatLng of place clicked
     */
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        userLat = latLng.latitude;
        userLong = latLng.longitude;
        LatLng userLoc = new LatLng(userLat, userLong);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLoc).title("Selected Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(userLoc));
    }

}