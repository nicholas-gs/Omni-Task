package com.example.ntu_timetable_calendar.Fragments;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
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
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class TaskDetailFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Widgets
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextInputLayout mTitleInputLayout, mDescriptionInputLayout;
    private TextInputEditText mTitleEditText, mDescriptionEditText;
    private Switch chooseClassSwitch;
    private TextView chooseClassTextView, endDateTV, endTimeTV, addAlarmTV, addPriorityTV, addProjectTV;

    // Variables
    private int taskEntityId;
    private int PRIORITY_1, PRIORITY_2, PRIORITY_3, PRIORITY_4, NO_PRIORITY;

    private TimetableEntity mainTimetableEntity;
    private TaskEntity taskEntity;

    private int chosenClassId; // Need to update
    private String title, description; // Need to update
    private int priorityChosen; // Need to update
    private Calendar deadlineCalendar; // Need to update
    private boolean[] alarmTimingChosen; // Need to update

    // ViewModel
    private SQLViewModel sqlViewModel;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public TaskDetailFragment(int taskEntityId) {
        this.taskEntityId = taskEntityId;
    }

    public TaskDetailFragment() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initToolbar();
        initVariables();
        initViewModels();
    }

    private void initViews(View view) {
        mAppBarLayout = view.findViewById(R.id.task_detail_appbarlayout);
        mToolbar = view.findViewById(R.id.task_detail_toolbar);
        mTitleInputLayout = view.findViewById(R.id.task_detail_title_textinputlayout);
        mTitleEditText = view.findViewById(R.id.task_detail_title_edittext);
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(10);
        mTitleEditText.setEllipsize(TextUtils.TruncateAt.END);
        mDescriptionInputLayout = view.findViewById(R.id.task_detail_description_textinputlayout);
        mDescriptionEditText = view.findViewById(R.id.task_detail_description_edittext);
        mDescriptionEditText.setHorizontallyScrolling(false);
        mDescriptionEditText.setMaxLines(10);
        mDescriptionEditText.setEllipsize(TextUtils.TruncateAt.END);

        chooseClassTextView = view.findViewById(R.id.task_detail_choose_class_textview);
        chooseClassTextView.setOnClickListener(this);
        chooseClassSwitch = view.findViewById(R.id.task_detail_choose_class_switch);
        chooseClassSwitch.setOnCheckedChangeListener(this);
        endDateTV = view.findViewById(R.id.task_detail_end_date_tv);
        endDateTV.setOnClickListener(this);
        endTimeTV = view.findViewById(R.id.task_detail_end_time_tv);
        endTimeTV.setOnClickListener(this);
        addAlarmTV = view.findViewById(R.id.task_detail_alarm_tv);
        addAlarmTV.setOnClickListener(this);
        addPriorityTV = view.findViewById(R.id.task_detail_add_priority_textview);
        addPriorityTV.setOnClickListener(this);
        addProjectTV = view.findViewById(R.id.task_detail_add_project_textview);
        addPriorityTV.setOnClickListener(this);
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

        mToolbar.inflateMenu(R.menu.task_detail_toolbar_menu);

        // Functionality for the save button clicked
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.task_detail_save) {
                    saveChangesDialog();
                } else if (item.getItemId() == R.id.task_detail_delete) {
                    deleteTaskDialog();
                }
                return true;
            }
        });
    }

    private void initVariables() {
        PRIORITY_1 = getResources().getInteger(R.integer.PRIORITY_1);
        PRIORITY_2 = getResources().getInteger(R.integer.PRIORITY_2);
        PRIORITY_3 = getResources().getInteger(R.integer.PRIORITY_3);
        PRIORITY_4 = getResources().getInteger(R.integer.PRIORITY_4);
        NO_PRIORITY = getResources().getInteger(R.integer.NO_PRIORITY);
    }

    private void initViewModels() {
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        sqlViewModel.getTask(this.taskEntityId).observe(this, new Observer<TaskEntity>() {
            @Override
            public void onChanged(TaskEntity taskEntity) {
                if (taskEntity != null) {
                    saveVariables(taskEntity);
                    displayTask();
                }
            }
        });

        sqlViewModel.getMainTimetable().observe(this, new Observer<TimetableEntity>() {
            @Override
            public void onChanged(TimetableEntity timetableEntity) {
                saveMainTimetable(timetableEntity);
            }
        });
    }

    private void saveVariables(TaskEntity taskEntity) {
        this.chosenClassId = taskEntity.getCourseEventEntityId();
        this.taskEntity = taskEntity;
        this.deadlineCalendar = Calendar.getInstance();
        this.deadlineCalendar.setTimeInMillis(taskEntity.getDeadLine());
        this.priorityChosen = this.taskEntity.getPriorityLevel();
        this.alarmTimingChosen = this.taskEntity.getAlarmTimingChosen();
    }

    private void saveMainTimetable(TimetableEntity timetableEntity) {
        this.mainTimetableEntity = timetableEntity;
    }

    private void displayTask() {
        initEditTexts();
        initClassSwitchTextView();
        initCurrentTimeTextViews();
        initAlarmTextView();
        initPriorityTextViewAndIcon();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initialise the 2 EditTexts with the TaskEntity's title and description strings
     */
    private void initEditTexts() {
        mTitleEditText.setText(this.taskEntity.getTitle());
        mDescriptionEditText.setText(this.taskEntity.getDescription());
    }

    /**
     * Initialise the ChooseClass TextView and switch with the TaskEntity's CourseEventEntityId
     */
    private void initClassSwitchTextView() {
        if (this.chosenClassId < 0) {
            chooseClassTextView.setText(getString(R.string.choose_class));
            chooseClassSwitch.setChecked(false);
        } else {
            chooseClassSwitch.setChecked(true);
        }
    }

    /**
     * Initialise the deadline date TextView and time TextView
     */
    private void initCurrentTimeTextViews() {
        Date currentTime = deadlineCalendar.getTime();
        String currentDateStr = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        endDateTV.setText(currentDateStr.trim());

        String timeStr = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentTime);
        endTimeTV.setText(timeStr.trim());
    }

    /**
     * Initialise the alarm text view's title based on the number of alarms chosen by user
     */
    private void initAlarmTextView() {
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

    /**
     * Initialise the Priority's flag icon and text view based on the TaskEntity's priority
     */
    private void initPriorityTextViewAndIcon() {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Dialog shown to user when the user clicks the close button on the toolbar -- prompts user if they want to discard changes to the task
     */
    private void closeFragmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(getString(R.string.task_detail_fragment_close_dialog_message));
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

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
     * Dialog shown to user when the user clicks the save button on the toolbar -- prompts user if they want to save changes to the task
     */
    private void saveChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(getString(R.string.save_changes_dialog_message));
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveTask();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

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
     * Dialog shown to user when the user clicks the delete button on the toolbar -- prompts user if they want to delete the task
     */
    private void deleteTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(getString(R.string.task_detail_fragment_delete_dialog_message));
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sqlViewModel.deleteTask(taskEntity);

                Toasty.success(requireContext(), getString(R.string.task_deleted), Toasty.LENGTH_SHORT).show();
                Objects.requireNonNull(getActivity()).finish();
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
        alertDialog.setCancelable(false);

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
     * Show a dialog telling the user there is no timetable if there is no main timetable to choose a class
     */
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

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                chooseClassSwitch.setChecked(false);
            }
        });

        alertDialog.show();
    }

    /**
     * Dialog shown to user when the user clicks the alarm text view -- prompt user to choose the alarms timing
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

                initAlarmTextView();

                dialogInterface.dismiss();
            }
        });

        // Reset the alarm timing to all false
        builder.setNegativeButton(getString(R.string.clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alarmTimingChosen = new boolean[]{false, false, false, false, false};

                initAlarmTextView();
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

    private void initPriorityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String[] listItems = {"Priority 1", "Priority 2", "Priority 3", "Priority 4", "No Priority"};
        builder.setTitle(getString(R.string.choose_priority));
        builder.setSingleChoiceItems(listItems, priorityChosen - 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                priorityChosen = i + 1;

                initPriorityTextViewAndIcon();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

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
     * Save updated task to Room
     */
    private void saveTask() {

        if (validationCheck()) {
            this.taskEntity.setTitle(this.title);
            this.taskEntity.setDescription(this.description);
            this.taskEntity.setPriorityLevel(this.priorityChosen);
            this.taskEntity.setDeadLine(this.deadlineCalendar.getTimeInMillis());
            this.taskEntity.setAlarmTimingChosen(this.alarmTimingChosen);

            List<Long> alarmList = new AlarmParser(this.alarmTimingChosen, this.deadlineCalendar).parse();

            this.taskEntity.setAlarmList(alarmList);

            sqlViewModel.updateTask(this.taskEntity);

            Toasty.success(requireContext(), getString(R.string.changes_saved), Toasty.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_detail_choose_class_textview:

                break;
            case R.id.task_detail_end_date_tv:
                MyDatePickerDialog datePicker = new MyDatePickerDialog(this.deadlineCalendar);
                datePicker.show(getChildFragmentManager(), "date_picker");
                break;
            case R.id.task_detail_end_time_tv:
                MyTimePickerDialog timePicker = new MyTimePickerDialog(this.deadlineCalendar);
                timePicker.show(getChildFragmentManager(), "time_picker");
                break;
            case R.id.task_detail_alarm_tv:
                initAlarmDialog();
                break;
            case R.id.task_detail_add_priority_textview:
                initPriorityDialog();
                break;
            case R.id.task_detail_add_project_textview:
                break;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the checked state of a compound button changed.
     *
     * @param compoundButton The compound button view whose state has changed.
     * @param b              The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.task_detail_choose_class_switch) {
            if (b) {
                // If the user does not have a main timetable, then show a dialog
                if (mainTimetableEntity == null) {
                    initNoMainTimetableDialog();
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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

        // Update the TextView
        initCurrentTimeTextViews();
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

        // Update the TextView
        initCurrentTimeTextViews();
    }
}