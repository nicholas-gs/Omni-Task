package com.example.ntu_timetable_calendar.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.converters.AlarmParser;
import com.example.ntu_timetable_calendar.models.entities.AlarmEntity;
import com.example.ntu_timetable_calendar.models.entities.CourseEventEntity;
import com.example.ntu_timetable_calendar.models.entities.TaskEntity;
import com.example.ntu_timetable_calendar.models.entities.TimetableEntity;
import com.example.ntu_timetable_calendar.utils.InitialiseBackStack;
import com.example.ntu_timetable_calendar.utils.datahelper.BooleanArrayHelper;
import com.example.ntu_timetable_calendar.utils.datahelper.EntryValidationCheck;
import com.example.ntu_timetable_calendar.utils.datahelper.StringHelper;
import com.example.ntu_timetable_calendar.utils.dialogs.CloseFragmentAlertDialog;
import com.example.ntu_timetable_calendar.utils.dialogs.DeleteTaskAlertDialog;
import com.example.ntu_timetable_calendar.utils.dialogs.MyDatePickerDialog;
import com.example.ntu_timetable_calendar.utils.dialogs.MyTimePickerDialog;
import com.example.ntu_timetable_calendar.utils.dialogs.NoMainTimetableAlertDialog;
import com.example.ntu_timetable_calendar.utils.viewformatters.AlarmTextViewFormatter;
import com.example.ntu_timetable_calendar.utils.viewformatters.PriorityTextViewFormatter;
import com.example.ntu_timetable_calendar.viewmodels.SQLViewModel;
import com.example.ntu_timetable_calendar.viewmodels.TasksFragmentViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class TaskDetailFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener/*, SecondActivity.MyOnBackPressedListener*/ {

    // Widgets
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextInputLayout mTitleInputLayout, mDescriptionInputLayout;
    private ImageView clearClassButton;
    private TextInputEditText mTitleEditText, mDescriptionEditText;
    private TextView chosenClassTextView, endDateTV, endTimeTV, addAlarmTV, addPriorityTV, addProjectTV;
    private MaterialButton markAsDoneButton;

    // Variables
    private boolean launchedFromNotification;
    private int taskEntityId;

    private int mainTimetableEntityId;
    private TaskEntity taskEntity;

    private String chosenClassTitle;                        // For local use only
    private String chosenClassTiming;                       // For local use only
    private int chosenClassId;                              // Need to update
    private String title, description;                      // Need to update
    private int priorityChosen;                             // Need to update
    private Calendar deadlineCalendar;                      // Need to update
    private boolean[] alarmTimingChosen;                    // Need to update
    private boolean isDone;                                 // Need to update

    // ViewModel
    private TasksFragmentViewModel tasksFragmentViewModel;
    private SQLViewModel sqlViewModel;

    // Utils
    private PriorityTextViewFormatter priorityTextViewFormatter;
    private AlarmTextViewFormatter alarmTextViewFormatter;
    private EntryValidationCheck entryValidationCheck;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public TaskDetailFragment(int taskEntityId, boolean launchedFromNotification) {
        this.taskEntityId = taskEntityId;
        this.launchedFromNotification = launchedFromNotification;
    }

    public TaskDetailFragment() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Handles what happens when activity onBackPressed is called.
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                closeFragmentDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initUtils();
        initToolbar();
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

        clearClassButton = view.findViewById(R.id.task_detail_clear_class_button);
        clearClassButton.setOnClickListener(this);
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
        markAsDoneButton = view.findViewById(R.id.task_detail_mark_as_done_button);
        markAsDoneButton.setOnClickListener(this);
    }

    private void initUtils() {
        priorityTextViewFormatter = new PriorityTextViewFormatter(requireContext(), addPriorityTV);
        alarmTextViewFormatter = new AlarmTextViewFormatter(requireContext(), addAlarmTV);
        entryValidationCheck = new EntryValidationCheck(requireContext(), mTitleInputLayout, mDescriptionInputLayout);
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
                        removeTaskAlarms();
                    } else {
                        Objects.requireNonNull(getActivity()).finish();
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
     * Remove all the alarms of this task in AlarmManager.
     * NOTE - We set up the alarms in the saveTask() method below
     */
    private void removeTaskAlarms() {
        final AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        sqlViewModel.getAllTaskAlarms(this.taskEntity.getId()).observe(this, new Observer<List<AlarmEntity>>() {
            @Override
            public void onChanged(List<AlarmEntity> alarmEntityList) {
                if (alarmEntityList != null && alarmEntityList.size() != 0) {
                    AlarmParser.deleteScheduledAlarmsHelper(requireContext(), alarmManager, alarmEntityList);
                }
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
        tasksFragmentViewModel.setIsDone(taskEntity.getIsDone());
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
        this.isDone = this.taskEntity.getIsDone();
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
        this.isDone = tasksFragmentViewModel.getIsDone();

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
        updateViewsByIsDone();
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
            chosenClassTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorContentText));
        } else {
            if (chosenClassTitle != null && chosenClassTiming != null) {
                String str = chosenClassTitle + " : " + chosenClassTiming;
                chosenClassTextView.setText(str);
                chosenClassTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
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
        alarmTextViewFormatter.update(this.alarmTimingChosen);
    }

    /**
     * Initialise the Priority's flag icon and text view based on the TaskEntity's priority
     */
    private void initPriorityTextViewAndIcon() {
        priorityTextViewFormatter.format(this.priorityChosen);
    }

    /**
     * Update the views (clickable & strike-through) based on the boolean value of isDone variable above
     */
    private void updateViewsByIsDone() {
        // Update the clickable state of the views
        setAllViewsClickableState(isDone);

        if (isDone) {
            markAsDoneButton.setText(getString(R.string.mark_as_not_done));
            // Strike-through chosenClassTextView only if user has chosen one
            if (chosenClassId >= 0) {
                chosenClassTextView.setPaintFlags(chosenClassTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            endDateTV.setPaintFlags(endDateTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            endTimeTV.setPaintFlags(endTimeTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            // Strike-through addAlarmTV only if user has at least one alarm set
            if (!BooleanArrayHelper.AreAllFalse(alarmTimingChosen)) {
                addAlarmTV.setPaintFlags(addAlarmTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            addPriorityTV.setPaintFlags(addPriorityTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            /* Remove all strike-through */
            markAsDoneButton.setText(getString(R.string.mark_as_done));
            chosenClassTextView.setPaintFlags(chosenClassTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            endDateTV.setPaintFlags(endDateTV.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            endTimeTV.setPaintFlags(endTimeTV.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            addAlarmTV.setPaintFlags(addAlarmTV.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            addPriorityTV.setPaintFlags(addPriorityTV.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * If the task is marked as done, we don't want to allow user to be able to edit it, so we disable
     * the view's clickable state
     *
     * @param isDone Whether task is complete
     */
    private void setAllViewsClickableState(boolean isDone) {
        chosenClassTextView.setClickable(!isDone);
        clearClassButton.setClickable(!isDone);
        endDateTV.setClickable(!isDone);
        endTimeTV.setClickable(!isDone);
        addAlarmTV.setClickable(!isDone);
        addPriorityTV.setClickable(!isDone);
        addProjectTV.setClickable(!isDone);

        if (isDone) {
            // Prevent user when editing the text in the two EditTexts
            mTitleEditText.setFocusable(false);
            mDescriptionEditText.setFocusable(false);
        } else {
            // Re-enable user to edit using the EditTexts
            mTitleEditText.setFocusableInTouchMode(true);
            mDescriptionEditText.setFocusableInTouchMode(true);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Dialog shown to user when the user clicks the close button on the toolbar -- prompts user if they want to discard changes to the task
     */
    private void closeFragmentDialog() {

        DialogInterface.OnClickListener positiveButtonClicked = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InitialiseBackStack.initMainActivity(requireActivity(), launchedFromNotification);
                Objects.requireNonNull(requireActivity()).finish();
            }
        };

        DialogInterface.OnClickListener negativeButtonClicked = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };


        new CloseFragmentAlertDialog(requireContext(), positiveButtonClicked, negativeButtonClicked).build().show();
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
        DialogInterface.OnClickListener positiveButtonClicked = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sqlViewModel.deleteTask(taskEntity);

                Toasty.success(requireContext(), getString(R.string.task_deleted), Toasty.LENGTH_SHORT).show();

                InitialiseBackStack.initMainActivity(requireActivity(), launchedFromNotification);

                Objects.requireNonNull(getActivity()).finish();
            }
        };

        DialogInterface.OnClickListener negativeButtonClicked = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

        new DeleteTaskAlertDialog(requireContext(), positiveButtonClicked, negativeButtonClicked).build().show();

    }

    /**
     * Show a dialog telling the user there is no timetable if there is no main timetable to choose a class
     */
    private void showNoMainTimetableDialog() {
        DialogInterface.OnClickListener positiveButtonClicked = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

        new NoMainTimetableAlertDialog(requireContext(), positiveButtonClicked).build().show();
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
     * Save updated task to Room
     */
    private void saveTask() {

        this.title = Objects.requireNonNull(mTitleEditText.getText()).toString().trim();
        this.description = Objects.requireNonNull(mDescriptionEditText.getText()).toString().trim();

        if (entryValidationCheck.validate(this.title, this.description)) {
            this.taskEntity.setCourseEventEntityId(this.chosenClassId);
            this.taskEntity.setTitle(this.title);
            this.taskEntity.setDescription(this.description);
            this.taskEntity.setPriorityLevel(this.priorityChosen);
            this.taskEntity.setDeadLine(this.deadlineCalendar.getTimeInMillis());
            this.taskEntity.setAlarmTimingChosen(this.alarmTimingChosen);
            this.taskEntity.setIsDone(this.isDone);

            List<Long> alarmList = new AlarmParser(this.alarmTimingChosen, this.deadlineCalendar).parse();

            this.taskEntity.setAlarmList(alarmList);

            // Save updated task to Room
            sqlViewModel.updateTask(this.taskEntity);

            if (this.isDone) {
                // Delete all the task's alarm if task is done since we do not need to inform user again
                sqlViewModel.deleteTaskAlarms(this.taskEntity.getId());
            } else {
                // Delete this task's alarms ( I delete all cause i can't be bothered to figure out what changes to task's alarm timings have been saved :/ )
                sqlViewModel.deleteTaskAlarms(this.taskEntity.getId());

                List<AlarmEntity> alarmEntityList = new ArrayList<>();
                for (Long l : this.taskEntity.getAlarmList()) {
                    alarmEntityList.add(new AlarmEntity(taskEntity.getId(), l, taskEntity.getTitle() + " task due"));
                }

                // Insert this task's alarms into Room
                for (AlarmEntity alarmEntity : alarmEntityList) {
                    sqlViewModel.insertAlarm(alarmEntity);
                }
            }

            InitialiseBackStack.initMainActivity(requireActivity(), launchedFromNotification);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toasty.success(requireContext(), getString(R.string.changes_saved), Toasty.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).finish();
                }
            }, 250);

        }
    }

    /**
     * Clear the ChosenClassId in the TasksFragmentViewModel
     * The LiveData observable will be triggered above and the text view will be cleared
     */
    private void clearChosenClass() {
        tasksFragmentViewModel.setChosenClassId(-1);
    }

    /**
     * Set the view
     */
    private void markAsDoneButtonPressed() {
        // Negate the current boolean value
        this.isDone = !this.isDone;
        tasksFragmentViewModel.setIsDone(this.isDone);

        // Update the views based on the value of isDone
        updateViewsByIsDone();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_detail_choose_class_textview:
                if (mainTimetableEntityId < 0) {
                    showNoMainTimetableDialog();
                } else {
                    initChooseClassFragment();
                }
                break;
            case R.id.task_detail_clear_class_button:
                clearChosenClass();
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
            case R.id.task_detail_mark_as_done_button:
                markAsDoneButtonPressed();
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
}