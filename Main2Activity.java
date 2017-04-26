package com.uisys.firebasechatapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {
    ListView listView;
    EditText messageET;
    Button  sendBtn;
    String TAG="@uniSys";
    TextView textView1;
    TextView textView2;
    FirebaseListAdapter<ChatMessage> firebaseListAdapter;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView= (ListView) findViewById(R.id.listView);
        messageET= (EditText) findViewById(R.id.sendMessage);
        sendBtn= (Button) findViewById(R.id.sendMessageButton);
        String uid=getIntent().getStringExtra("uid");
        final String userName =getIntent().getStringExtra("userName");
        Log.i(TAG,uid);
        databaseReference = FirebaseDatabase.getInstance().
                getReference("/ChatApp/Messages" + "/" +
                        uid);
        chatFirebaseListAdatper();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText   =messageET.getText().toString();
                ChatMessage message = new ChatMessage(userName,messageText);
                databaseReference.push().setValue(message);


            }
        });


    }
    void chatFirebaseListAdatper()

    {

        firebaseListAdapter = new FirebaseListAdapter<ChatMessage>
                (this,
                        ChatMessage.class,android.R.layout.simple_list_item_2,
                        databaseReference.limitToLast(5))
        {
            @Override
            protected void populateView(View v, ChatMessage chatMessage, int position) {
                textView1 = (TextView) v.findViewById(android.R.id.text1);
                textView2=  (TextView) v.findViewById(android.R.id.text2);

                textView1.setTextColor(Color.BLUE);
                textView1.setText(chatMessage.getName());
                textView2.setText(chatMessage.getMessage());

            }
        };

        listView.setAdapter(firebaseListAdapter);



    }

}
