package com.example.trackcovid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackcovid.common.Country;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Refresh API Data Button
        final Button refreshDataButton = findViewById(R.id.button_refresh_api_data);
        refreshDataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"TODO",Toast.LENGTH_SHORT).show();
            }
        });

        //Change Location Button
        final Button changeLocationButton = findViewById(R.id.change_location_button);
        changeLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent changeLocationIntent = new Intent(MainActivity.this, LocationMenuActivity.class);
                startActivity(changeLocationIntent);
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("country").child("United States");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Country Canada = snapshot.getValue(Country.class);
                TextView country = findViewById(R.id.Country);
                TextView population = findViewById(R.id.population);
                TextView cases = findViewById(R.id.cases);

                country.setText(Canada.getName().toString());
                population.setText(Integer.toString(Canada.getPop()));
                cases.setText(Integer.toString(Canada.getTotal_cases()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}