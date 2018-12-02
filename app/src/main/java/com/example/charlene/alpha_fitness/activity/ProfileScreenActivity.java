package com.example.charlene.alpha_fitness.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.example.charlene.alpha_fitness.R;
import com.example.charlene.alpha_fitness.database.DatabaseHelper;
import com.example.charlene.alpha_fitness.model.User;
import com.example.charlene.alpha_fitness.model.Workout;

import java.util.ArrayList;
import java.util.Date;

public class ProfileScreenActivity extends AppCompatActivity {
    public static final String TAG = "ProfileScreenActivity";

    private User user;
    private Workout workout;

    private ListView lvAverage;
    private ListView lvAllTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sceen);
        setUp();
    }

    public void setUp(){
        // get distance time workouts and calories from textView
        DatabaseHelper db = DatabaseHelper.getInstance(this);

        // move to ProfileScreenActivity onCreate()
//        // ************************* unchecked *************************
//        // mock data to checkout addWorkout in db, this code should be in MapsActivity inside of startBtn onclick,
//        // but since they perform the same, so check in here
        Workout workout1 = new Workout("1/1/2018",12.2,100.0,12.0, 1.1,2.1,0.1);
//        // db should have the mock data
        db.addWorkout(workout1);

        // ************************* checked *************************
        // get data from db and display on front-end
        // db getWorkout according to the date, find the whole week
        Date date = new Date();
        int weekday = date.getDay();
        lvAverage = findViewById(R.id.ListViewAverage);
        lvAllTime = findViewById(R.id.ListViewAllTime);

        String[] averageWorkout = db.getWorkoutAverage(date.toString(), weekday);
        ArrayAdapter<String> adapterAve = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, averageWorkout);
        lvAverage.setAdapter(adapterAve);

        String[] alltimeWorkout = db.getWorkoutAllTime();
        ArrayAdapter<String> adapterAllTime = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, alltimeWorkout);
        lvAllTime.setAdapter(adapterAllTime);

    }







}
