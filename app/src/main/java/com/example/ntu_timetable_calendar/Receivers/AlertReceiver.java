package com.example.ntu_timetable_calendar.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ntu_timetable_calendar.Helper.NotificationHelper;
import com.example.ntu_timetable_calendar.R;

/**
 * BroadcastReceiver for AlarmManager when it is triggered
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Send the notification
        NotificationHelper.sendTaskDueNotification(context, intent.getStringExtra(context.getString(R.string.INTENT_MESSAGE_KEY)),
                intent.getIntExtra(context.getString(R.string.notification_id_key), -1),
                intent.getIntExtra(context.getString(R.string.TASK_ENTITY_ID), -1));
    }

}