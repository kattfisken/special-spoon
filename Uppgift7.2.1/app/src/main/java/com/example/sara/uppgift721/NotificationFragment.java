package com.example.sara.uppgift721;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A class for showing a fragment with a button starting a notification.
 */
public class NotificationFragment extends Fragment {

    //fragment argument corresponding to the number this section has
    private static final String ARG_SECTION_NUMBER = "section_number";
    //a name for the section
    public static String NAME = "Notify";

    /**
     * These fragments should be instantiated via factory method, not via this constructor!
     */
    public NotificationFragment() {
    }

    /**
     * Factory method for making new fragments. Class not really utilized, but this design pattern
     * is good practise since it enables further extension.
     *
     * @param sectionNumber The number this section has in the activity.
     * @return a newly created fragment
     */
    public static NotificationFragment newInstance(int sectionNumber) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new view for the fragment.
     *
     * @param inflater           Inflater to use when inflating new views.
     * @param container          The viewGroup in which the Fragment sits.
     * @param savedInstanceState Old bundle for previous instances of the fragment.
     * @return An inflated View object showing the Fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        final Button button = (Button) rootView.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.LOGTAG,"Notifierings-knappen blev tryckt!");
                createNotification();
            }
        });

        return rootView;
    }

    /**
     * Create a notification.
     */
    public void createNotification() {
        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.INVOCATION_START_TIME, System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long delay = getResources().getInteger(R.integer.notification_delay_seconds) * 1000;
        Log.d(MainActivity.LOGTAG,"Om "+delay+" millisekunder startas en notifiering");

        long futureInMilliseconds = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager =
                (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMilliseconds, pendingIntent);

    }


}
