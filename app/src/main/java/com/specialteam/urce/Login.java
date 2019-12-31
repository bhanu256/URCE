package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = "TAG";
    private Intent home;
    public FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private EditText mail;
    private EditText pass;

    TextView name_choose;
    Spinner listDisplay;

    static String name = "bhanu";

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    TextView loading;

    String[] clubs = {"Literacy club","E club","NSS"};
    String[] buses = {"0542","1033","1034","1489","1713","1716","2434","2435","2436","3002","4842","4843","4844","5171","5483","8533","8788","8918"};

    ArrayAdapter a_clubs;
    ArrayAdapter a_buses;

    String shmail;
    String chosen;

    Boolean cl = false,bu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        shmail = prefs.getString("App_Login_Mail",null);

        name_choose = findViewById(R.id.by_name_choose_id);
        listDisplay = findViewById(R.id.spinner_login_id);

        a_clubs = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,clubs);
        a_clubs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        a_buses = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,buses);
        a_buses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals("club@usharama.in")){
                    listDisplay.setAdapter(a_clubs);
                    name_choose.setVisibility(View.VISIBLE);
                    listDisplay.setVisibility(View.VISIBLE);
                    cl = true;
                    bu = false;
                }
                else if(s.toString().equals("bus@usharama.in")){
                    listDisplay.setAdapter(a_buses);
                    name_choose.setVisibility(View.VISIBLE);
                    listDisplay.setVisibility(View.VISIBLE);
                    bu = true;
                    cl = false;
                }
                else{
                    name_choose.setVisibility(View.INVISIBLE);
                    listDisplay.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(cl) {
                    chosen = clubs[position];
                    System.out.println(chosen);
                }
                if(bu) {
                    chosen = buses[position];
                    System.out.println(chosen);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listener = new FirebaseAuth.AuthStateListener(){
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null && shmail!=null) {
                    Intent intent = new Intent(Login.this,Home.class);
                    intent.putExtra("mail",shmail);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        auth.addAuthStateListener(listener);
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    public void Next(View view){

        /*final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Logging in.....");
        progressDialog.show();*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progressing,null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        auth.signInWithEmailAndPassword(mail.getText().toString(),pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: sss");
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String mail_s = mail.getText().toString();
                            String pass_s = pass.getText().toString();

                            if(name!=null) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("App_Login_Mail",mail_s);
                                editor.putString("App_Login_Pass",pass_s);
                                editor.commit();
                                dialog.dismiss();
                                if(mail_s.equals("administrative@usharama.in") ||
                                    mail_s.equals("hod@usharama.in") ||
                                    mail_s.equals("faculty@usharama.in")){
                                    home = new Intent(Login.this,Form_teachers.class);
                                    home.putExtra("mail",mail_s);
                                    startActivity(home);
                                }
                                else if(mail_s.equals("club@usharama.in") || mail_s.equals("bus@usharama.in")){
                                    if(chosen!=null){
                                        home = new Intent(Login.this,Home.class);
                                        if(cl){
                                            editor.putString("Name",chosen);
                                        }
                                        else if(bu){
                                            editor.putString("Name","bus");
                                            editor.putString("Bus",chosen);
                                        }
                                        editor.commit();
                                        home.putExtra("mail",mail_s);
                                        dialog.dismiss();
                                        startActivity(home);
                                    }
                                    else{
                                        dialog.dismiss();
                                        TextView f = findViewById(R.id.fl);
                                        f.setVisibility(View.VISIBLE);
                                    }
                                }
                                else if(mail_s.equals("canteen@usharama.in")){
                                    home = new Intent(Login.this,Home.class);
                                    home.putExtra("mail",mail_s);
                                    editor.putString("Name","canteenAdmin");
                                    editor.commit();
                                    dialog.dismiss();
                                    startActivity(home);
                                }
                                else if(mail_s.equals("student@usharama.in")){
                                    home = new Intent(Login.this,Form.class);
                                    dialog.dismiss();
                                    startActivity(home);
                                }
                                else{
                                    home = new Intent(Login.this,Home.class);
                                    editor.putString("Name","AdminOfApp");
                                    editor.commit();
                                    dialog.dismiss();
                                    startActivity(home);
                                }

                            }
                        } else {
                            // If sign in fails, display a message to the user.

                            name = null;
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener(){

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        TextView f = findViewById(R.id.fl);
                        f.setVisibility(View.VISIBLE);
                    }
                });



    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
