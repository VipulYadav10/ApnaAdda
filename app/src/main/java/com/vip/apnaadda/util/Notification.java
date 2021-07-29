package com.vip.apnaadda.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vip.apnaadda.R;

public class Notification {

    String title;
    String content;
    String CHANNEL_ID = "adda_notify";
    Context context;

    public Notification(Context context, String title, String content) {
        this.context = context;
        this.title = title;
        this.content = content;
    }

    public void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.noti_icon_24)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("This is a very large text that cannot fit here..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(101, builder.build());
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "apna_channel", importance);
            channel.setDescription("this is the channel for ApnaAdda app");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
