package com.specialteam.urce;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Feedback extends AppCompatActivity {

    Button button;
    DatabaseReference reff;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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
}
