package com.example.charlene.alpha_fitness;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.example.charlene.alpha_fitness.IMyAidlInterface;
import com.example.charlene.alpha_fitness.database.DatabaseHelper;

import java.util.Calendar;

public class MyService extends Service implements SensorEventListener{

    private final static String TAG = MyService.class.getSimpleName();
    public IMyAidlInterface.Stub mBinder;
    private SensorManager sensorManager;

    public MyService() {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.v(TAG, "Service onCreate() is called");
        Toast.makeText(this, "Service is created", Toast.LENGTH_LONG).show();

        DatabaseHelper db = DatabaseHelper.getInstance(this);

        // SensorManager should be SensorService
        final MyService myService = this;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mBinder = new IMyAidlInterface.Stub() {

            @Override
            public int countSteps() throws RemoteException {
                return 0;
            }

            @Override
            public int countSec() throws RemoteException {
                return 0;
            }

            @Override
            public int calcAverage() throws RemoteException {
                return 0;
            }

            @Override
            public int calcMax() throws RemoteException {
                return 0;
            }

            @Override
            public int calcMin() throws RemoteException {
                return 0;
            }

            @Override
            public void startCounting() throws RemoteException {
                PackageManager packageManager = getPackageManager();
                if (!packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)){
                    Toast.makeText(getApplicationContext(), "Count sensor not avaiable!", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "startCounting()");
                if (!isCounting()){
                    Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                    if (countSensor != null){
                        sensorManager.registerListener(myService, countSensor, SensorManager.SENSOR_DELAY_UI);
                        setIsCounting(true);

//                        // set sensor step count so that we start counting steps from zero
//                        setStepsAtReset(getSensorStepCount());
//                        Log.d(TAG, "setIsCounting(true)");
//                        long currDatetime = Calendar.getInstance().getTimeInMillis();
//                        setWorkoutStartTime(currDatetime);
//                        Log.d(TAG, "workout start time: " + getWorkoutStartTime());
//
                    } else {
                        //Toast.makeText(mySvc, "Count sensor not available!", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Count sensor not available!");
                    }
                }
            }

            public void stopCounting(){
                Log.d(TAG, "stopCounting()");

                //if you unregister the last listener, the hardware will stop detecting step events
                sensorManager.unregisterListener(myService);

                // flush the count so far
                sensorManager.flush(MyService.this);
                setIsCounting(false);
                // add current workout data to all time date in DB

            }

            @Override
            public int getCurrentWorkoutStepCount() throws RemoteException {
                return getCurrentStepCount();
            }
        };

    }

    private boolean isCounting() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("AlphaFitness", Context.MODE_PRIVATE);
        return preferences.getBoolean("IS_COUNTING", false);
    }

    private void setIsCounting(boolean isCounting) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("AlphaFitness", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IS_COUNTING", isCounting);
        editor.commit();
    }

    private int getCurrentStepCount() {
        int count = getSensorStepCount() - getStepsAtReset();
        return count;
    }

    public int getStepsAtReset() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("AlphaFitness", Context.MODE_PRIVATE);
        return preferences.getInt("STEPS_AT_RESET", 0);
    }

    public int getSensorStepCount() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("AlphaFitness", Context.MODE_PRIVATE);
        return preferences.getInt("CURRENT_STEP_COUNT", 0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    public void onDestroy(){

        Log.v(TAG, "Service onCreate() is called");
        Toast.makeText(this, "service is Stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
