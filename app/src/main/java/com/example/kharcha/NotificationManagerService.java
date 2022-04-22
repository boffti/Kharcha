package com.example.kharcha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationManagerService {

    private final String CHANNEL_ID = "karcha";
    private final String CHANNEL_NAME = "Karcha";
    private final String CHANNEL_DESCRIPTION = "Karcha System";
    private static int notificationID = 0;
    private final Context context;

    public NotificationManagerService(Context context) {
        this.context = context;
        this.createNotificationChannel();
    }

    public void sendNotification(String textTitle, String textContent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon_wallet)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setDefaults(Notification.DEFAULT_ALL)
//                .setColor(Color.rgb(255, 0, 0))
//                .setColorized(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        System.out.println("NOTIFICATION ID: " + notificationID);
        notificationManager.notify(notificationID++, builder.build());
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
