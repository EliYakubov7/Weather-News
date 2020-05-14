package com.example.newsweather;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;


import androidx.core.app.NotificationCompat;
import java.util.Objects;


public class NotifierService extends Service {

    public static final int NOTIFICATION_ID = 1;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int millisFreq = intent.getIntExtra("notification_freq", 0);
        String notificationCategory = intent.getStringExtra("notification_category");

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String channelId = "channel_id";
        String channelName = "Some channel";


        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(manager).createNotificationChannel(channel);
        }


        String lastTitle = MainActivity.lastTitle;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(android.R.drawable.btn_star).setContentTitle(lastTitle);


//        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
//        notificationIntent.setData(Uri.parse("http://www.google.com"));
//        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        // Resources r = getResources();
//        Notification notification = new NotificationCompat.Builder(context)
//                .setTicker("yortext")
//                .setSmallIcon(android.R.drawable.ic_menu_report_image)
//                .setContentTitle("yortext")
//                .setContentText("sdsd")
//                .setContentIntent(pi)
//                .setAutoCancel(true)
//                .build();
//
//        NotificationManager notificationManager2 =  (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
//        notificationManager2.notify(0, notification);

//        if(Objects.requireNonNull(notificationCategory).contentEquals("All")) {
//          builder.setSmallIcon(android.R.drawable.btn_star).setContentTitle("אל תפספס את חדשות ה -" + " " + notificationCategory + " " + "!");
//        }
//        else {
//          builder.setSmallIcon(android.R.drawable.btn_star).setContentTitle("אל תפספס את חדשות ה -" + " " + notificationCategory + " " + "!");
//        }

        Intent newIntent = new Intent(this,MainActivity.class);
        notificationCategory = Objects.equals(notificationCategory, "all") ? "" : "&category=" + notificationCategory;
        newIntent.putExtra("category_link", notificationCategory);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        startForeground(NOTIFICATION_ID, notification);

        if(millisFreq != 0)
        {
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntentForAlarm = PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            Objects.requireNonNull(alarmManager).set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + millisFreq,pendingIntentForAlarm);
        }

        return super.onStartCommand(intent, flags, startId);
    }


}
