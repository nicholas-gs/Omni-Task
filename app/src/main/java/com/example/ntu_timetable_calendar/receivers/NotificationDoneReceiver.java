package com.example.ntu_timetable_calendar.receivers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ntu_timetable_calendar.utils.notifications.NotificationHelper;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.databaseservices.sqlrepository.SQLRepository;

/**
 * BroadcastReceiver for when the user clicks the "Mark as done" button in a task due notification
 */
public class NotificationDoneReceiver extends BroadcastReceiver {

    private int taskId;
    private SQLRepository sqlRepository;

    @Override
    public void onReceive(Context context, Intent intent) {

        sqlRepository = new SQLRepository((Application) context.getApplicationContext());

        taskId = intent.getIntExtra(context.getString(R.string.TASK_ENTITY_ID), -1);

        // Change TaskEntity isDone to true
        sqlRepository.completeTask(taskId);

        // Since user marks the task as done, we delete the alarm entry in the alarm_table
        sqlRepository.deleteTaskAlarms(taskId);

        // Dismiss the notification using the notification id passed in the intent
        NotificationHelper.dismissNotification(intent.getIntExtra(context.getString(R.string.notification_id_key), 0), context);
    }
}