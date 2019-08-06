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
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.TaskEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.Helper.AlarmParser;
import com.example.ntu_timetable_calendar.Helper.StringHelper;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.SecondActivity;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.example.ntu_timetable_calendar.ViewModels.TasksFragmentViewModel;
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

public class TaskDetailFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, SecondActivity.MyOnBackPressedListener {

    // Widgets
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextInputLayout mTitleInputLayout, mDescriptionInputLayout;
    private TextInputEditText mTitleEditText, mDescriptionEditText;
    private TextView chosenClassTextView, endDateTV, endTimeTV, addAlarmTV, addPriorityTV, addProjectTV;

    // Variables
    private int taskEntityId;
    private int PRIORITY_1, PRIORITY_2, PRIORITY_3, PRIORITY_4, NO_PRIORITY;

    private int mainTimetableEntityId;
    private TaskEntity taskEntity;

    private String chosenClassTitle; // For local use only
    private String chosenClassTiming; // For local use only
    private int chosenClassId; // Need to update
    private String title, description; // Need to update
    private int priorityChosen; // Need to update
    private Calendar deadlineCalendar; // Need to update
    private boolean[] alarmTimingChosen; // Need to update

    // ViewModel
    private TasksFragmentViewModel tasksFragmentViewModel;
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

        chosenClassTextView = view.findViewById(R.id.task_detail_choose_class_textview);
        chosenClassTextView.setOnClickListener(this);
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
        tasksFragmentViewModel = ViewModelProviders.of(requireActivity()).get(TasksFragmentViewModel.class);

        // If the fragment is being instantiated for the first time, we need to retrieve TaskEntity data from Room first!
        if (tasksFragmentViewModel.getTaskEntity() == null) {
            sqlViewModel.getTask(this.taskEntityId).observe(this, new Observer<TaskEntity>() {
                @Override
                public void onChanged(TaskEntity taskEntity) {
                    if (taskEntity != null) {
                        saveVariablesToViewModel(taskEntity);
                        saveLocalVariables(taskEntity);
                        retrieveCourseEventData(taskEntity);
                        displayTask();
                    }
                }
            });
        } else {
          /*  If the fragment is begin re-instantiated (orientation change), or returning to this fragment, we retrieve the data
            from TasksFragmentViewModel instead.
            */
            retrieveVariablesFromViewModel();
            updateChooseClassTextView();
            displayTask();
        }

