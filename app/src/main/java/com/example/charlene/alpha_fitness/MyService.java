package com.example.charlene.alpha_fitness;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.example.charlene.alpha_fitness.IMyAidlInterface;

public class MyService extends Service {

    private final static String TAG = MyService.class.getSimpleName();
    public IMyAidlInterface.Stub mBinder;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        Log.v(TAG, "Service onCreate() is called");
        Toast.makeText(this, "Service is created", Toast.LENGTH_LONG).show();

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
            public int getCurrentWorkoutStepCount() throws RemoteException {
                return 0;
            }
        };

    }

    @Override
    public void onDestroy(){

        Log.v(TAG, "Service onCreate() is called");
        Toast.makeText(this, "service is Stopped", Toast.LENGTH_LONG).show();
    }

}
