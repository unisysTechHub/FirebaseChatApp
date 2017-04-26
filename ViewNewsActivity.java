package com.uisys.firebasechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewNewsActivity extends AppCompatActivity {
    ListView newsList;
    DatabaseReference databaseReference;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    String TAG = "@uniSys";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        newsList= (ListView) findViewById(R.id.viewNewsList);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        databaseReference = FirebaseDatabase.getInstance().getReference("/Topics/FilmNews");

        FirebaseListAdapter firebaseListAdapter =
                new FirebaseListAdapter<FilmNews>
                (this,
                 FilmNews.class,
                 R.layout.newlistrow,
                 databaseReference.limitToLast(10)) {
            @Override
            protected void populateView(View v, FilmNews filmNews, int position) {
                textView1 = (TextView) v.findViewById(R.id.textView1);
                textView2 = (TextView) v.findViewById(R.id.textView2);
                textView3= (TextView) v.findViewById(R.id.textView3);
                textView1.setText(filmNews.getNews_header());
                textView2.setText(filmNews.getNews_text());
                textView3.setText(filmNews.getImagePath());


            }
        };

        newsList.setAdapter(firebaseListAdapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                String newsHeader = ((TextView)view.findViewById(R.id.textView1)).getText().toString();
                String newsText = ((TextView)view.findViewById(R.id.textView2)).getText().toString();
                String imagePath =  ((TextView)view.findViewById(R.id.textView3)).getText().toString();

                Intent intent = new Intent(ViewNewsActivity.this,NewsDisplayActivity.class);
                intent.putExtra("newsHeader",newsHeader);
                intent.putExtra("newsText",newsText);
                intent.putExtra("imagePath",imagePath);

                startActivity(intent);






            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
