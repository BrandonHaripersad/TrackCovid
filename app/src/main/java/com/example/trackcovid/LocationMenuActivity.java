package com.example.trackcovid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LocationMenuActivity extends AppCompatActivity {

    final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    static final int LOCATION_REQUEST_CODE = 5;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = "LocationMenuActivity";
    private FirebaseAuth mAuth;
    //SharedPreferences sp;
    //SharedPreferences.Editor spEditor;
    Geocoder geocoder;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_menu);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        //spEditor = sp.edit();
        mAuth = FirebaseAuth.getInstance();

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

        geocoder = new Geocoder(this, Locale.getDefault());
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
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    //spEditor.putLong("latitude", Double.doubleToRawLongBits(latitude));
                    //spEditor.putLong("longitude", Double.doubleToRawLongBits(longitude));
                    //spEditor.commit();

                    Toast.makeText(getApplicationContext(), "Latitude: " + latitude + ", and " + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String country = addresses.get(0).getCountryName();
                        String adminArea = addresses.get(0).getAdminArea();
                        //May have province or state
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            com.example.trackcovid.common.Location loc = new com.example.trackcovid.common.Location(country, adminArea, longitude, latitude, user.getUid());
                            MainActivity.db.locationDao().insert(loc);
                        }
                        //String city = addresses.get(0).getLocality();
                        //spEditor.putString("country", country);
                        //spEditor.putString("adminArea", adminArea);
                        //spEditor.putString("city", city);
                        //spEditor.commit();

                        Toast.makeText(getApplicationContext(), adminArea + ", " + country, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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