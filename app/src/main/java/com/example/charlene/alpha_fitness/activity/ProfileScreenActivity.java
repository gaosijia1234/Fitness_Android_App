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
    private ListView lvAverage;
    private ListView lvAllTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sceen);
        setUp();
    }

    public void setUp(){
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

        // Display user info on page
        user = db.getUser();
        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(user.getUsername());
        TextView textViewWeight = findViewById(R.id.textViewWeight);
        textViewWeight.setText(Double.toString(user.getWeight()) + " lbs");
        TextView textViewGender = findViewById(R.id.textViewGender);
        textViewGender.setText(user.getGender());
    }

    public void buttonOnClickUpdate(View view){
        TextView textViewName = findViewById(R.id.textViewName);
        String userName = textViewName.getText().toString();
        TextView textViewWeight = findViewById(R.id.textViewWeight);
        Double weight = Double.parseDouble(textViewWeight.getText().toString().split(" ")[0]);
        TextView textViewGender = findViewById(R.id.textViewGender);
        String gender = textViewGender.getText().toString();

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.updateUser(user, new User(userName, gender, weight));

        Intent newProfielScreenActivity = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(newProfielScreenActivity);
    }
}
