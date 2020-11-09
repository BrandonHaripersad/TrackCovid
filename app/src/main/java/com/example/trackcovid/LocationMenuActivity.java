package com.example.trackcovid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LocationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_menu);

        //TODO: USE GPS FOR LOCATION
        //Use GPS
        final Button useGPSLocationButton = findViewById(R.id.useGPSButton);
        useGPSLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TODO",Toast.LENGTH_SHORT).show();
            }
        });

        //Manually Change Location
        final Button manualLocationButton = findViewById(R.id.manualLocationButton);
        manualLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent manualChangeLocationIntent = new Intent(LocationMenuActivity.this, LocationSelectorActivity.class);
                startActivity(manualChangeLocationIntent);
            }
        });
    };
}