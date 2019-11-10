package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);
        naview = findViewById(R.id.navigation);

        bt1 = findViewById(R.id.bus);
        bt2 = findViewById(R.id.stu);

        getLocationPermission();

        if(mail=="bus@gmail.com"){
            bt2.setVisibility(View.INVISIBLE);
        }
        else if(mail=="student@gmail.com"){
            bt1.setVisibility(View.INVISIBLE);
        }

        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;

                switch(menuItem.getItemId()){
                    case R.id.transport : intent = new Intent(Transport.this,Transport.class);
                                            return true;

                    case R.id.home : intent = new Intent(Transport.this,Home.class);
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
        startService(locationIntent);
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
