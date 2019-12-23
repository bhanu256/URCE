package com.specialteam.urce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    Intent intent;
    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    String mail = null;
    String pass = null;
    String name = null;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        mail = sharedPreferences.getString("App_Login_Mail",null);
        pass = sharedPreferences.getString("App_Login_Pass",null);
        name = sharedPreferences.getString("Name",null);

        auth = FirebaseAuth.getInstance();

        if(mail!=null&&name!=null){
            Log.d("Mail",mail);
            Log.d("Pass",pass);
            auth.signInWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser Fuser = auth.getCurrentUser();

                                if(Fuser!=null){
                                    intent = new Intent(MainActivity.this,Home.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
        }
        else {
            intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}