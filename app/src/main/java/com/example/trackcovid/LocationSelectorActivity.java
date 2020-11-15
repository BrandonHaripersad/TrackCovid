package com.example.trackcovid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class LocationSelectorActivity extends AppCompatActivity {

    Spinner countrySpinner;
    Spinner provinceSpinner;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);
        initializePlaceholderSpinners();

        sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        spEditor = sp.edit();

        //TODO: OK button for selecting location
        final Button submitButton = findViewById(R.id.location_selector_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent BackToMain = new Intent(LocationSelectorActivity.this, MainActivity.class);
                startActivity((BackToMain));
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

    private void initializePlaceholderSpinners() {
        //Initialize Country Spinner

        provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.test_country_array, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spEditor.putString("country", parent.getItemAtPosition(position).toString());
                spEditor.commit();

                if (parent.getItemAtPosition(position).equals("Canada")) {
                    fillSpinner("Canada");

                } else {
                    fillSpinner("test_province_array");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
        public void fillSpinner(String name) {
        if (name.equals("Canada")) {
            ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this, R.array.Provinces, android.R.layout.simple_spinner_item);
            provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            provinceSpinner.setAdapter(provinceAdapter);
        } else {
            ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this, R.array.test_province_array, android.R.layout.simple_spinner_item);
            provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            provinceSpinner.setAdapter(provinceAdapter);
        }
        //Initialize Province Spinner

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spEditor.putString("adminArea", parent.getItemAtPosition(position).toString());
                spEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}