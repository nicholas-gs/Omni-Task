package com.example.ntu_timetable_calendar;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String TASK_DUE_CHANNEL_ID = "task_due_notification_channel_id";
    public static final String OVERDUE_TASKS_CHANNEL_ID = "overdue_tasks_notification_channel_id";
    public static final String TASK_DUE_CHANNEL_NAME = "Task Due";
    public static final String OVERDUE_TASKS_CHANNEL_NAME = "Overdue Tasks";

    @Override
    public void onCreate() {
        super.onCreate();

        createTaskDueChannel();
        createOverdueTasksChannel();
    }

    /**
     * Create the task due notification channel for the application
     */
    private void createTaskDueChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel taskNotificationChannel = new NotificationChannel(TASK_DUE_CHANNEL_ID, TASK_DUE_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            taskNotificationChannel.setDescription("Notification channel for due tasks");
            taskNotificationChannel.enableLights(true);
            taskNotificationChannel.enableVibration(true);
            taskNotificationChannel.setLightColor(R.color.colorPrimary);
            taskNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(taskNotificationChannel);
        }
    }

    private void createOverdueTasksChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel taskNotificationChannel = new NotificationChannel(OVERDUE_TASKS_CHANNEL_ID, OVERDUE_TASKS_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            taskNotificationChannel.setDescription("Notification channel for overdue tasks");
            taskNotificationChannel.enableLights(true);
            taskNotificationChannel.enableVibration(true);
            taskNotificationChannel.setLightColor(R.color.colorPrimary);
            taskNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(taskNotificationChannel);
        }
    }




}
