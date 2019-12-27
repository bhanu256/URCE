package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_student);



        morning = (TextView)findViewById(R.id.morningItemsStudent);
        afternoon = (TextView)findViewById(R.id.afternoonItemsStudent);
        evening = (TextView)findViewById(R.id.eveningItemsStudent);

        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance().format(calendar.getTime());



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
}
