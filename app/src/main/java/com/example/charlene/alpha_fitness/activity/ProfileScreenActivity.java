package com.example.charlene.alpha_fitness.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charlene.alpha_fitness.R;
import com.example.charlene.alpha_fitness.database.DatabaseHelper;
import com.example.charlene.alpha_fitness.model.User;
import com.example.charlene.alpha_fitness.model.Workout;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class ProfileScreenActivity extends AppCompatActivity {
    public static final String TAG = "ProfileScreenActivity";

    private User user;
    private Workout workout;

    private ListView lvAverage;
    private ListView lvAllTime;

    private Spinner spinner;
    private static final String[] genders = {"Male", "Female"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sceen);
        setUp();
    }

    public void setUp(){
        // get distance time workouts and calories from textView
        DatabaseHelper db = DatabaseHelper.getInstance(this);

        // ************************* checked *************************
        // get data from db and display on front-end
        // db getWorkout according to the date, find the whole week
        Date date = new Date();
        int weekday = date.getDay();
        lvAverage = findViewById(R.id.ListViewAverage);
        lvAllTime = findViewById(R.id.ListViewAllTime);

        String[] averageWorkout = db.getWorkoutAverage();
        ArrayAdapter<String> adapterAve = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, averageWorkout);
        lvAverage.setAdapter(adapterAve);

        String[] alltimeWorkout = db.getWorkoutAllTime();
        ArrayAdapter<String> adapterAllTime = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, alltimeWorkout);
        lvAllTime.setAdapter(adapterAllTime);

        addUser(db);

    }

    private void addUser(DatabaseHelper db) {

        // mock data for testing db addUser() -- works
//        User user1 = new User("sijia", "Female", 100.0);
//        db.addUser(user1);

        // drop down menu for gender
        spinner = findViewById(R.id.spinnerGender);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genders);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);
        String gender = spinner.getSelectedItem().toString();

        // get name
        TextView textViewName = findViewById(R.id.textViewName);
        String userName = textViewName.getText().toString();

        // get Weight
        TextView textViewWeight = findViewById(R.id.textViewName);
        Double weight = Double.parseDouble(textViewWeight.getText().toString());

        User user = new User(userName, gender, weight);
        db.addUser(user);
    }






}
