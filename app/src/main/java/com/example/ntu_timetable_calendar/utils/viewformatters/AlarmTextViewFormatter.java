package com.example.ntu_timetable_calendar.utils.viewformatters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

import java.util.Locale;

/**
 * Format the alarm TextView based on the number of alarms chosen
 */
public class AlarmTextViewFormatter {

    private Context context;
    private TextView alarmTextView;

    public AlarmTextViewFormatter(@NonNull Context context, @NonNull TextView alarmTextView) {
        this.context = context;
        this.alarmTextView = alarmTextView;
    }

    public void update(@NonNull boolean[] alarmTimingChosen) {
        int count = 0;

        for (boolean b : alarmTimingChosen) {
            if (b) {
                count++;
            }
        }

        if (count == 0) {
            alarmTextView.setText(context.getString(R.string.add_alarm));
        } else if (count == 1) {
            alarmTextView.setText(String.format(Locale.ENGLISH, "%d alarm", count));
        } else {
            alarmTextView.setText(String.format(Locale.ENGLISH, "%d alarms", count));
        }
    }
}
