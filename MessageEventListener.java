package com.uisys.firebasechatapp;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Admin on 4/1/17.
 */

public class MessageEventListener implements ValueEventListener {
    Context context;
    String uid;
    String userName;
    private boolean firstTime=true;
    MessageEventListener(Context context, String uid,String userName)
    {
        this.context=context;
        this.uid =uid;
        this.userName=userName;


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(firstTime)
        {
            firstTime=false;
        }
        else
        {

            Intent intent = new Intent(context,Main2Activity.class);
            intent.putExtra("uid",dataSnapshot.getKey());
            intent.putExtra("userName",userName);
            context.startActivity(intent);

        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
