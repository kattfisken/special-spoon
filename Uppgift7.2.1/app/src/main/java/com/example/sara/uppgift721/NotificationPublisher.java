package com.example.sara.uppgift721;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A class that shows a notification.
 */
public class NotificationPublisher extends BroadcastReceiver {

    public static final String INVOCATION_START_TIME =
            "com.example.sara.uppgift721.INVOCATION_EXTRA_CODE";
    private static final String LOGTAG = MainActivity.LOGTAG + "Receiver";

    /**
     * Method that is called when a broadcast asks for a notification to be shown. If you click the
     * notification, the main activity is started again.
     *
     * @param context Context of the application.
     * @param intent  The broadcasted intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOGTAG,"Recieved a call for making notifications");

        //creates an intent for launching the app main activity
        Intent resultIntent = new Intent(context, MainActivity.class);

        /*
        The stack builder object will create an artificial back stack for the activity. This ensures
         that navigating backwards from the activity leads out of you application to the home screen
         */
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // add the back stack for the intent (but not for the intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // adds the intent that starts the activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //it is ok if we have wrong locale... I can accept that.
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        long startTimeMilliseconds = intent.getLongExtra(INVOCATION_START_TIME, 0);
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startTimeMilliseconds);
        String startTime = sdf.format(startCal.getTime());

        Calendar stopCal = Calendar.getInstance();
        String stopTime = sdf.format(stopCal.getTime());

        Notification notification =
                new NotificationCompat
                        .Builder(context)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle("Notification example")
                        .setContentText("Button press @" + startTime + ". Notify @" + stopTime)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS |
                                Notification.DEFAULT_VIBRATE)
                        .setContentIntent(resultPendingIntent)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(123456789, notification);

    }
}
