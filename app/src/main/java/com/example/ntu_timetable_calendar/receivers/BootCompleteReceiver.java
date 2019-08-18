package com.example.ntu_timetable_calendar.receivers;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ntu_timetable_calendar.databaseservices.dao.AlarmDAO;
import com.example.ntu_timetable_calendar.converters.AlarmParser;
import com.example.ntu_timetable_calendar.databaseservices.room.SQLDatabase;

/**
 * BroadcastReceiver that is triggered when phone is turned on/restarted.
 * We schedule notifications using AlarmManager by accessing all the alarms in Room
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private SQLDatabase sqlDatabase;
    private AlarmDAO alarmDAO;

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {

                final PendingResult result = goAsync();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sqlDatabase = SQLDatabase.getInstance(context);
                        alarmDAO = sqlDatabase.alarmDAO();
                        AlarmParser.scheduleAlarmsHelper(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE), alarmDAO.getAllAlarmsList());

                        result.finish();
                    }
                }).start();
            }
        }
    }

}