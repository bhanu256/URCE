package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClubsPage extends AppCompatActivity {

    DatabaseReference reff;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    NavigationView naview;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_page);

        String ClubName = getIntent().getExtras().getString("ClubName");
        TextView clubNameView = (TextView) findViewById(R.id.ClubName);
        clubNameView.setText(ClubName);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        naview = findViewById(R.id.navigation);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent2;

                switch(menuItem.getItemId()){

                    case R.id.home :
                        intent2 = new Intent(ClubsPage.this,Transport.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        return true;

                    case R.id.transport :
                        intent2 = new Intent(ClubsPage.this,Transport.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
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
                        intent2 = new Intent(ClubsPage.this,Login.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.canteen :
                        String mail = sharedPreferences.getString("App_Login_Mail",null);
                        if(mail.equals("canteen@usharama.in")){
                            intent2 = new Intent(ClubsPage.this,CanteenAdmin.class);
                            startActivity(intent2);
                        }
                        else{
                            intent2 = new Intent(ClubsPage.this,CanteenStudent.class);
                            startActivity(intent2);
                        }
                        break;

                    case R.id.feedback :
                        intent2 = new Intent(ClubsPage.this,Feedback.class);
                        startActivity(intent2);
                        break;

                    case R.id.clubs :
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.departments :
                        intent2 = new Intent(ClubsPage.this,DepartmentsMenu.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Clubs").child(ClubName);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView missionView = (TextView) findViewById(R.id.MissionDescView);
                TextView headView = (TextView) findViewById(R.id.HeadDescView);
                TextView descView =  (TextView) findViewById(R.id.DescDescView);
                String missionDesc = dataSnapshot.child("Mission").getValue().toString();
                String headDesc = dataSnapshot.child("Head").getValue().toString();
                String desc = dataSnapshot.child("Description").getValue().toString();
                missionView.setText(missionDesc);
                headView.setText(headDesc);
                descView.setText(desc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(ClubsPage.this,ClubsMenu.class);
        startActivity(intent);
    }
}
