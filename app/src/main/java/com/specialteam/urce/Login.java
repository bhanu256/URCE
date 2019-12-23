package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
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

    static String name = "bhanu";

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        listener = new FirebaseAuth.AuthStateListener(){
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    //redirect

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

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Logging in.....");
        progressDialog.show();

        auth.signInWithEmailAndPassword(mail.getText().toString(),pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: sss");
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser Fuser = auth.getCurrentUser();
                            name = Fuser.getUid();

                            home = new Intent(Login.this,Form.class);
                            Log.d(TAG, name);
                            home.putExtra("name",name);
                            if(name!=null) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("App_Login_Mail",mail.getText().toString());
                                editor.putString("App_Login_Pass",pass.getText().toString());
                                editor.commit();
                                progressDialog.dismiss();
                                startActivity(home);
                            }
                        } else {
                            // If sign in fails, display a message to the user.

                            name = null;
                        }

                        // ...
                    }
                });



    }
}
