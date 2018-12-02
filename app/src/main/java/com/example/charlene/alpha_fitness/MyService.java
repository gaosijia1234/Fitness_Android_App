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
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            public int square(int value) throws RemoteException {
                return value *value;
            }
        };

    }

    @Override
    public void onDestroy(){

        Log.v(TAG, "Service onCreate() is called");
        Toast.makeText(this, "service is Stopped", Toast.LENGTH_LONG).show();
    }

}
