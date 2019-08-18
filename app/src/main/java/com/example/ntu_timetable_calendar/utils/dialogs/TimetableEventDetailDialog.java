package com.example.ntu_timetable_calendar.utils.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ntu_timetable_calendar.models.Event;
import com.example.ntu_timetable_calendar.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class TimetableEventDetailDialog extends DialogFragment {

    // Variables
    private Event event;

    // Views
    private Dialog dialog;
    private TextView titleTV, locationTV, timeTV;
    private MaterialButton closeButton;

    public TimetableEventDetailDialog() {
    }

    public TimetableEventDetailDialog(Event event) {
        this.event = event;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.timetable_event_detail_dialog_layout, null);

        initViews(view);
        initDetails();
        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }

    private void initViews(View view) {
        titleTV = view.findViewById(R.id.event_detail_dialog_title);
        locationTV = view.findViewById(R.id.event_detail_dialog_location);
        timeTV = view.findViewById(R.id.event_detail_dialog_time);
        closeButton = view.findViewById(R.id.event_detail_dialog_close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initDetails() {
        titleTV.setText(event.getTitle());
        locationTV.setText(event.getLocation());

        Calendar start = event.getStartTime();
        Calendar end = event.getEndTime();

        String startStrHour = Integer.toString(start.get(Calendar.HOUR_OF_DAY));
        if (startStrHour.length() == 1) {
            startStrHour = "0" + startStrHour;
        }

        String startStrMin = Integer.toString(start.get(Calendar.MINUTE));

        if (startStrMin.length() == 1) {
            startStrMin = startStrMin + "0";
        }
        String startStr = startStrHour + ":" + startStrMin;

        ////////////////////////////////////////////////////////////////////////////////////////////

        String endStrHour = Integer.toString(end.get(Calendar.HOUR_OF_DAY));
        if (endStrHour.length() == 1) {
            endStrHour = "0" + endStrHour;
        }

        String endStrMin = Integer.toString(end.get(Calendar.MINUTE));

        if (endStrMin.length() == 1) {
            endStrMin = endStrMin + "0";
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        String endStr = endStrHour + ":" + endStrMin;
        String timeStr = startStr + " - " + endStr;

        timeTV.setText(timeStr);
    }

    @Override
    public void onPause() {
        super.onPause();

        /*
         * Close the dialog when onPause is called -- prevent NullPointerException for when dialog is recreated
         * after orientation change
         */
        if (this.dialog.isShowing()) {
            dismiss();
        }
    }
}
