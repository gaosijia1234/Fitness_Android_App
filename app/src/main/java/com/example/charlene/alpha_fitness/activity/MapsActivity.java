package com.example.charlene.alpha_fitness.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telecom.RemoteConnection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charlene.alpha_fitness.IMyAidlInterface;
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
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static final String TAG = "MapsActivity ";
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private boolean workbutton = false;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private LocationManager mLocationManager = null;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private ArrayList<Double> velocities = new ArrayList<>();
    private ArrayList<Double> calories = new ArrayList<>();
    private double totalDistance;
    private double totalCalories;
//    private Timer timer;
    private double endSecond;
    private Date date;
    private double startSecond;

    CountDownTimer countDownTimer;

    private IMyAidlInterface remoteService;
    RemoteConnection remoteConnection = null;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    class RemoteConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IMyAidlInterface.Stub.asInterface((IBinder) service);
            Toast.makeText(MapsActivity.this,
                    "Remote Service connected.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
            Toast.makeText(MapsActivity.this,
                    "Remote Service disconnected.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // initialize the service
        remoteConnection = new RemoteConnection();
        Intent intent = new Intent();
        intent.setClassName("com.example.charlene.alpha_fitness", com.example.charlene.alpha_fitness.MyService.class.getName());
        if (!bindService(intent, remoteConnection, BIND_AUTO_CREATE)) {
            Toast.makeText(MapsActivity.this, "Failed to bind the remote service, SensorService.", Toast.LENGTH_SHORT).show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Button workBtn =findViewById(R.id.start_workout_button);
            if (velocities.size()>0) {
                workBtn.setText("Stop Workout");
            }
            else  if (velocities.size() == 0){
                workBtn.setText("Start Workout");
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbind service
       unbindService(remoteConnection);
       remoteConnection = null;
    }

    public void workOutButtonOnClick(View view ) {
        Button workBtn =findViewById(R.id.start_workout_button);

        if (workBtn.getText().toString().matches("Start Workout")) {
            startService(new Intent(MapsActivity.this, MyService.class));

            workBtn.setText("Stop Workout");
            workbutton = true;
            // start_workout_button_OnClick() goes here
            try {
                setUp();
            }catch (RemoteException e){
                Log.e(TAG, "Error setUp()");
            }
            return;
        }

        if (workBtn.getText().toString().matches("Stop Workout")) {
            stopService(new Intent(MapsActivity.this, MyService.class));
            workBtn.setText("Start Workout");
            workbutton = false;
            endWorkout();
            return;
        }

    }
    public void imageBtnClick(View view){
        Intent newProfielScreenActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(newProfielScreenActivity);
    }

    private void setUp() throws android.os.RemoteException{
        // initialize the time on Map screen
//        timer = new Timer();
//        timer.schedule(new TimerTask(){ 
//            public void run() { 
//                System.out.println("Time's up!"); 
//                timer.cancel(); //Terminate the timer thread 
//                } 
//        } , 0, 1000);
        // send the timer to front-end.

        // only get the start time, doesn't change along the real time
        String startTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());//22:26:37
        String[] strings = startTime.split(":");
        startSecond = Double.parseDouble(strings[0]) * 3600
                + Double.parseDouble(strings[1]) * 60
                + Double.parseDouble(strings[2]);

        // manually define the ending time
        countDownTimer = new CountDownTimer(24 * 3600 * 1000, 1000) {
            int steps = remoteService.getCurrentWorkoutStepCount();
            //            int steps = 100;
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    int currentSteps = remoteService.getCurrentWorkoutStepCount() - steps;
                    //int currentSteps = 150;
                    double distance = currentSteps * 1.0 / 1000;
                    totalDistance += 1;
                    TextView totalDistanceView =findViewById(R.id.textView4);
                    totalDistanceView.setText(totalDistance + "");

                    double currentVelocity = distance / 5.0;
                    velocities.add(currentVelocity);

                    double calory = currentSteps / 1000 * 40;
                    calories.add(calory);
                    totalCalories += calory;
                    steps = remoteService.getCurrentWorkoutStepCount();
                    //steps = 200;
                    Log.i("totalDistance", totalDistance+"");
                    Log.i("velocity", velocities.toString());
                    Log.i("calories", calories.toString());

                } catch (Exception e){
                    Log.e(TAG, "Error occurred in SensorService while trying to get current workout distance and duration.");
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void endWorkout() {
//        timer.cancel();
        countDownTimer.cancel();

        String endTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());//22:26:37
        String[] strings = endTime.split(":");
        endSecond = Double.parseDouble(strings[0]) * 3600
                + Double.parseDouble(strings[1]) * 60
                + Double.parseDouble(strings[2]);

        double durationSecond = endSecond - startSecond;
        double aveVelocity = totalDistance / (durationSecond / 60);
        List<Double> newVelocities = new ArrayList<>(velocities);
        Collections.sort(newVelocities);
        int size = newVelocities.size();
        double maxVelocity = 0;
        double minVelocity = 0;
        if (size > 0) {
            maxVelocity = newVelocities.get(size - 1);
            minVelocity = newVelocities.get(0);
        }

        // get date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        String currentDate = formatter.format(date); //30/11/2018 
        Workout workout = new Workout(currentDate, totalDistance, totalCalories, durationSecond, aveVelocity, maxVelocity, minVelocity);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.addWorkout(workout);

        velocities = new ArrayList<>();
        calories = new ArrayList<>();
        totalCalories = 0;
        totalDistance = 0;
        startSecond = 0;
        endSecond = 0;
        date = null;
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

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

            if( addresses != null) {
                Address address = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder("");
                for (int i=0 ; i<address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append("/");
                }

                label = label + stringBuilder.toString();
            }

        }catch (IOException e) {
            Log.i("MYTAG", "noooo ");
        }

        LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(here).title(label));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
