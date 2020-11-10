package com.example.trackcovid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationMenuActivity extends AppCompatActivity {

    final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    static final int LOCATION_REQUEST_CODE = 5;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = "LocationMenuActivity";
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_menu);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        spEditor = sp.edit();

        final Button useGPSLocationButton = findViewById(R.id.useGPSButton);
        useGPSLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkForPermissions();
            }
        });

        final Button manualLocationButton = findViewById(R.id.manualLocationButton);
        manualLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent manualChangeLocationIntent = new Intent(LocationMenuActivity.this, LocationSelectorActivity.class);
                startActivity(manualChangeLocationIntent);
            }
        });
    }

    private void checkForPermissions() {
        //Get permissions if not already granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST_CODE);
            Toast.makeText(getApplicationContext(), "Getting permissions..", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            getUserLocation();
        }
    }

    private void getUserLocation(){
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();

                    spEditor.putLong("latitude", Double.doubleToRawLongBits(latitude));
                    spEditor.putLong("longitude", Double.doubleToRawLongBits(longitude));
                    spEditor.commit();
                    Toast.makeText(getApplicationContext(), "Saved Latitude: " + latitude + ", and " + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                }
                else{
                    Log.d(TAG, "Location was null");
                    Toast.makeText(getApplicationContext(), "Location was null..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Location Task Failure " + e.getLocalizedMessage());
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                //Permission Granted
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getUserLocation();
                    Toast.makeText(getApplicationContext(), "GPS permissions Granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Permission not granted
                    Toast.makeText(getApplicationContext(), "GPS permissions are required to use your location", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}