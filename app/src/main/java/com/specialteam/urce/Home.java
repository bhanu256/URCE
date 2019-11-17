package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import static com.specialteam.urce.ContextAdaptor.context1;

public class Home extends AppCompatActivity{

    Intent intent;
    Bundle bd;

    List<String> ids = new ArrayList<String>();

    private DatabaseReference databaseReference;
    FirebaseAuth auth;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    FirebaseRecyclerAdapter<DataFromAdaptor,ContextHolder> adaptor;

    final int PICK_IMAGE = 1;
    final int RESULT_CODE = -1;

    NavigationView naview;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

        intent=getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(intent!=null) {
            bd = intent.getExtras();
            //tv.setText(bd.getString("name"));
        }

        String name = "bhnau";

        Datas dats = new Datas(name);

        databaseReference.child(user.getUid()).setValue(dats);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        naview = findViewById(R.id.navigation);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent2;

                switch(menuItem.getItemId()){

                    case R.id.home : drawerLayout.closeDrawer(Gravity.LEFT);
                                        return true;

                    case R.id.transport : intent2 = new Intent(Home.this,Transport.class);
                                            startActivity(intent2);
                                            return true;

                    case R.id.logout :    sharedPreferences.edit().remove("App_Login_Mail").commit();
                    sharedPreferences.edit().remove("App_Login_Pass").commit();
                        intent2 = new Intent(Home.this,Login.class);
                        startActivity(intent2);
                    break;

                }
                return false;
            }
        });

        recycleload();
    }

    public void recycleload(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Container");

        System.out.println(query);

        FirebaseRecyclerOptions<DataFromAdaptor> options = new FirebaseRecyclerOptions.Builder<DataFromAdaptor>()
                .setQuery(query,DataFromAdaptor.class)
                .build();


        adaptor = new FirebaseRecyclerAdapter<DataFromAdaptor, ContextHolder>(options) {
            @NonNull
            @Override
            public ContextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adaptorcard,parent,false);
                return new ContextHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ContextHolder contextHolder, int i, @NonNull DataFromAdaptor dataFromAdaptor) {
                contextHolder.tvname.setText(dataFromAdaptor.getCName());
                Glide.with(getApplicationContext()).load(dataFromAdaptor.getPhotoID()).placeholder(R.mipmap.ic_launcher).into(contextHolder.ivimage);
                ids.add(dataFromAdaptor.getCommentID());
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        System.out.println("qaf");
    }

    public void nav(View view){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptor.stopListening();
    }

    public void post_select(View view){
        Intent img_intent = new Intent(Intent.ACTION_PICK);
        img_intent.setType("image/*");
        img_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(img_intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_CODE && data!=null && data.getData()!=null){

        }
    }

    public void comment(String id){
        Intent intent = new Intent(Home.this,Comment.class);
        intent.putExtra("CommentId",id);
        startActivity(intent);
    }


    class ContextHolder extends RecyclerView.ViewHolder{

        TextView tvname;
        ImageView ivimage;
        TextView vac;
        public ContextHolder(@NonNull final View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.cname);
            ivimage = itemView.findViewById(R.id.img);
            vac = itemView.findViewById(R.id.vac);

            System.out.println("fwef");

            vac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ids!=null) {
                        System.out.println(ids.get(getAdapterPosition()));
                        comment(ids.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}


