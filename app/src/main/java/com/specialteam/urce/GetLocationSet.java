package com.specialteam.urce;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetLocationSet extends Service {

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    public Location mLastKnownLocation = null;
    private float DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(16.578093, 80.874766);

    int i=2;

    Intent notificationIntent;
    PendingIntent pendingIntent;
    NotificationCompat.Builder builder;
    Notification notification;
    NotificationChannel channel;
    NotificationManager notificationManager;


    public void intervallocations(){
        try {

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            Log.v("TAG", locationResult.toString());
            locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        Log.v("loca",location.getLatitude()+" "+location.getLongitude());
                        databaseReference.child("Location").child("Latitude").setValue(location.getLatitude());
                        databaseReference.child("Location").child("Longitude").setValue(location.getLongitude());
                    }
                }
            });
        }
        catch (Exception e){
            Log.e("Exception","1st excep");
        }
    }

    public void locationupdates(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        Log.d("TAG","success");
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location locaation){
        Log.v("Location",locaation.getLatitude()+" "+locaation.getLongitude());
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }




    @Override
    public void onCreate(){
        super.onCreate();

        Intent intent = new Intent(GetLocationSet.this,AfterKilled.class);
        startService(intent);

        notificationIntent = new Intent(this, GetLocationSet.class);
        pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        builder = new NotificationCompat.Builder(this,"Location update")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId("12345")
                .setContentTitle("Location")
                .setContentText("Updating")
                .setContentIntent(pendingIntent);
        notification=builder.build();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            channel = new NotificationChannel("12345", "Location update", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("For updating");
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Toast toast = Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT);
        toast.show();

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();

        this.mFusedLocationProviderClient = getFusedLocationProviderClient(this);
        /*locationCallback = new LocationCallback(){
          @Override
          public void onLocationResult(LocationResult locationResult){
              super.onLocationResult(locationResult);
          }
        };

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/


        //locationupdates();

        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(i%2==0) {
                    System.out.println("jjk");
                    intervallocations();
                    startForeground(12345, notification);
                    StatusBarNotification[] notlist = notificationManager.getActiveNotifications();
                    for(int j=0;j<notlist.length;j++){
                        System.out.println(notlist[j].getId());
                    }
                }
                else {
                    System.out.println("out");
                    //stopService(new Intent(GetLocationSet.this, GetLocationSet.class));
                    stopForeground(true);
                    StatusBarNotification[] notlist = notificationManager.getActiveNotifications();
                    for(int j=0;j<notlist.length;j++){
                        System.out.println(notlist[j].getId());
                    }
                    //onDestroy();
                }
                ++i;
            }
        },0,30000);

        /*try {
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, null);
        } catch (SecurityException unlikely) {
            Log.e("EXCEPTION", "Lost location permission. Could not request updates. " + unlikely);
        }*/


    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, MyReceiver.class);
        this.sendBroadcast(broadcastIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
