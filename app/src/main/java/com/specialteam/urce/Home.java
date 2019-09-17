package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity{

    Intent intent;
    private TextView tv ;
    Bundle bd;

    private DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        intent=getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        tv = findViewById(R.id.username);
        if(intent!=null) {
            bd = intent.getExtras();
            //tv.setText(bd.getString("name"));
        }

        String name = "bhnau";

        Datas dats = new Datas(name);

        databaseReference.child(user.getUid()).setValue(dats);

        DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView naview = findViewById(R.id.navigation);

        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.transport : Intent intent = new Intent(Home.this,Transport.class);
                                            startActivity(intent);
                                            return true;

                }
                return false;
            }
        });
    }

}
