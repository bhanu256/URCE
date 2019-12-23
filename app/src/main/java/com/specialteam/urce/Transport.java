package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import static java.security.AccessController.getContext;

public class Transport extends AppCompatActivity{

    private NavigationView naview;
    DrawerLayout drawerLayout;

    private Button bt1;
    private Button bt2;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    private boolean mLocationPermissionGranted = false;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        String mail = sharedPreferences.getString("App_Login_Mail",null);

        System.out.println(mail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        naview = findViewById(R.id.navigation);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        bt1 = findViewById(R.id.bus);
        bt2 = findViewById(R.id.stu);

        getLocationPermission();

        if(mail=="bus@gmail.com"){
            bt2.setVisibility(View.INVISIBLE);
        }
        else if(mail=="student@gmail.com" || mail.equals("student@gmail.com")){
            bt1.setVisibility(View.INVISIBLE);
        }

        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;

                switch(menuItem.getItemId()){
                    case R.id.transport : drawerLayout.closeDrawer(Gravity.LEFT);
                                            return true;

                    case R.id.home : intent = new Intent(Transport.this,Home.class);
                                            startActivity(intent);
                                            return true;
                }
                return false;
            }
        });
    }


    public void stud(View view){
     Intent intent = new Intent(this,MapsActivity.class);
     startActivity(intent);
    }

    public void bus(View view){
        Intent locationIntent = new Intent(this,GetLocationSet.class);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            startService(locationIntent);
        }
        else{
            startService(locationIntent);
        }
        Toast toast = Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_SHORT);
        toast.show();
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

    }
}
