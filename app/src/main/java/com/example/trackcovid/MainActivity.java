package com.example.trackcovid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trackcovid.common.AppDatabase;
import com.example.trackcovid.common.CaseTuple;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Log";
    public static AppDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    RequestQueue requestQueue;
    //Dictionary with date as the key, and a CaseTuple value with daily cases, deaths, recoveries
    Map<String, CaseTuple> CaseDictionary = new HashMap<>();
    TextView dailyCasesCounter, changesInCasesCounter, locationTextView, dateTextView, userLoggedIn;
    final Handler handler = new Handler();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "proj").allowMainThreadQueries().build();
        mAuth = FirebaseAuth.getInstance();

        sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        spEditor = sp.edit();

        dailyCasesCounter = findViewById(R.id.counter_todays_cases);
        changesInCasesCounter = findViewById(R.id.counter_changes_since_yesterday);
        locationTextView = findViewById(R.id.location_textview);
        dateTextView = findViewById(R.id.date_textview);
        userLoggedIn = findViewById(R.id.user_currently_logged_in);
        reloadTextviews();

        //Change Location Button
        final Button changeLocationButton = findViewById(R.id.change_location_button);
        changeLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent changeLocationIntent = new Intent(MainActivity.this, LocationMenuActivity.class);
                startActivity(changeLocationIntent);
            }
        });

        final Button signOutButton = findViewById(R.id.signout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();

                Intent signoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(signoutIntent);
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("country").child("United States");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Refresh API Data Button
        final Button refreshDataButton = findViewById(R.id.button_refresh_api_data);
        refreshDataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //prevent exceeding API rate limit
                refreshDataButton.setEnabled(false);
                requestData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshDataButton.setEnabled(true);
                    }
                }, 5000);
            }
        });
    }

    private boolean hasLocationDataAvailable() {
        FirebaseUser user = mAuth.getCurrentUser();
        String country = null;
        String adminArea = null;

        if (user != null) {
            country = db.locationDao().getCountryName(user.getUid());
            adminArea = db.locationDao().getAdminArea(user.getUid());
        }

        if (country == null || adminArea == null) {
            return false;
        }

        return true;
    }

    private String buildApiUrl(String country, String adminArea) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.disease.sh")
                .appendPath("v3")
                .appendPath("covid-19")
                .appendPath("historical")
                .appendPath(country)
                .appendPath(adminArea)
                .appendQueryParameter("lastdays", "2");
        String url = builder.build().toString();
        return url;
    }

    private void requestData() {

        RequestQueue queue = Volley.newRequestQueue(this);
        Boolean hasLocationData = hasLocationDataAvailable();
        //String url = "https://disease.sh/v3/covid-19/historical/Canada/Ontario?lastdays=2";
        if (hasLocationData) {
            FirebaseUser user = mAuth.getCurrentUser();
            String country = db.locationDao().getCountryName(user.getUid());
            String adminArea = db.locationDao().getAdminArea(user.getUid());
            String url = buildApiUrl(country, adminArea);
            Toast.makeText(getApplicationContext(), "Fetching Data..", Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject responseObject = new JSONObject(response);

                                JSONObject timelineObject = responseObject.getJSONObject("timeline");
                                saveTimelineData(timelineObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            queue.add(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Location Has Not Been Selected", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadTextviews();
        reloadDate();
        requestData();
    }

    private void saveTimelineData(JSONObject timelineObject) {

        String key;
        int changeInCasesSinceYesterday;
        ArrayList<Integer> dailyCases = new ArrayList<>();

        try {
            JSONObject casesObject = timelineObject.getJSONObject("cases");
            JSONObject deathsObject = timelineObject.getJSONObject("deaths");
            JSONObject recoveriesObject = timelineObject.getJSONObject("recovered");

            Iterator<String> keys = casesObject.keys();
            while (keys.hasNext()) {
                key = keys.next();

                int cases = Integer.parseInt(casesObject.getString(key));
                int deaths = Integer.parseInt(deathsObject.getString(key));
                int recovered = Integer.parseInt(recoveriesObject.getString(key));

                dailyCases.add(cases);
                CaseTuple caseTuple = new CaseTuple(cases, deaths, recovered);
                CaseDictionary.put(key, caseTuple);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO: PROPER DATE COMPARISON, SHOULDN'T RELY ON ORDERING OF JSON
        changeInCasesSinceYesterday = dailyCases.get(1) - dailyCases.get(0);
        spEditor.putInt("todaysCases", dailyCases.get(1));
        spEditor.putInt("changeInCasesSinceYesterday", changeInCasesSinceYesterday);
        spEditor.commit();

        refreshCounters();
    }

    private void refreshCounters() {
        int todaysCases = sp.getInt("todaysCases", -1);
        int changeInCasesSinceYesterday = sp.getInt("changeInCasesSinceYesterday", -1);
        String changesCounterValue = Integer.toString(changeInCasesSinceYesterday);
        if (changeInCasesSinceYesterday > 0) {
            changesCounterValue = "+" + changesCounterValue;
        } else {
            changesCounterValue = "-" + changesCounterValue;
        }
        dailyCasesCounter.setText(Integer.toString(todaysCases));
        changesInCasesCounter.setText(changesCounterValue);
    }

    private void reloadTextviews() {
        FirebaseUser user = mAuth.getCurrentUser();
        String country = null;
        String adminArea = null;

        if (user != null) {
            country = db.locationDao().getCountryName(user.getUid());
            adminArea = db.locationDao().getAdminArea(user.getUid());
            userLoggedIn.setText("User ID: \n" + user.getUid());
        }

        if (country == null || adminArea == null) {
            locationTextView.setText(R.string.empty_location);
        } else {
            locationTextView.setText(adminArea + "\n" + country);
        }
    }

    private void reloadDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String dateStr = dateFormatter.format(new Date());
        dateTextView.setText(dateStr);
    }

}