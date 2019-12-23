package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class CanteenStudent extends AppCompatActivity {

    TextView morning, afternoon, evening;
    Button refresh;
    DatabaseReference reff;
    String date, morning_data, afternoon_data, evening_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_student);

        morning = (TextView) findViewById(R.id.morningItemsStudent);
        afternoon = (TextView) findViewById(R.id.afternoonItemsStudent);
        evening = (TextView) findViewById(R.id.eveningItemsStudent);

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());


        reff = FirebaseDatabase.getInstance().getReference().child("TodayFood").child(date);
        reff.addValueEventListener(new ValueEventListener() {
            @Override


            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

    }
}
