package com.example.ntu_timetable_calendar.utils;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ntu_timetable_calendar.activities.MainActivity;

/**
 * Initialise the BackStack (MainActivity.class) if the fragment is opened by the user clicking the notification
 */
public abstract class InitialiseBackStack {

    public static void initMainActivity(@NonNull FragmentActivity fragmentActivity, boolean launchedFromNotification) {
        if (launchedFromNotification) {
            Intent intent = new Intent(fragmentActivity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            fragmentActivity.startActivity(intent);
        }
    }
}
