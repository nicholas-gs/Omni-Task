package com.example.ntu_timetable_calendar.utils.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.ntu_timetable_calendar.BaseApplication;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.receivers.NotificationDoneReceiver;
import com.example.ntu_timetable_calendar.activities.SecondActivity;

/**
 * Helper class to send whatever notifications needed
 */
public abstract class NotificationHelper {

    /**
     * Send notification on Task Due Channel
     *
     * @param context        Context
     * @param message        Message to display in notification
     * @param notificationId ID of notification
     */
    public static void sendTaskDueNotification(@NonNull Context context, @NonNull String message, int notificationId, int taskId) {

        PendingIntent contentIntent = getTaskDetailPendingIntent(context, taskId, notificationId);
        PendingIntent actionIntent = getTaskDetailDonePendingIntent(context, taskId, notificationId);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, BaseApplication.TASK_DUE_CHANNEL_ID).setContentTitle(
                BaseApplication.TASK_DUE_CHANNEL_NAME
        ).setContentText(message).setColor(ContextCompat.getColor(context, R.color.colorAccent)).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_todo_icon_selected).setCategory(NotificationCompat.CATEGORY_REMINDER).setContentIntent(contentIntent).setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_check_white_24dp, context.getString(R.string.mark_as_done), actionIntent).setAutoCancel(true).build();

        notificationManager.notify(notificationId, notification);
    }

    public static void sendOverdueTaskNotification(@NonNull Context context, @NonNull String message, int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, BaseApplication.OVERDUE_TASKS_CHANNEL_ID).setContentTitle(
                BaseApplication.OVERDUE_TASKS_CHANNEL_NAME
        ).setContentText(message).setColor(ContextCompat.getColor(context, R.color.colorAccent)).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_todo_icon_selected).setCategory(NotificationCompat.CATEGORY_REMINDER).build();

        notificationManager.notify(notificationId, notification);
    }

    /**
     * @param context Context
     * @param taskId  The ID of the task that is due
     * @return PendingIntent to open the TaskDetailFragment when the user clicks the notification
     */
    private static PendingIntent getTaskDetailPendingIntent(@NonNull Context context, int taskId, int notificationId) {
        Intent activityIntent = new Intent(context, SecondActivity.class);
        activityIntent.putExtra(context.getString(R.string.EXTRA_INTENT_INFO_KEY), true);
        activityIntent.putExtra(context.getString(R.string.SECOND_ACTIVITY_INTENT_KEY), context.getString(R.string.TASK_DETAIL_INTENT));
        activityIntent.putExtra(context.getString(R.string.TASK_ENTITY_ID), taskId);

        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(context, notificationId,
                activityIntent, 0);
    }

    private static PendingIntent getTaskDetailDonePendingIntent(@NonNull Context context, int taskId, int notificationId) {
        Intent intent = new Intent(context, NotificationDoneReceiver.class);
        // The ID of the task
        intent.putExtra(context.getString(R.string.TASK_ENTITY_ID), taskId);
        // The ID of the notification
        intent.putExtra(context.getString(R.string.notification_id_key), notificationId);
        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Dismiss the notification using the notification id passed in the intent
     *
     * @param notificationId ID of the notification to dismiss
     * @param context        Application Context
     */
    public static void dismissNotification(int notificationId, @NonNull Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}