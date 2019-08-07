package com.example.ntu_timetable_calendar.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.Dialogs.MyDatePickerDialog;
import com.example.ntu_timetable_calendar.Dialogs.MyTimePickerDialog;
import com.example.ntu_timetable_calendar.Entity.TaskEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.Helper.AlarmParser;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.SecondActivity;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.example.ntu_timetable_calendar.ViewModels.TasksFragmentViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddNewTaskFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, SecondActivity.MyOnBackPressedListener {

    // Widgets
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextInputLayout mTitleInputLayout, mDescriptionInputLayout;
    private ImageView clearClassButton;
    private TextInputEditText mTitleEditText, mDescriptionEditText;
    private TextView chosenClassTV, endDateTV, endTimeTV, addAlarmTV, addPriorityTV, addProjectTV;

    // ViewModel
    private SQLViewModel sqlViewModel;
    private TasksFragmentViewModel tasksFragmentViewModel;

    // Variables
    private int mainTimetableEntityId;
    private int PRIORITY_1, PRIORITY_2, PRIORITY_3, PRIORITY_4, NO_PRIORITY;
    private boolean[] alarmTimingChosen;

    // Variables to save
    private Integer chosenClassId;
    private Calendar deadlineCalendar; // Save the time and day chosen for task deadline by the user using the pickers
    private String title, description;
    private Integer priorityChosen;
    private List<Long> listOfAlarms = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModels();
        initVariables();
        initToolbar();
        initCurrentTimeTextViews();
    }

    private void initViews(View view) {
        mAppBarLayout = view.findViewById(R.id.add_new_task_appbarlayout);
        mToolbar = view.findViewById(R.id.add_new_task_toolbar);
        mTitleInputLayout = view.findViewById(R.id.add_new_task_title_textinputlayout);
        mTitleEditText = view.findViewById(R.id.add_new_task_title_edittext);
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(10);
        mTitleEditText.setEllipsize(TextUtils.TruncateAt.END);
        mDescriptionInputLayout = view.findViewById(R.id.add_new_task_description_textinputlayout);
        mDescriptionEditText = view.findViewById(R.id.add_new_task_description_edittext);
        mDescriptionEditText.setHorizontallyScrolling(false);
        mDescriptionEditText.setMaxLines(10);
        mDescriptionEditText.setEllipsize(TextUtils.TruncateAt.END);

        clearClassButton = view.findViewById(R.id.add_new_task_clear_class_button);
        clearClassButton.setOnClickListener(this);
        chosenClassTV = view.findViewById(R.id.add_new_task_choose_class_textview);
        chosenClassTV.setOnClickListener(this);
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

    /**
     * Initialise the Constants (The various PRIORITY variables) and the various variables from the
     * TasksFragmentViewModel for fragment re-instantiation
     */
    private void initVariables() {
        // CONSTANTS
        PRIORITY_1 = getResources().getInteger(R.integer.PRIORITY_1);
        PRIORITY_2 = getResources().getInteger(R.integer.PRIORITY_2);
        PRIORITY_3 = getResources().getInteger(R.integer.PRIORITY_3);
        PRIORITY_4 = getResources().getInteger(R.integer.PRIORITY_4);
        NO_PRIORITY = getResources().getInteger(R.integer.NO_PRIORITY);

        // Priority Level
        this.priorityChosen = tasksFragmentViewModel.getPriorityChosen();
        // Default Value
        if (this.priorityChosen == 0) {
            this.priorityChosen = this.PRIORITY_4;
        }
        // Set the priority icon's color and title
        setPriorityIconAndTitle();

        // AlarmTimingChosen boolean array
        this.alarmTimingChosen = tasksFragmentViewModel.getAlarmTimingChosen();
        // Default Value
        if (this.alarmTimingChosen == null) {
            alarmTimingChosen = new boolean[]{false, false, false, false, false};
        }
        // Set the alarm text view title
        setAddAlarmTitle();

        // DeadLine Calendar
        this.deadlineCalendar = tasksFragmentViewModel.getDeadLineCalendar();
        // Default value
        if (this.deadlineCalendar == null) {
            // Set the default deadline as 1 day after today
            deadlineCalendar = Calendar.getInstance();
            deadlineCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void initViewModels() {

        tasksFragmentViewModel = ViewModelProviders.of(requireActivity()).get(TasksFragmentViewModel.class);

        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        // Get the main timetable entity from Room
        sqlViewModel.getMainTimetable().observe(this, new Observer<TimetableEntity>() {
            @Override
            public void onChanged(TimetableEntity timetableEntity) {
                saveMainTimetable(timetableEntity);
            }
        });

           /*
          The TaskFragmentViewModel is used to share data between this fragment and ChooseClassFragment
         */
        tasksFragmentViewModel.getChosenClassId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                chosenClassId = integer == null ? -1 : integer;
                initChosenClassTextView();
            }
        });
    }

    /**
     * @param timetableEntity The main timetable from Room to be saved in the class member variable mainTimetableEntity
     */
    private void saveMainTimetable(TimetableEntity timetableEntity) {
        if (timetableEntity == null) {
            this.mainTimetableEntityId = -1;
        } else {
            this.mainTimetableEntityId = timetableEntity.getId();
        }
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
                    saveTaskDialog();
                }
                return true;
            }
        });
    }

    /**
     * Set the current date and time for the 2 text views
     */
    private void initCurrentTimeTextViews() {
        Date currentTime = deadlineCalendar.getTime();
        String currentDateStr = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        endDateTV.setText(currentDateStr.trim());

        String timeStr = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentTime);
        endTimeTV.setText(timeStr.trim());
    }

    /**
     * Set the string of the choose class text view
     */
    private void initChosenClassTextView() {
        if (chosenClassId < 0) {
            chosenClassTV.setText(getString(R.string.choose_class));
            chosenClassTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorContentText));
        } else {
            String chosenClassTitle = tasksFragmentViewModel.getChosenClassTitle();
            String chosenClassTiming = tasksFragmentViewModel.getChosenClassTiming();
            if (chosenClassTitle != null && chosenClassTiming != null) {
                String str = chosenClassTitle + " : " + chosenClassTiming;
                chosenClassTV.setText(str);
                chosenClassTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            }
        }
    }

    /**
     * Save the new task
     */
    private void saveTask() {
        if (validationCheck()) {

            // If there is no main timetable, set the class variable timetableId to -1
            int timetableId = this.mainTimetableEntityId;

            this.listOfAlarms = new AlarmParser(this.alarmTimingChosen, this.deadlineCalendar).parse();

            TaskEntity taskEntity = new TaskEntity(timetableId, chosenClassId != null ? chosenClassId : -1, title, description, this.deadlineCalendar.getTimeInMillis(),
                    this.priorityChosen, this.listOfAlarms, this.alarmTimingChosen);

            sqlViewModel.insertTask(taskEntity);

            Toasty.success(requireContext(), "Task saved", Toasty.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    /**
     * Internal validation check to see if the Task's details that the user is trying to save is valid
     *
     * @return boolean value true -- valid and proceed to save the task into Room, false -- not valid and don't save
     */
    private boolean validationCheck() {
        mTitleInputLayout.setErrorEnabled(false);
        mDescriptionInputLayout.setErrorEnabled(false);

        boolean validTitle = true;
        boolean validDescription = true;

        this.title = Objects.requireNonNull(mTitleEditText.getText()).toString().trim();
        this.description = Objects.requireNonNull(mDescriptionEditText.getText()).toString().trim();

        if (title.length() == 0) {
            mTitleInputLayout.setError(getString(R.string.title_is_empty));
            validTitle = false;
        }

        if (description.length() == 0) {
            mDescriptionInputLayout.setError(getString(R.string.description_is_empty));
            validDescription = false;
        }

        return (validTitle && validDescription);
    }

    /**
     * Helper function for setting the Priority icon's color as well as title based on the priority level chosen
     */
    private void setPriorityIconAndTitle() {
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

    /**
     * Helper function for setting the alarm text view's title based on the number of alarms chosen by user
     */
    private void setAddAlarmTitle() {
        int count = 0;
        for (boolean b : alarmTimingChosen) {
            if (b) {
                count++;
            }
        }
        if (count == 0) {
            addAlarmTV.setText(getString(R.string.add_alarm));
        } else if (count == 1) {
            String str = String.format(Locale.ENGLISH, "%d alarm", count);
            addAlarmTV.setText(str);
        } else {
            String str = String.format(Locale.ENGLISH, "%d alarms", count);
            addAlarmTV.setText(str);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Dialog shown to user when the user clicks the close button on the toolbar -- prompts user if they want to discard the task
     */
    private void closeFragmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(getString(R.string.close_new_task_dialog_message));
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Objects.requireNonNull(getActivity()).finish();
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
     * Dialog shown to user when the user clicks the save button on the toolbar -- prompts user if they want to save or continue editing
     */
    private void saveTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(getString(R.string.save_new_task_dialog_message));
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveTask();
            }
        });

        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
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
     * Show the alert dialog that allows the user to choose which priority the task is (default is PRIORITY_4)
     */
    private void initPriorityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String[] listItems = getResources().getStringArray(R.array.priority_dialog_selections);
        builder.setTitle(getString(R.string.choose_priority));
        builder.setSingleChoiceItems(listItems, priorityChosen - 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                priorityChosen = i + 1;
                // Save the priority chosen in the ViewModel
                tasksFragmentViewModel.setPriorityChosen(priorityChosen);

                setPriorityIconAndTitle();
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

    /**
     * AlertDialog for user to choose the time for the alarm/reminder for the task
     */
    private void initAlarmDialog() {
        String[] listItems = getResources().getStringArray(R.array.alarm_timing_dialog_selections);
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

                // Save the the alarmTimingChosen boolean array into the ViewModel
                tasksFragmentViewModel.setAlarmTimingChosen(alarmTimingChosen);

                setAddAlarmTitle();

                dialogInterface.dismiss();
            }
        });

        // Reset the alarm timing to all false
        builder.setNegativeButton(getString(R.string.clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alarmTimingChosen = new boolean[]{false, false, false, false, false};
                // Save the the alarmTimingChosen boolean array into the ViewModel
                tasksFragmentViewModel.setAlarmTimingChosen(alarmTimingChosen);

                setAddAlarmTitle();
            }
        });

        builder.setNeutralButton(getString(R.string.custom), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

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

    private void initNoMainTimetableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(getString(R.string.no_main_timetable_dialog_message));
        builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
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

    /**
     * Clear the ChosenClassId in the TasksFragmentViewModel
     * The LiveData observable will be triggered above and the text view will be cleared
     */
    private void clearChosenClass() {
        tasksFragmentViewModel.setChosenClassId(-1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_task_choose_class_textview:
                if (this.mainTimetableEntityId < 0) {
                    initNoMainTimetableDialog();
                } else {
                    initChooseClassFragment();
                }
                break;
            case R.id.add_new_task_clear_class_button:
                clearChosenClass();
                break;
            case R.id.add_new_task_end_date_tv:
                MyDatePickerDialog datePicker = new MyDatePickerDialog(this.deadlineCalendar);
                datePicker.show(getChildFragmentManager(), "date_picker");
                break;
            case R.id.add_new_task_end_time_tv:
                MyTimePickerDialog timePicker = new MyTimePickerDialog(this.deadlineCalendar);
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
     * Callback method for the date picker dialog
     *
     * @param datePicker dialog box
     * @param i          YEAR
     * @param i1         MONTH
     * @param i2         DAY_OF_MONTH
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        deadlineCalendar.set(Calendar.YEAR, i);
        deadlineCalendar.set(Calendar.MONTH, i1);
        deadlineCalendar.set(Calendar.DAY_OF_MONTH, i2);
        Date currentTime = deadlineCalendar.getTime();
        String currentDateStr = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        endDateTV.setText(currentDateStr.trim());

        // Save the deadline calendar into the ViewModel
        tasksFragmentViewModel.setDeadLineCalendar(this.deadlineCalendar);
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
        deadlineCalendar.set(Calendar.HOUR_OF_DAY, i);
        deadlineCalendar.set(Calendar.MINUTE, i1);
        String timeStr = DateFormat.getTimeInstance(DateFormat.SHORT).format(deadlineCalendar.getTime());
        endTimeTV.setText(timeStr.trim());

        // Save the deadline calendar into the ViewModel
        tasksFragmentViewModel.setDeadLineCalendar(this.deadlineCalendar);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Callback method for when user clicks the back button -- from SecondActivity.java
     */
    @Override
    public void myOnBackPressed() {
        closeFragmentDialog();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Display the ChooseClassFragment for user to choose a class for the task
     */
    private void initChooseClassFragment() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.second_activity_fragment_container, new ChooseClassFragment(), "home_fragment")
                .addToBackStack("home_fragment").commit();
    }

    /**
     * Attach the activity's onBackPressed listener here
     */
    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity instanceof SecondActivity) {
            ((SecondActivity) activity).setMyBackPressedListener(this);
        }
    }

    /**
     * Detach the activity's onBackPressed listener here
     */
    @Override
    public void onStop() {
        super.onStop();
        Activity activity = getActivity();
        if (activity instanceof SecondActivity) {
            ((SecondActivity) activity).setMyBackPressedListener(null);
        }
    }
}