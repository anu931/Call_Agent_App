package com.example.call_agent;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class CallMonitorService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Notification notification = new NotificationCompact.Builder(this, "call_channel")
                .setContentTitle("Call Agent Running")
                .setContentText("Monitoring incoming calls")
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}