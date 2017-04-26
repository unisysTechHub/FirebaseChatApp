package com.uisys.firebasechatapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 3/22/17.
 */

class ChatArrayAdapter extends ArrayAdapter<ArrayList>
{
     Context context;
     ArrayList userList;
     TextView textView1;
     TextView textView2;
     TextView textView3;
    public ChatArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList userList) {
        super(context, resource, userList);
        this.context=context;
        this.userList=userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null )
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.userlistrow,null);

        }

        textView1 = (TextView) convertView.findViewById(R.id.userName);
        textView2=  (TextView) convertView.findViewById(R.id.userEmailId);
        textView3= (TextView) convertView.findViewById(R.id.uid);


        textView1.setTextColor(Color.BLUE);
        textView1.setText(((BktappUserInfo)userList.get(position)).getName());
        textView2.setText(((BktappUserInfo)userList.get(position)).getEmailId());
        textView3.setText(((BktappUserInfo)userList.get(position)).getUid());
        return convertView;


    }
}