package com.example.ntu_timetable_calendar.utils.viewformatters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.utils.constants.PriorityConstants;

import java.util.Locale;

/**
 * Format the string and icon for the priority TextView
 */
public class PriorityTextViewFormatter {

    private TextView addPriorityTV;
    private Context context;

    public PriorityTextViewFormatter(@NonNull Context context, @NonNull TextView addPriorityTV) {
        this.context = context;
        this.addPriorityTV = addPriorityTV;
    }

    public void format(int priorityChosen) {
        if (priorityChosen == PriorityConstants.getPriority1()) {
            addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_red_24dp, 0, 0, 0);
        } else if (priorityChosen == PriorityConstants.getPriority2()) {
            addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_yellow_24dp, 0, 0, 0);
        } else if (priorityChosen == PriorityConstants.getPriority3()) {
            addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_green_24dp, 0, 0, 0);
        } else if (priorityChosen == PriorityConstants.getPriority4()) {
            addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_lightblue_24dp, 0, 0, 0);
        } else if (priorityChosen == PriorityConstants.getNoPriority()) {
            addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_darkgray_24dp, 0, 0, 0);
        }

        if (priorityChosen == PriorityConstants.getNoPriority()) {
            addPriorityTV.setText(context.getText(R.string.no_priority));
        } else {
            String str = String.format(Locale.ENGLISH, "Priority %d", priorityChosen);
            addPriorityTV.setText(str);
        }
    }

}
