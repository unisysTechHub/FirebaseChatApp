package com.uisys.firebasechatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddNewsActivity extends AppCompatActivity {
        EditText newsHeader;
        EditText newsText;
        TextView imagePath;
        Button selectImage;

        int REQ_CODE = 1;
        DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        newsHeader= (EditText) findViewById(R.id.newsHeader);
        newsText = (EditText) findViewById(R.id.newsText);
        imagePath = (TextView) findViewById(R.id.imagePath);
        selectImage = (Button) findViewById(R.id.selectImage);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),REQ_CODE);

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        databaseReference = FirebaseDatabase.getInstance().getReference("/Topics/FilmNews");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageName = "news" +System.currentTimeMillis();
                final String newsImagePath = imagePath.getText().toString();

                final StorageReference newsImageRef = FirebaseStorage.getInstance().getReference().
                        child("FilmNews/" + imageName);
                final String imagePath = "FilmNews/" + imageName;
                if(newsImagePath != null)
                {
                    newsImageRef.putFile(Uri.parse(newsImagePath))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FilmNews filmNews = new FilmNews(newsHeader.getText().toString(),
                                    newsText.getText().toString(),
                                    imagePath);
                            databaseReference.push().setValue(filmNews);
                            Intent intent = new Intent(AddNewsActivity.this, ViewNewsActivity.class);
                            startActivity(intent);


                        }
                    });

                }



            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE)
        {

            if(resultCode == RESULT_OK)
            {
                Uri imageUri = data.getData();
                imagePath.setText(imageUri.toString());

            }


        }
    }
}
