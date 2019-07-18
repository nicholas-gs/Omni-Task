package com.example.ntu_timetable_calendar.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ntu_timetable_calendar.Dialogs.MyDatePickerDialog;
import com.example.ntu_timetable_calendar.Dialogs.MyTimePickerDialog;
import com.example.ntu_timetable_calendar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddNewTaskFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextInputLayout mInputLayout;
    private TextInputEditText mEdittext;
    private Switch chooseClassSwitch;
    private TextView endDateTV, endTimeTV, addAlarmTV, addPriorityTV, addProjectTV;

    // Variables
    private int PRIORITY_1, PRIORITY_2, PRIORITY_3, PRIORITY_4, NO_PRIORITY;

    // Variables to save
    private Calendar calendar; // Save the time and day chosen for task deadline by the user using the pickers
    private String title, description;
    private int priorityChosen;
    private boolean[] alarmTimingChosen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the default deadline as 1 day after today
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        intiVariables();
        initViews(view);
        initToolbar();
        initCurrentTimeTextViews();

    }

    private void initViews(View view) {
        mAppBarLayout = view.findViewById(R.id.add_new_task_appbarlayout);
        mToolbar = view.findViewById(R.id.add_new_task_toolbar);
        mInputLayout = view.findViewById(R.id.add_new_task_title_textinputlayout);
        mEdittext = view.findViewById(R.id.add_new_task_title_edittext);

        chooseClassSwitch = view.findViewById(R.id.add_new_task_choose_class_switch);
        endDateTV = view.findViewById(R.id.add_new_task_end_date_tv);
        endDateTV.setOnClickListener(this);
        endTimeTV = view.findViewById(R.id.add_new_task_end_time_tv);
        endTimeTV.setOnClickListener(this);
        addAlarmTV = view.findViewById(R.id.add_new_task_alarm_tv);
        addAlarmTV.setOnClickListener(this);
        addPriorityTV = view.findViewById(R.id.add_new_task_add_priority_textview);
        addPriorityTV.setOnClickListener(this);
        addProjectTV = view.findViewById(R.id.add_new_task_add_project_textview);
        addPriorityTV.setOnClickListener(this);
    }

    private void intiVariables() {
        PRIORITY_1 = getResources().getInteger(R.integer.PRIORITY_1);
        PRIORITY_2 = getResources().getInteger(R.integer.PRIORITY_2);
        PRIORITY_3 = getResources().getInteger(R.integer.PRIORITY_3);
        PRIORITY_4 = getResources().getInteger(R.integer.PRIORITY_4);
        NO_PRIORITY = getResources().getInteger(R.integer.NO_PRIORITY);
        priorityChosen = PRIORITY_4;
        alarmTimingChosen = new boolean[]{false, false, false, false, false};
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_close_white_24dp));

        // Close fragment
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragmentDialog();
            }
        });

        mToolbar.inflateMenu(R.menu.add_new_task_toolbar_menu);

        // Functionality for the save button clicked
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_new_task_save) {
                    saveTask();
                }
                return true;
            }
        });
    }

    private void closeFragmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to discard this task?");
        builder.setPositiveButton("Keep editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
            }
        });

        alertDialog.show();
    }

    /**
     * Save the new task
     */
    private void saveTask() {
        Toasty.success(requireContext(), "Task saved", Toasty.LENGTH_SHORT).show();
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    /**
     * Set the current date and time for the 2 text views
     */
    private void initCurrentTimeTextViews() {
        Date currentTime = calendar.getTime();
        String currentDateStr = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        endDateTV.setText(currentDateStr.trim());

        String timeStr = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentTime);
        endTimeTV.setText(timeStr.trim());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_task_end_date_tv:
                MyDatePickerDialog datePicker = new MyDatePickerDialog(this.calendar);
                datePicker.show(getChildFragmentManager(), "date_picker");
                break;
            case R.id.add_new_task_end_time_tv:
                MyTimePickerDialog timePicker = new MyTimePickerDialog(this.calendar);
                timePicker.show(getChildFragmentManager(), "time_picker");
                break;
            case R.id.add_new_task_alarm_tv:
                initAlarmDialog();
                break;
            case R.id.add_new_task_add_priority_textview:
                initPriorityDialog();
                break;
            case R.id.add_new_task_add_project_textview:
                break;
        }
    }

    /**
     * Show the alert dialog that allows the user to choose which priority the task is (default is PRIORITY_4)
     */
    private void initPriorityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String[] listItems = {"Priority 1", "Priority 2", "Priority 3", "Priority 4", "No Priority"};
        builder.setTitle(getString(R.string.choose_priority));
        builder.setSingleChoiceItems(listItems, priorityChosen - 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                priorityChosen = i + 1;
                if (priorityChosen == PRIORITY_1) {
                    addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_red_24dp, 0, 0, 0);
                } else if (priorityChosen == PRIORITY_2) {
                    addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_yellow_24dp, 0, 0, 0);
                } else if (priorityChosen == PRIORITY_3) {
                    addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_green_24dp, 0, 0, 0);
                } else if (priorityChosen == PRIORITY_4) {
                    addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_lightblue_24dp, 0, 0, 0);
                } else {
                    addPriorityTV.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_flag_darkgray_24dp, 0, 0, 0);
                }

                if (priorityChosen == NO_PRIORITY) {
                    addPriorityTV.setText(getText(R.string.no_priority));
                } else {
                    String str = String.format(Locale.ENGLISH, "Priority %d", priorityChosen);
                    addPriorityTV.setText(str);
                }
            }
        });

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
            }
        });

        alertDialog.show();

    }

    private static final String TAG = "AddNewTaskFragmentTAG";
    /**
     * AlertDialog for user to choose the time for the alarm/reminder for the task
     */
    private void initAlarmDialog() {
        String[] listItems = {"30 minutes before", "2 hours before", "6 hours before", "12 hours before", "1 day before"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.alarm_timing));
        builder.setMultiChoiceItems(listItems, alarmTimingChosen, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                alarmTimingChosen[i] = b;
            }
        });

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int count = 0;
                for(boolean b : alarmTimingChosen){
                    if(b){
                        count++;
                    }
                }

                String str = String.format(Locale.ENGLISH , "%d alarms", count);
                addAlarmTV.setText(str);
                dialogInterface.dismiss();
            }
        });

        // Reset the alarm timing to all false
        builder.setNegativeButton(getString(R.string.clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alarmTimingChosen = new boolean[]{false, false, false, false, false};
                addAlarmTV.setText(getString(R.string.add_alarm));
            }
        });

        builder.setNeutralButton(getString(R.string.custom), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        // Customise the appearance of the buttons
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
            }
        });

        alertDialog.show();
    }

    /**
     * Callback method for the date picker dialog
     *
     * @param datePicker dialog box
     * @param i          YEAR
     * @param i1         MONTH
     * @param i2         DAY_OF_MONTH
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        Date currentTime = calendar.getTime();
        String currentDateStr = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        endDateTV.setText(currentDateStr.trim());
        datePicker.updateDate(i, i1, i2);
    }

    /**
     * Callback method for the time picker dialog
     *
     * @param timePicker dialog box
     * @param i          hourOfDay
     * @param i1         minute
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i1);
        String timeStr = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        endTimeTV.setText(timeStr.trim());
    }

}