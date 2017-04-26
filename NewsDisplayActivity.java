package com.uisys.firebasechatapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NewsDisplayActivity extends AppCompatActivity {
    TextView newsHeaderT;
    TextView newsTextT;
    ImageView newsImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        newsHeaderT= (TextView) findViewById(R.id.viewNewsHeader);
        newsTextT= (TextView) findViewById(R.id.viewNewsText);
        newsImage= (ImageView) findViewById(R.id.viewNewsImage);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String newsHeader =getIntent().getStringExtra("newsHeader");
        String newsText = getIntent().getStringExtra("newsText");
        String imagePath = getIntent().getStringExtra("imagePath");
        Log.i("@uniSys", "image ref " +FirebaseStorage.getInstance().getReference().child(imagePath).toString());

        newsHeaderT.setText(newsHeader);
        newsTextT.setText(newsText);
        Glide.with(this)
             .using(new FirebaseImageLoader())
             .load(FirebaseStorage.getInstance().getReference().child(imagePath))
             .into(newsImage);


    }

}
