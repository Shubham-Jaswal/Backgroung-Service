package com.example.service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.example.service.Utils.Utils;

public class Notification extends Application
{


    @Override
    public void onCreate() {
        super.onCreate();
        createFirstNotificationChannel();
}
    private void createFirstNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel(Utils.FIRST_CHANNEL_ID,Utils.FIRST_CHANNEL_Name,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(Utils.FIRST_CHANNEL_DEC);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


    }
}
