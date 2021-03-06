package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Feedback extends AppCompatActivity {

    Button button;
    DatabaseReference reff;
    AlertDialog.Builder builder;

    NavigationView naview;
    DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


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
                        intent2 = new Intent(Feedback.this,Home.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        return true;

                    case R.id.transport :
                        intent2 = new Intent(Feedback.this,Transport.class);
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
                        intent2 = new Intent(Feedback.this,Login.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.canteen :
                        String mail = sharedPreferences.getString("App_Login_Mail",null);
                        if(mail.equals("canteen@usharama.in")){
                            intent2 = new Intent(Feedback.this,CanteenAdmin.class);
                            startActivity(intent2);
                        }
                        else{
                            intent2 = new Intent(Feedback.this,CanteenStudent.class);
                            startActivity(intent2);
                        }
                        break;

                    case R.id.feedback :
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.clubs :
                        intent2 = new Intent(Feedback.this,ClubsMenu.class);
                        startActivity(intent2);
                        break;

                    case R.id.departments :
                        intent2 = new Intent(Feedback.this,DepartmentsMenu.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }

    public void FeedbackSubmit(View view) {
        long Milli;
        Milli = System.currentTimeMillis();
        EditText FeedbackTextEdit = (EditText) findViewById(R.id.FeedbackEditView);
        String FeedbackString = FeedbackTextEdit.getText().toString();
        if (FeedbackString.isEmpty() || FeedbackString.matches("\n*") || FeedbackString.equals(".") || FeedbackString.equals(","))
        {
            FeedbackTextEdit.setText("");
            FeedbackTextEdit.setHint("Cannot send empty input");
            FeedbackTextEdit.setHintTextColor(getResources().getColor(R.color.colorRed));
        }
        else {

            reff = FirebaseDatabase.getInstance().getReference().child("Feedback");
            HashMap<String, Object> result = new HashMap<>();
            result.put("" + Milli, FeedbackString);
            reff.updateChildren(result);
            builder = new AlertDialog.Builder(this);

            builder.setMessage("We respond to your request as early as possible...");
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Thank you");
            alert.show();
            FeedbackTextEdit.setText("Your feedback has been Submitted..");
            FeedbackTextEdit.setTextColor(getResources().getColor(R.color.colorBlue));
            FeedbackTextEdit.setFocusable(false);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(Feedback.this,Home.class);
        startActivity(intent);
    }
}
