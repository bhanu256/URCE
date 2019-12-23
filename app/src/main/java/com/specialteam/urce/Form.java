package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Form extends AppCompatActivity {

    EditText name;
    EditText num;
    Spinner dep;
    Spinner year;
    EditText dob;
    TextView error;

    int cdate,cyear,cmonth;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    String sdep = "";
    String syear = "";

    String[] departs = {"CSE","ECE","EEE","MECH","CIVIL","IT"};
    String[] years = {"1","2","3","4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        name = findViewById(R.id.form_name);
        num = findViewById(R.id.form_num);
        dep = findViewById(R.id.form_dep);
        year = findViewById(R.id.form_year);
        dob = findViewById(R.id.form_dob);
        error = findViewById(R.id.error);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,departs);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dep.setAdapter(aa);

        dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sdep = departs[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sdep = "";
            }
        });

        ArrayAdapter a2 = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,years);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(a2);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                syear = years[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                syear = "";
            }
        });

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);
    }

    public void submit(View view){
        String sname = name.getText().toString();
        String snum = num.getText().toString();
        String sdob = dob.getText().toString();

        System.out.println(sname+snum+sdep+syear+sdob);

        isPerfectNum(snum);

        if(!sname.equals("")&&!snum.equals("")&&isPerfectNum(snum.toUpperCase())&&!sdep.equals("")&&!syear.equals("")&&!sdob.equals("")) {

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("Name", sname);
            editor.putString("Number", snum);
            editor.putString("Dep", sdep);
            editor.putString("Year", syear);
            editor.putString("DOB", sdob);

            editor.commit();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("StudentData").child(syear).child(snum);

            databaseReference.child("Branch").setValue(sdep);
            databaseReference.child("DOB").setValue(sdob);
            databaseReference.child("Name").setValue(sname);

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Intent intent = new Intent(Form.this,Home.class);
                    startActivity(intent);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{
            error.setVisibility(View.VISIBLE);
            error.setTextColor(Color.RED);
        }

    }

    public void getdate(View view){

        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cdate = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dob.setText(dayOfMonth+"/"+month+"/"+year);
            }
        },cyear,cmonth,cdate);

        datePickerDialog.show();
    }

    public boolean isPerfectNum(String number){

        System.out.println(number);

        if(number.matches("1[0-9]NG1A[0-9][0-9][0-9A-Z][0-9A-Z]")) {
            System.out.println("true");
            return true;
        }
        else
            return false;
    }
}
