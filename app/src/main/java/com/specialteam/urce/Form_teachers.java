package com.specialteam.urce;

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

import java.util.Calendar;

public class Form_teachers extends AppCompatActivity {

    TextView dep;
    Spinner deps;
    EditText dob;
    EditText tname;
    TextView er;

    int cdate,cyear,cmonth;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    String[] departs = {"CSE","ECE","EEE","MECH","CIVIL","IT"};

    String sdep;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_teachers);

        Intent intent = getIntent();
        mail = intent.getStringExtra("mail");

        dep = findViewById(R.id.textView3_t);
        deps = findViewById(R.id.form_dep_t);
        dob = findViewById(R.id.form_dob_t);
        tname = findViewById(R.id.form_name_t);
        er = findViewById(R.id.error_t);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,departs);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deps.setAdapter(aa);

        deps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sdep = departs[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(mail.equals("administrative@usharama.in")){
            dep.setVisibility(View.GONE);
            deps.setVisibility(View.GONE);
        }
        else{
            dep.setVisibility(View.VISIBLE);
            deps.setVisibility(View.VISIBLE);
        }
    }

    public void getdate_t(View view){
        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cdate = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =  month+1;
                dob.setText(dayOfMonth+"/"+month+"/"+year);
            }
        },cyear,cmonth,cdate);

        datePickerDialog.show();
    }

    public void submit_t(View view){
        String stname = tname.getText().toString();
        String sdob = dob.getText().toString();

        if(mail.equals("hod@usharama.in") || mail.equals("faculty@usharama.in")){
            if(!stname.equals("") || !sdob.equals("") || !sdep.equals("")){
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("Name",stname);
                editor.putString("DOB",sdob);
                editor.putString("Dep",sdep);

                editor.commit();

                Intent intent = new Intent(Form_teachers.this,Home.class);
                startActivity(intent);
            }
            else{
                er.setVisibility(View.VISIBLE);
                er.setTextColor(Color.RED);
            }
        }
        else{
            if (!stname.equals("") || !sdob.equals("")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("Name", stname);
                editor.putString("DOB", sdob);

                editor.commit();

                Intent intent = new Intent(Form_teachers.this, Home.class);
                startActivity(intent);
            } else {
                er.setVisibility(View.VISIBLE);
                er.setTextColor(Color.RED);
            }
        }

    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(Form_teachers.this,Login.class);
        sharedPreferences.edit().remove("App_Login_Mail").commit();
        sharedPreferences.edit().remove("App_Login_Pass").commit();
        startActivity(intent);
    }
}