        sqlViewModel.getMainTimetable().observe(this, new Observer<TimetableEntity>() {
            @Override
            public void onChanged(TimetableEntity timetableEntity) {
                saveMainTimetable(timetableEntity);
            }
        });
    }

    /**
     * We save the various fields in TaskEntity into the TasksFragmentViewModel the first time this
     * fragment is instantiated.
     * NOTE : The CourseEventEntity's details are not saved here. They are saved in retrieveCourseEventData()!
     *
     * @param taskEntity TaskEntity that user wants to change/see detail
     */
    private void saveVariablesToViewModel(TaskEntity taskEntity) {
        tasksFragmentViewModel.setTaskEntity(taskEntity);
        tasksFragmentViewModel.setChosenClassId(taskEntity.getCourseEventEntityId());
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(taskEntity.getDeadLine());
        tasksFragmentViewModel.setDeadLineCalendar(tempCalendar);

        tasksFragmentViewModel.setAlarmTimingChosen(taskEntity.getAlarmTimingChosen());
        tasksFragmentViewModel.setPriorityChosen(taskEntity.getPriorityLevel());
    }

    /**
     * Save variables to local variables, so that the ViewModel does not have to accessed every time
     * the user changes something. We do so only the first time the fragment is instantiated.
     * NOTE : The CourseEventEntity's details are not saved here. They are saved in retrieveCourseEventData()!
     *
     * @param taskEntity TaskEntity that user wants to change/see detail
     */
    private void saveLocalVariables(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;
        this.chosenClassId = taskEntity.getCourseEventEntityId();
        this.deadlineCalendar = Calendar.getInstance();
        this.deadlineCalendar.setTimeInMillis(taskEntity.getDeadLine());
        this.priorityChosen = this.taskEntity.getPriorityLevel();
        this.alarmTimingChosen = this.taskEntity.getAlarmTimingChosen();
    }

    private void saveMainTimetable(TimetableEntity timetableEntity) {
        if (timetableEntity == null) {
            this.mainTimetableEntityId = -1;
        } else {
            this.mainTimetableEntityId = timetableEntity.getId();
        }
    }

    private void retrieveVariablesFromViewModel() {
        this.taskEntity = tasksFragmentViewModel.getTaskEntity();
        this.deadlineCalendar = tasksFragmentViewModel.getDeadLineCalendar();
        this.priorityChosen = tasksFragmentViewModel.getPriorityChosen();
        this.alarmTimingChosen = tasksFragmentViewModel.getAlarmTimingChosen();

        tasksFragmentViewModel.getChosenClassId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                chosenClassId = integer == null ? -1 : integer;
                if (chosenClassId >= 0) {
                    chosenClassTitle = tasksFragmentViewModel.getChosenClassTitle();
                    chosenClassTiming = tasksFragmentViewModel.getChosenClassTiming();
                }
                updateChooseClassTextView();
            }
        });
    }

    /**
     * We retrieve the CourseEventEntity from Room using it's ID in the TaskEntity object
     *
     * @param taskEntity TaskEntity that user wants to edit/see details of
     */
    private void retrieveCourseEventData(TaskEntity taskEntity) {
        sqlViewModel.getCourseEvent(taskEntity.getCourseEventEntityId()).observe(this, new Observer<CourseEventEntity>() {
            @Override
            public void onChanged(CourseEventEntity courseEventEntity) {
                // Save class title to local variable and TasksFragmentViewModel
                if (courseEventEntity != null) {
                    chosenClassTitle = courseEventEntity.getTitle();
                    tasksFragmentViewModel.setChosenClassTitle(chosenClassTitle);

                    // Save class timing to local variable and TasksFragmentViewModel
                    Calendar startCal = Calendar.getInstance();
                    Calendar endCal = (Calendar) startCal.clone();

                    startCal.setTimeInMillis(courseEventEntity.getStartTime());
                    endCal.setTimeInMillis(courseEventEntity.getEndTime());

                    chosenClassTiming = StringHelper.ClassTimingParser(startCal, endCal);
                    tasksFragmentViewModel.setChosenClassTiming(chosenClassTiming);


                } else {
                    chosenClassId = -1;
                }
                updateChooseClassTextView();
            }
        });
    }

    /**
     * We display the various data here
     * NOTE : We do not call updateChooseClassTextView() here
     */
    private void displayTask() {
        initEditTexts();
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
     * Update the Chosen Class TextView when there is a change in the Chosen Class
     */
    private void updateChooseClassTextView() {
        if (chosenClassId < 0) {
            chosenClassTextView.setText(getString(R.string.choose_class));
        } else {
            if (chosenClassTitle != null && chosenClassTiming != null) {
                String str = chosenClassTitle + " : " + chosenClassTiming;
                chosenClassTextView.setText(str);
            }
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
                Objects.requireNonNull(getActivity()).finish();
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
        alertDialog.setCancelable(false);

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
     * Dialog shown to user when the user clicks the alarm text view -- prompt user to choose the alarms timing
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
                tasksFragmentViewModel.setAlarmTimingChosen(alarmTimingChosen);
                initAlarmTextView();

                dialogInterface.dismiss();
            }
        });

        // Reset the alarm timing to all false
        builder.setNegativeButton(getString(R.string.clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alarmTimingChosen = new boolean[]{false, false, false, false, false};
                tasksFragmentViewModel.setAlarmTimingChosen(alarmTimingChosen);
                initAlarmTextView();
                dialogInterface.dismiss();
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
        String[] listItems = getResources().getStringArray(R.array.priority_dialog_selections);
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
                tasksFragmentViewModel.setPriorityChosen(priorityChosen);
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
            this.taskEntity.setCourseEventEntityId(this.chosenClassId);
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
                if (mainTimetableEntityId < 0) {
                    initNoMainTimetableDialog();
                } else {
                    initChooseClassFragment();
                }
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

        // Update the ViewModel
        tasksFragmentViewModel.setDeadLineCalendar(deadlineCalendar);
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

        // Update the ViewModel
        tasksFragmentViewModel.setDeadLineCalendar(deadlineCalendar);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Callback method for when user clicks the back button -- from SecondActivity.java
     */
    @Override
    public void myOnBackPressed() {
        closeFragmentDialog();
    }
}