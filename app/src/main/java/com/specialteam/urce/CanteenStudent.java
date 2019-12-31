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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CanteenStudent extends AppCompatActivity {

    TextView morning,afternoon,evening;
    Button refresh;
    DatabaseReference reff;
    int rate;
    int rateCount;
    String date,morning_data,afternoon_data,evening_data, RatingDate;

    NavigationView naview;
    DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_student);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        naview = findViewById(R.id.navigation);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

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
                        intent2 = new Intent(CanteenStudent.this,Home.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        return true;

                    case R.id.transport :
                        intent2 = new Intent(CanteenStudent.this,Transport.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        return true;

                    case R.id.logout :
                        sharedPreferences.edit().remove("App_Login_Mail").commit();
                        sharedPreferences.edit().remove("App_Login_Pass").commit();
                        sharedPreferences.edit().remove("Name").commit();
                        sharedPreferences.edit().remove("Number").commit();
                        sharedPreferences.edit().remove("Dep").commit();
                        sharedPreferences.edit().remove("Bus").commit();
                        sharedPreferences.edit().remove("Year").commit();
                        sharedPreferences.edit().remove("DOB").commit();
                        FirebaseAuth.getInstance().signOut();
                        intent2 = new Intent(CanteenStudent.this,Login.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.canteen :
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.feedback :
                        intent2 = new Intent(CanteenStudent.this,Feedback.class);
                        startActivity(intent2);
                        break;

                    case R.id.clubs :
                        intent2 = new Intent(CanteenStudent.this,ClubsMenu.class);
                        startActivity(intent2);
                        break;

                    case R.id.departments :
                        intent2 = new Intent(CanteenStudent.this,DepartmentsMenu.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });


        morning = (TextView)findViewById(R.id.morningItemsStudent);
        afternoon = (TextView)findViewById(R.id.afternoonItemsStudent);
        evening = (TextView)findViewById(R.id.eveningItemsStudent);

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());

        System.out.println(date);

        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        reff.addValueEventListener(new ValueEventListener() {
            @Override



            public void onDataChange (@NonNull DataSnapshot dataSnapshot){

                morning_data = dataSnapshot.child("Morning").getValue().toString();
                afternoon_data = dataSnapshot.child("Afternoon").getValue().toString();
                evening_data = dataSnapshot.child("Evening").getValue().toString();
                morning.setText(morning_data);
                afternoon.setText(afternoon_data);
                evening.setText(evening_data);
                Toast.makeText(CanteenStudent.this, "Data has been updated", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (RatingDate!=date)
        {
            Button button = (Button) findViewById(R.id.RateButton);
            button.setEnabled(true);
            RatingDate="";
        }




    }

    public void submitCanteenRating(View view) {
        RatingBar ratingbar;
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        int rating=Integer.valueOf((int)ratingbar.getRating());
        DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rate = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
                rateCount = Integer.parseInt(dataSnapshot.child("rateCount").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed Here:" + databaseError);

            }
        });


        HashMap<String,Object> result = new HashMap<>();
        result.put("rateCount",(rateCount+1));
        int r = rating;
        result.put("rating",(rate + r)/(rateCount+1));
        reff.updateChildren(result);
        Toast.makeText(getApplicationContext(), "Thank for your rating!!\n" + rating, Toast.LENGTH_LONG).show();
        Calendar calendar = Calendar.getInstance();
        RatingDate = DateFormat.getDateInstance().format(calendar.getTime());
        Button button = (Button) findViewById(R.id.RateButton);
        button.setEnabled(false);
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(CanteenStudent.this,Home.class);
        startActivity(intent);
    }
}
