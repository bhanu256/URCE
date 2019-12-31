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
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ClubsMenu extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    NavigationView naview;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_menu);

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
                        intent2 = new Intent(ClubsMenu.this,Transport.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        return true;

                    case R.id.transport :
                        intent2 = new Intent(ClubsMenu.this,Transport.class);
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
                        intent2 = new Intent(ClubsMenu.this,Login.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.canteen :
                        String mail = sharedPreferences.getString("App_Login_Mail",null);
                        if(mail.equals("canteen@usharama.in")){
                            intent2 = new Intent(ClubsMenu.this,CanteenAdmin.class);
                            startActivity(intent2);
                        }
                        else{
                            intent2 = new Intent(ClubsMenu.this,CanteenStudent.class);
                            startActivity(intent2);
                        }
                        break;

                    case R.id.feedback :
                        intent2 = new Intent(ClubsMenu.this,Feedback.class);
                        startActivity(intent2);
                        break;

                    case R.id.clubs :
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.departments :
                        intent2 = new Intent(ClubsMenu.this,DepartmentsMenu.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(ClubsMenu.this,Home.class);
        startActivity(intent);
    }

    public void FilmClub(View view) {
        Intent intent = new Intent(this, ClubsPage.class);
        intent.putExtra("ClubName","Film Club");
        startActivity(intent);
    }

    public void EntreClub(View view) {
        Intent intent = new Intent(this, ClubsPage.class);
        intent.putExtra("ClubName","Entrepreneur Club");
        startActivity(intent);

    }

    public void LiteraryClub(View view) {
        Intent intent = new Intent(this, ClubsPage.class);
        intent.putExtra("ClubName","Literary Club");
        startActivity(intent);
    }

    public void SportsClub(View view) {
        Intent intent = new Intent(this, ClubsPage.class);
        intent.putExtra("ClubName","Sports Club");
        startActivity(intent);
    }

    public void PhotographyClub(View view) {
        Intent intent = new Intent(this, ClubsPage.class);
        intent.putExtra("ClubName","Photography Club");
        startActivity(intent);
    }

    public void CulturalClub(View view) {
        Intent intent = new Intent(this, ClubsPage.class);
        intent.putExtra("ClubName","Cultural Club");
        startActivity(intent);
    }
}
