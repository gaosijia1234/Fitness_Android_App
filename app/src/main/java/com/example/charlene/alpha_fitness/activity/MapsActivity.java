package com.example.charlene.alpha_fitness.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.charlene.alpha_fitness.MyService;
import com.example.charlene.alpha_fitness.R;
import com.example.charlene.alpha_fitness.database.DatabaseHelper;
import com.example.charlene.alpha_fitness.model.Workout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

//    private DatabaseHelper db;
    private Workout workout;  // to call setter method,


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // need to be debug.. for testing purpose without clicking on the btn
        //
        DatabaseHelper db = DatabaseHelper.getInstance(this);

        //        // mock data to checkout addWorkout in db,
//        // this code should be inside of startBtn onclick function(last function in this page),

        // another week_of_year
        Workout workout1 = new Workout("12/1/2018",12.2,100.0,12.0, 1.1,2.1,3.1);

        // current week_of_year
        Workout workout2 = new Workout("12/2/2018",1.3,10.0,11.0, 4.0,5.1,1.1);
        Workout workout3 = new Workout("12/4/2018",1.3,10.0,11.0, 4.0,5.1,1.1);
        Workout workout4 = new Workout("12/6/2018",1.3,10.0,11.0, 4.0,5.1,1.1);

        // another week_of_year
        Workout workout5 = new Workout("12/9/2018",1.3,10.0,11.0, 4.0,5.1,1.1);

// db should have the mock data
        db.addWorkout(workout1);
        db.addWorkout(workout2);
        db.addWorkout(workout3);
        db.addWorkout(workout4);
        db.addWorkout(workout5);

        Date currentDate = new Date();
        db.getWorkoutAverage();
        db.getWorkoutAllTime();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        findViewById(R.id.start_workout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button workBtn =findViewById(R.id.start_workout_button);

                if (workBtn.getText().toString().matches("Start Workout")) {
                    startService(new Intent(MapsActivity.this, MyService.class));
                    workBtn.setText("Stop Workout");
                    // start_workout_button_OnClick() goes here
                    return;
                }

                if (workBtn.getText().toString().matches("Stop Workout")) {
                    stopService(new Intent(MapsActivity.this, MyService.class));
                    workBtn.setText("Start Workout");
                    return;
                }

            }
        });

        setUp();
    }

    // the code should be inside of btn onclick of start_workout_button_Onclick
    // here is because don't need to click on the btn
    private void setUp() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);


//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        System.out.println(formatter.format(date));

        // startBtn, time start from 0, for every 5 minutes,
        // call setter to change the max and min, update the object
        // ignore if workout period is 2 days

        // get date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate = formatter.format(date); //30/11/2018

        // only get the start time, doesn't change along the real time
        String startTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());//22:26:37
        String[] strings = startTime.split(":");
        Double startSecond = Double.parseDouble(strings[0]) * 3600
                + Double.parseDouble(strings[1]) * 60
                + Double.parseDouble(strings[2]);

        Double everyFiveMinutes = 5.0 * 3600;

        // manually define the ending time
        Double endSecond = startSecond + everyFiveMinutes;


        // ??????? how to get the 5 minutes later/?????????????????????????
        if (startSecond == endSecond) {


        }

        // from Google API, get distance

        // get duration

        // after hit this btn again, times += 1

        // calculate calories according to the time and distance

        // average velocity

        // max velocity

        // min velocity

        // after all done, update workout object
        // ************************* unchecked *************************
        // mock data to checkout addWorkout in db
        Workout workout1 = new Workout("1/1/2018",12.2,100.0,12.0, 1.1,2.1,0.1);
        // db should have the mock data
//        db.addWorkout(workout1);
    }

    public void start_workout_button_Onclick(View view){

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    public void imageBtnClick(View view){
        Intent newProfielScreenActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(newProfielScreenActivity);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy((Criteria.ACCURACY_FINE));
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String locationProvider = locationManager.getBestProvider(criteria, true);


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("MYTAG", "check map!!!");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);


        String label = "Address: ";
        List<Address> addresses;

//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
//
//            if( addresses != null) {
//                Address address = addresses.get(0);
//                StringBuilder stringBuilder = new StringBuilder("");
//                for (int i=0 ; i<address.getMaxAddressLineIndex(); i++) {
//                    stringBuilder.append(address.getAddressLine(i)).append("/");
//                }
//
//                label = label + stringBuilder.toString();
//            }
//
//        }catch (IOException e) {
//            Log.i("MYTAG", "noooo ");
//        }
//
//        LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(here).title(label));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        Log.i("MYTAG", "nooooooooooooooo ");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {

    }

}
