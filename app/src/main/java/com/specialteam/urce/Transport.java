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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.security.AccessController.getContext;

public class Transport extends AppCompatActivity{

    private NavigationView naview;
    DrawerLayout drawerLayout;

    private Button bt1;
    private Button bt2;
    Spinner bus_select;
    TextView bus_status;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    private boolean mLocationPermissionGranted = false;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;

    Intent locationIntent;

    String[] buses = {"0542","1033","1034","1489","1713","1716","2434","2435","2436","3002","4842","4843","4844","5171","5483","8533","8788","8918"};

    ArrayAdapter a_buses;

    Spinner bus_sel;

    String selected_bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        String mail = sharedPreferences.getString("App_Login_Mail",null);

        bus_sel = findViewById(R.id.bus_spinner);

        a_buses = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,buses);
        a_buses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bus_sel.setAdapter(a_buses);

        bus_sel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_bus = buses[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        System.out.println(mail);

        bus_select = findViewById(R.id.bus_select_id);
        bus_status = findViewById(R.id.bus_status_id);

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

        if(mail=="bus@usharama.in"){
            bt1.setVisibility(View.INVISIBLE);
            bus_status.setVisibility(View.INVISIBLE);
            bus_sel.setVisibility(View.INVISIBLE);
        }
        else{
            bt2.setVisibility(View.INVISIBLE);
            bus_select.setVisibility(View.INVISIBLE);
        }

        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;

                switch(menuItem.getItemId()){
                    case R.id.transport :
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;

                    case R.id.home :
                        intent = new Intent(Transport.this,Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        return true;

                    case R.id.logout :
                        sharedPreferences.edit().remove("App_Login_Mail").commit();
                        sharedPreferences.edit().remove("App_Login_Pass").commit();
                        sharedPreferences.edit().remove("Name").commit();
                        sharedPreferences.edit().remove("Number").commit();
                        sharedPreferences.edit().remove("Dep").commit();
                        sharedPreferences.edit().remove("Year").commit();
                        sharedPreferences.edit().remove("DOB").commit();
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(Transport.this,Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.canteen :
                        String mail = sharedPreferences.getString("App_Login_Mail",null);
                        if(mail.equals("canteen@usharama.in")){
                            intent = new Intent(Transport.this,CanteenAdmin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            intent = new Intent(Transport.this,CanteenStudent.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        break;

                    case R.id.feedback :
                        intent = new Intent(Transport.this,Feedback.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.clubs :
                        intent = new Intent(Transport.this,ClubsMenu.class);
                        startActivity(intent);
                        break;

                    case R.id.departments :
                        intent = new Intent(Transport.this,DepartmentsMenu.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }


    public void stud(View view){
        if(selected_bus!=null){
            Intent intent = new Intent(this,MapsActivity.class);
            intent.putExtra("Bus",selected_bus);
            startActivity(intent);
        }
    }

    public void bus(View view){
        locationIntent = new Intent(this,GetLocationSet.class);


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

    public void stopped(View view){
        new GetLocationSet().stop();
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(Transport.this,Home.class);
        startActivity(intent);
    }
}
