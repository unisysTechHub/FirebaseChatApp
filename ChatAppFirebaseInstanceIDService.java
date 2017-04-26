package com.uisys.firebasechatapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Admin on 4/23/17.
 */

public class ChatAppFirebaseInstanceIDService extends FirebaseInstanceIdService {
     String TAG = "@uniSys";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.i(TAG,FirebaseInstanceId.getInstance().getToken());
    }
}
