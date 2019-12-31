package com.specialteam.urce;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class AfterKilled extends Service {
    public AfterKilled() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onTaskRemoved(Intent intent){
        Intent locationIntent = new Intent(AfterKilled.this,GetLocationSet.class);
        System.out.println("Yes");

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            System.out.println("jjk");
            startForegroundService(locationIntent);
        }
        else{
            startService(locationIntent);
        }
        super.onTaskRemoved(intent);
    }
}
