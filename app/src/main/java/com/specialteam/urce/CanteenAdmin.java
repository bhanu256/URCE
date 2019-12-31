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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CanteenAdmin extends AppCompatActivity {



    String morningItems = "", afternoonItems = "", eveningItems ="";
    String date;
    DatabaseReference reff;

    NavigationView naview;
    DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_admin);

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
                        intent2 = new Intent(CanteenAdmin.this,Home.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        return true;

                    case R.id.transport :
                        intent2 = new Intent(CanteenAdmin.this,Transport.class);
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
                        intent2 = new Intent(CanteenAdmin.this,Login.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.canteen :
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.feedback :
                        intent2 = new Intent(CanteenAdmin.this,Feedback.class);
                        startActivity(intent2);
                        break;

                    case R.id.clubs :
                        intent2 = new Intent(CanteenAdmin.this,ClubsMenu.class);
                        startActivity(intent2);
                        break;

                    case R.id.departments :
                        intent2 = new Intent(CanteenAdmin.this,DepartmentsMenu.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }

    public void addMorningItems(View view) {
        TextView morning = (TextView) findViewById(R.id.morningItemsView);
        EditText mItemsEdit = (EditText) findViewById(R.id.morningItemsEditText);
        String mItems = mItemsEdit.getText().toString();

        morningItems = morningItems + mItems + ",";
        morning.setText("Morning Items: " + morningItems.toString());
        mItemsEdit.setText("");

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        HashMap<String,Object> result = new HashMap<>();
        result.put("Morning",morningItems);
        result.put("rating",0);
        result.put("rateCount",0);
        reff.updateChildren(result);

        Toast.makeText(this, "Morning Item Added", Toast.LENGTH_SHORT).show();
    }

    public void deleteMorningItems(View view) {
        TextView morning = (TextView) findViewById(R.id.morningItemsView);
        EditText mItemsEdit = (EditText) findViewById(R.id.morningItemsEditText);
        morningItems = "";
        morning.setText("Morning Items: " + morningItems);
        mItemsEdit.setText("");

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        HashMap<String,Object> result = new HashMap<>();
        result.put("Morning",morningItems);
        reff.updateChildren(result);
        Toast.makeText(this, "Morning Items Cleared", Toast.LENGTH_SHORT).show();

    }

    public void addAfternoonItems(View view) {
        TextView afternoon = (TextView) findViewById(R.id.afternoonItemsView);
        EditText mItemsEdit = (EditText) findViewById(R.id.afternoonItemsEditText);
        String mItems = mItemsEdit.getText().toString();

        afternoonItems = afternoonItems + mItems + ",";
        afternoon.setText("Afternoon Items: " + afternoonItems.toString());
        mItemsEdit.setText("");

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        HashMap<String,Object> result = new HashMap<>();
        result.put("Afternoon",afternoonItems);
        reff.updateChildren(result);

        Toast.makeText(this, "Afternoon Item Added", Toast.LENGTH_SHORT).show();

    }

    public void deleteAfternoonItems(View view) {

        TextView afternoon = (TextView) findViewById(R.id.afternoonItemsView);
        EditText mItemsEdit = (EditText) findViewById(R.id.afternoonItemsEditText);
        afternoonItems = "";
        afternoon.setText("Afternoon Items: " + afternoonItems);
        mItemsEdit.setText("");

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        HashMap<String,Object> result = new HashMap<>();
        result.put("Afternoon",afternoonItems);
        reff.updateChildren(result);
        Toast.makeText(this, "Afternoon Items Cleared", Toast.LENGTH_SHORT).show();

    }

    public void addEveningItems(View view) {

        TextView evening = (TextView) findViewById(R.id.eveningItemsView);
        EditText mItemsEdit = (EditText) findViewById(R.id.eveningItemsEditText);
        String mItems = mItemsEdit.getText().toString();

        eveningItems = eveningItems + mItems + ",";
        evening.setText("Evening Items: " + eveningItems.toString());
        mItemsEdit.setText("");

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        HashMap<String,Object> result = new HashMap<>();
        result.put("Evening",eveningItems);
        reff.updateChildren(result);

        Toast.makeText(this, "Evening Item Added", Toast.LENGTH_SHORT).show();

    }

    public void deleteEveningItems(View view) {

        TextView evening = (TextView) findViewById(R.id.eveningItemsView);
        EditText mItemsEdit = (EditText) findViewById(R.id.eveningItemsEditText);
        eveningItems = "";
        evening.setText("Evening Items: " + eveningItems);
        mItemsEdit.setText("");

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());
        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        HashMap<String,Object> result = new HashMap<>();
        result.put("Evening",eveningItems);
        reff.updateChildren(result);
        Toast.makeText(this, "Evening Items Cleared", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(CanteenAdmin.this,Home.class);
        startActivity(intent);
    }
}
