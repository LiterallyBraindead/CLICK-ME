package com.wguprojects.testingroomnestedrelationships;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

public class ReminderBroadcast extends BroadcastReceiver {

    TermViewModel termViewModel;
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"courseChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Course starting soon!")
                .setContentText("Your course is starting in 5 minutes!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(1, builder.build());

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context,"courseChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Course ending soon!")
                .setContentText("Your course is ending in 5 minutes!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(2, builder2.build());

        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(context,"assessmentChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Assessment starting soon!")
                .setContentText("Your assessment is starting in 5 minutes!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(context);

        notificationManager2.notify(1, builder3.build());

        NotificationCompat.Builder builder4 = new NotificationCompat.Builder(context,"assessmentChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Assessment ending soon!")
                .setContentText("Your assessment is ending in 5 minutes!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager3 = NotificationManagerCompat.from(context);

        notificationManager3.notify(2, builder4.build());
    }
}
