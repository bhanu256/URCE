package com.specialteam.urce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CanteenAdmin extends AppCompatActivity {

    String morningItems = "", afternoonItems = "", eveningItems ="";
    String date;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_admin);
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
}
