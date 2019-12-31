package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class Home extends AppCompatActivity{

    Intent intent = null;
    Bundle bd;

     List<String> url = new ArrayList<String>();
     List<String> likes = new ArrayList<String>();
     List<String> contId = new ArrayList<>();

    private DatabaseReference databaseReference;
    FirebaseAuth auth;

    SharedPreferences sharedPreferences;
    String AppPreference = "URCE_Preference";

    FirebaseRecyclerAdapter<DataFromAdaptor,ContextHolder> adaptor;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Bundle recycleViewBundle;
    final private String saveStateKey = "resumeKey";

    NavigationView naview;
    DrawerLayout drawerLayout;

    String intent_mail = null;

    //From surya
    String date;
    String FourthYear,ThirdYear, SecondYear, FirstYear;
    String[] RollNumbers;
    List<String> RollNumbersList = new ArrayList<String>();
    DatabaseReference reff;
    int year;
    String[] Months = {"","January","February","March","April","May","June","July",
            "August","September","October","November","December"};

    SQLiteDatabase database;

    String u;
    String loc_likes;
    String loc_id;

    Button post_button;


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

        SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(this);
        String spmail = prefs.getString("App_Login_Mail",null);
        String sppass = prefs.getString("App_Login_Pass",null);

        if(user.getUid()==null){
            Intent logout = new Intent(Home.this,Login.class);
            startActivity(logout);
        }

        database = openOrCreateDatabase("URCE",MODE_PRIVATE,null);

        post_button = findViewById(R.id.post_button_id);

        if(intent!=null) {
            bd = intent.getExtras();

            intent_mail = intent.getStringExtra("mail");

            if(intent_mail!=null){
            if(intent_mail.equals("canteen@usharama.in") || intent_mail.equals("bus@usharama.in")){
                post_button.setVisibility(View.GONE);
            }}
        }

        drawerLayout = findViewById(R.id.my_drawer_layout);
        naview = findViewById(R.id.navigation);

        TextView txtProfileName = naview.getHeaderView(0).findViewById(R.id.nav_header_textView);
        String name = sharedPreferences.getString("Name",null);
        if(name!=null)
            txtProfileName.setText("Welcome "+name);

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
                        sharedPreferences.edit().remove("Name").commit();
                        sharedPreferences.edit().remove("Number").commit();
                        sharedPreferences.edit().remove("Dep").commit();
                        sharedPreferences.edit().remove("Year").commit();
                        sharedPreferences.edit().remove("DOB").commit();
                        FirebaseAuth.getInstance().signOut();
                        intent2 = new Intent(Home.this,Login.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent2);
                        finish();
                    break;

                    case R.id.canteen :
                        String mail = sharedPreferences.getString("App_Login_Mail",null);
                        if(mail.equals("canteen@usharama.in")){
                            intent2 = new Intent(Home.this,CanteenAdmin.class);
                            startActivity(intent2);
                        }
                        else{
                            intent2 = new Intent(Home.this,CanteenStudent.class);
                            startActivity(intent2);
                        }
                        break;

                    case R.id.feedback :
                        intent2 = new Intent(Home.this,Feedback.class);
                        startActivity(intent2);
                        break;

                    case R.id.clubs :
                        intent2 = new Intent(Home.this,ClubsMenu.class);
                        startActivity(intent2);
                        break;

                    case R.id.departments :
                        intent2 = new Intent(Home.this,DepartmentsMenu.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        //Admin panel starts
        int mo = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String PresentMonth = Months[mo];

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        System.out.println(day);

        reff = FirebaseDatabase.getInstance().getReference().child("BirthDates").child(PresentMonth).child(""+day);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (int i = 1; i <= 4; i++) {
                    if (dataSnapshot.child("" + i).getValue() != null) {
                        TextView nameView = (TextView) findViewById(R.id.BirthdayNamesTextView);
                        nameView.setText("");
                        String RollNumberString = dataSnapshot.child("" + i).getValue().toString();
                        //System.out.println(RollNumberString);
                        System.out.println("Year: " + i);
                        LinearLayout birthdayCard = (LinearLayout) findViewById(R.id.BirthdayCardLayout);
                        birthdayCard.setVisibility(View.VISIBLE);
                        System.out.println(RollNumberString);
                        //createBirthdayWish(i, RollNumberString);
                        createBirthdayWish(i, RollNumberString);
                    } else {
                        //LinearLayout birthdayCard = (LinearLayout) findViewById(R.id.BirthdayCardLayout);
                        //birthdayCard.setVisibility(View.GONE);
                        System.out.println("Year: " + i + ":: NO Birthdays Today");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public DataFromAdaptor getItem(int p){
                return super.getItem(getItemCount() - (p+1));
            }

            @Override
            protected void onBindViewHolder(@NonNull ContextHolder contextHolder, int i, @NonNull DataFromAdaptor dataFromAdaptor) {
                u = dataFromAdaptor.getPhotoID();
                loc_likes = dataFromAdaptor.getLiked();
                loc_id = dataFromAdaptor.getID();

                contextHolder.tvname.setText(dataFromAdaptor.getCName());
                Glide.with(getApplicationContext())
                        .load(u)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(contextHolder.ivimage);
                contextHolder.commented.setText(dataFromAdaptor.getDesc());

                if(!url.contains(u))
                    url.add(u);
                if(!contId.contains(loc_id))
                    contId.add(loc_id);
                likes.add(loc_likes);

                String s = "select post from CommentedPost where id='"+ loc_id+"'";

                Cursor resultSet = database.rawQuery(s,null);
                if(resultSet.moveToFirst()){
                    System.out.println("yyy");
                    String liked = resultSet.getString(0);
                    //Boolean loved = resultSet.getString(0)=="loved";
                    switch (liked){
                        case "liked" : contextHolder.like.setImageResource(R.drawable.ic_flame_linked);
                            System.out.println("l");
                            break;

                        case "loved" : contextHolder.heart.setImageResource(R.drawable.ic_favorite_linked);
                            System.out.println("h");
                            break;
                    }
                }

                int count = Integer.parseInt(loc_likes);
                if(count!=0){
                    contextHolder.com_count.setText(count+"likes");
                }
            }
        };

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Home.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adaptor);
    }

    private void createBirthdayWish(int i, String rollNumberString) {
        RollNumbers = rollNumberString.split(",");
        RollNumbersList = Arrays.asList(RollNumbers);
        year = 0;
        year = i;
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("StudentData").child(""+year);
        for (final String roll:RollNumbersList)
        {
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int yy = year;
                    String name = dataSnapshot.child(roll).child("Name").getValue().toString();
                    String branch = dataSnapshot.child(roll).child("Branch").getValue().toString();
                    TextView nameView = (TextView) findViewById(R.id.BirthdayNamesTextView);
                    String content = nameView.getText().toString();
                    nameView.setText(content + name+" of " + dataSnapshot.getKey() + " " + branch + "\n");
                    //System.out.println("Year is: "+ year);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

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

    @Override
    protected void onPause(){
        super.onPause();

        recycleViewBundle = new Bundle();
        Parcelable state = linearLayoutManager.onSaveInstanceState();
        recycleViewBundle.putParcelable(saveStateKey,state);
        System.out.println("saved");
    }

    @Override
    protected void onResume(){
        super.onResume();

        drawerLayout.closeDrawer(Gravity.LEFT);

        if(recycleViewBundle!=null){
            Parcelable state = recycleViewBundle.getParcelable(saveStateKey);
            linearLayoutManager.onRestoreInstanceState(state);
            System.out.println("resume");
        }
    }

    @Override
    public void onBackPressed(){
        onDestroy();
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void post_select(View view){
        Intent intent = new Intent(Home.this,Post_Selection.class);
        startActivity(intent);
    }

    public void comment(String id){
        Intent intent = new Intent(Home.this,Comment.class);
        intent.putExtra("CommentId",id);
        startActivity(intent);
    }


    class ContextHolder extends RecyclerView.ViewHolder{

        TextView tvname;
        ImageView ivimage;
        //TextView vac;

        ImageView download;

        ImageView like;
        ImageView heart;
        ImageView send;

        TextView com_count;
        TextView commented;

        int pos=0;
        DatabaseReference reff;

        public ContextHolder(@NonNull final View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.cname);
            ivimage = itemView.findViewById(R.id.img);
            //vac = itemView.findViewById(R.id.vac);

            like = itemView.findViewById(R.id.frame_id);
            heart = itemView.findViewById(R.id.heart);
            send = itemView.findViewById(R.id.send_id);
            download = itemView.findViewById(R.id.download_id);

            com_count = itemView.findViewById(R.id.liked_count_id);
            commented = itemView.findViewById(R.id.commented_tag_id);

            database.execSQL("CREATE TABLE IF NOT EXISTS CommentedPost(id varchar(20),post varchar(10))");

            System.out.println(contId);

            reff = FirebaseDatabase.getInstance().getReference();

            //Uncommment for comments
            /*vac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(url!=null) {
                        comment(url.get(getAdapterPosition()));
                    }
                }
            });*/

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    String webUrl = url.get(pos);
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(webUrl)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/URCE/Saved");
                                    if(!dir.exists())
                                        dir.mkdir();
                                    File imageFile = new File(dir,"URCE"+pos+".png");
                                    try {
                                        if (!imageFile.exists())
                                            imageFile.createNewFile();
                                        OutputStream fout = new FileOutputStream(imageFile);
                                        resource.compress(Bitmap.CompressFormat.PNG, 100, fout);
                                        fout.flush();
                                        fout.close();
                                        Toast.makeText(getApplicationContext(),"Image Downloaded",Toast.LENGTH_SHORT);
                                        //MediaStore.Images.Media.insertImage(getContentResolver(),resource,"Image"+pos,"Saved from URCE");
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
            });

            ivimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    String webUrl = url.get(pos);
                    System.out.println(pos);
                    System.out.println("dddd");
                    System.out.println(contId);
                    System.out.println(webUrl);
                    Intent intent = new Intent(getBaseContext(),MaxPost.class);
                    intent.putExtra("URL",webUrl);
                    startActivity(intent);
                }
            });


            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setImageResource(R.drawable.ic_flame_linked);
                    heart.setImageResource(R.drawable.ic_favorite_border_comment);
                    pos = getAdapterPosition();
                    Cursor resultSet = database.rawQuery("select post from CommentedPost where id='"+ contId.get(pos)+"'",null);
                    if(resultSet.moveToFirst()){
                        database.execSQL("update CommentedPost set post = 'liked' where id ='"+contId.get(pos)+"'");
                        System.out.println("like");
                    }
                    else{
                        database.execSQL("insert into CommentedPost values('"+contId.get(pos)+"','liked')");
                        System.out.println("first like");
                        int cot = Integer.parseInt(likes.get(pos))+1;
                        reff.child("Container").child(contId.get(pos)).child("Liked").setValue(cot+"");
                        com_count.setText(cot+" likes");
                    }
                }
            });

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    heart.setImageResource(R.drawable.ic_favorite_linked);
                    like.setImageResource(R.drawable.ic_flame_default);
                    pos = getAdapterPosition();
                    String s = "select post from CommentedPost where id='"+ contId.get(pos)+"'";
                    System.out.println(s);
                    Cursor resultSet = database.rawQuery(s,null);
                    if(resultSet.moveToFirst()){
                        database.execSQL("update CommentedPost set post = 'loved' where id ='"+contId.get(pos)+"'");
                        System.out.println("love");
                    }
                    else{
                        database.execSQL("insert into CommentedPost values('"+contId.get(pos)+"','loved')");
                        System.out.println("first love");
                        System.out.println(likes);
                        int cot = Integer.parseInt(likes.get(pos))+1;
                        reff.child("Container").child(contId.get(pos)).child("Liked").setValue(cot+"");
                        com_count.setText(cot+" likes");
                    }
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String webUrl = url.get(getAdapterPosition());
                    Glide.with(getBaseContext())
                            .asBitmap()
                            .load(webUrl)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/URCE/Share");
                                    if(!dir.exists())
                                        dir.mkdir();
                                    File imageFile = new File(dir,"sharing.png");
                                    try{
                                        if(!imageFile.exists())
                                            imageFile.createNewFile();
                                        OutputStream fout = new FileOutputStream(imageFile);
                                        resource.compress(Bitmap.CompressFormat.PNG,100,fout);
                                        fout.flush();
                                        fout.close();
                                        Intent share = new Intent(Intent.ACTION_SEND);
                                        share.setType("image/*");
                                        share.putExtra(Intent.EXTRA_STREAM,Uri.parse(imageFile.getAbsolutePath()));
                                        startActivity(Intent.createChooser(share,"Share via"));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
            });
        }
    }
}


