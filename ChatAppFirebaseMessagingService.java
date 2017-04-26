package com.uisys.firebasechatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.preference.RingtonePreference;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 4/23/17.
 */

public class ChatAppFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "@uniSys";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG,  remoteMessage.getNotification().getBody());
        try {
            JSONObject jsonFilmNews = new JSONObject(remoteMessage.getNotification().getBody());
                     String news_header=jsonFilmNews.getString("news_header");
                     String news_text= jsonFilmNews.getString("news_text");
                     String imagePath = jsonFilmNews.getString("imagePath");
            Log.i(TAG,"news header" +news_header);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_ic_notification)
                            .setContentTitle("Film News")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(news_header);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            Intent newsDisplayIntent = new Intent(getApplicationContext(), NewsDisplayActivity.class);
            newsDisplayIntent.putExtra("newsHeader",news_header);
            newsDisplayIntent.putExtra("newsText",news_text);
            newsDisplayIntent.putExtra("imagePath",imagePath);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(NewsDisplayActivity.class);
            stackBuilder.addNextIntent(newsDisplayIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            mNotificationManager.notify( 1,mBuilder.build());



        } catch (JSONException e) {
            Log.i(TAG,"error josn error");
            e.printStackTrace();
        }
    }
}
