package com.example.ntu_timetable_calendar.Helper;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.ntu_timetable_calendar.App;
import com.example.ntu_timetable_calendar.R;

/**
 * Helper class to send whatever notifications needed
 */
public abstract class NotificationHelper {

    private static final String TASK_DUE_TITLE = "Task due";

    public static void sendTaskDueNotification(Context context, String message, int notificationId) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, App.TASK_DUE_CHANNEL_ID).setContentTitle(
                TASK_DUE_TITLE
        ).setContentText(message).setColor(ContextCompat.getColor(context, R.color.colorAccent)).setPriority(NotificationCompat.PRIORITY_DEFAULT).setSmallIcon(R.drawable.ic_todo_icon_selected).setCategory(NotificationCompat.CATEGORY_REMINDER).build();

        notificationManager.notify(notificationId, notification);
    }
}
