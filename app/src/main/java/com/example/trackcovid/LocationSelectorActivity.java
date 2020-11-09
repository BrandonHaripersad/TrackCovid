package com.example.trackcovid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LocationSelectorActivity extends AppCompatActivity {

    Spinner countrySpinner;
    Spinner provinceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);
       initializePlaceholderSpinners();

        //TODO: OK button for selecting location
        final Button submitButton = findViewById(R.id.location_selector_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TODO",Toast.LENGTH_SHORT).show();
            }
        });

        //Back Button
        final Button backButton = findViewById(R.id.location_selector_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializePlaceholderSpinners(){
        //Initialize Country Spinner
        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.test_country_array, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        //Initialize Province Spinner
        provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,
                R.array.test_province_array, android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAdapter);
    }
}