package com.example.trackcovid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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