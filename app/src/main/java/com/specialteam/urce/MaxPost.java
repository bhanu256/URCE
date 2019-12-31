package com.specialteam.urce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MaxPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_post);

        Intent getdata = getIntent();
        String url = getdata.getStringExtra("URL");
        getdata.removeExtra("URL");

        System.out.println(url);

        ImageView img = findViewById(R.id.max_post_img);

        Glide.with(getApplicationContext()).load(url).placeholder(R.mipmap.ic_launcher).into(img);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
