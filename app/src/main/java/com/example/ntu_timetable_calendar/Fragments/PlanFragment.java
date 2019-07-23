package com.example.ntu_timetable_calendar.Fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.EventClickListener;
import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.ntu_timetable_calendar.BottomSheets.PlanFragmentBottomSheet;
import com.example.ntu_timetable_calendar.Dialogs.SaveTimetableDialog;
import com.example.ntu_timetable_calendar.Dialogs.TimetableEventDetailDialog;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Exam;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.JsonViewModel;
import com.example.ntu_timetable_calendar.ViewModels.PlanFragmentActivityViewModel;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class PlanFragment extends Fragment implements View.OnClickListener, EventClickListener<Event>,
        MonthChangeListener<Event>, DateTimeInterpreter, PlanFragmentBottomSheet.PlanFragmentBottomSheetInterface,
        SaveTimetableDialog.DialogCallbackInterface, SQLViewModel.InsertTimetableCompletedListener {

    private JsonViewModel jsonViewModel;
    private PlanFragmentActivityViewModel planFragmentActivityViewModel;
    private SQLViewModel sqlViewModel;

    // Variables
    private boolean openBottomSheet = false;
    private List<String> courseSelectionsList = new ArrayList<>();
    private List<String> finalCourseCodeSelList = new ArrayList<>();
    private List<String> allCourseCodesList = new ArrayList<>();

    private TimetableEntity timetableEntity;

    // Temporary store the list of courses sent by the JsonViewModel after sending the query
    private List<Course> queriedCourseList = new ArrayList<>();

    // Temporary store the list of exam sent by the JsonViewModel after sending the query
    private List<Exam> queriedExamList = new ArrayList<>();

    // A HashMap for temporary storage of the index selection for each course that the user has queried.
    private Map<String, String> indexesSel = new HashMap<>();

    // We store the events we want to display in the weekview widget here
    private List<WeekViewDisplayable<Event>> eventList = new ArrayList<>();

    // Views
    private MaterialButton submitButton, clearButton, chooseIndexesButton;
    private ImageView saveIcon;
    private TextView errorTV;
    private WeekView<Event> mWeekView;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initJsonViewModel();
        initActivityViewModel();
        initSQLViewModel();
        initWeekViewWidget();

        queriedCourseList.addAll(planFragmentActivityViewModel.getQueriedCourseList());
        queriedExamList.addAll(planFragmentActivityViewModel.getQueriedExamList());
        indexesSel.putAll(planFragmentActivityViewModel.getIndexesSel());
    }

    private void initViews(View view) {
        submitButton = view.findViewById(R.id.submit_fragment_plan_button);
        submitButton.setOnClickListener(this);
        clearButton = view.findViewById(R.id.plan_fragment_clear_button);
        clearButton.setOnClickListener(this);
        saveIcon = view.findViewById(R.id.plan_fragment_save_icon);
        saveIcon.setOnClickListener(this);
        chooseIndexesButton = view.findViewById(R.id.plan_fragment_choose_indexes);
        chooseIndexesButton.setOnClickListener(this);
        mWeekView = view.findViewById(R.id.plan_fragment_weekView);
        errorTV = view.findViewById(R.id.plan_fragment_error_textview);
        multiAutoCompleteTextView = view.findViewById(R.id.plan_fragment_autocompletetextview);
    }

    private void initJsonViewModel() {
        jsonViewModel = ViewModelProviders.of(this).get(JsonViewModel.class);

        /*
          Setup viewmodel -- when it is complete and the list of all course codes is retrieved, call
          setupAutoCompleteTextView(List<String> courseCodeList)
         */
        jsonViewModel.getAllCourseCode().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                // Save List<String> strings to allCourseCode object
                allCourseCodesList.clear();
                allCourseCodesList.addAll(strings);
                setupAutoCompleteTextView();
            }
        });

        /*
          Send the query to get all course codes
         */
        jsonViewModel.queryAllCourseCode();

        /*
            Observe changes to the list of courses chosen by the user -- triggered when the user presses the submit button
            and the viewmodel receives the list of courses according to the query
         */
        jsonViewModel.getTimetablePlanningCourseList().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courseList) {
                saveQueriedCourseList(courseList);
                if (openBottomSheet) {
                    chooseIndexesButton.performClick();
                    openBottomSheet = false;
                }
            }
        });

        jsonViewModel.getFilteredExamData().observe(this, new Observer<List<Exam>>() {
            @Override
            public void onChanged(List<Exam> examList) {
                savedQueriedExamList(examList);
            }
        });

    }

    private void initActivityViewModel() {
        planFragmentActivityViewModel = ViewModelProviders.of(requireActivity())
                .get(PlanFragmentActivityViewModel.class);
          /*
            When triggered, it means that is a new timetable to display. It is triggered BottomSheet submit button
            click callback below
         */
        planFragmentActivityViewModel.getTimetableEvents().observe(requireActivity(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> eventList) {

                // Make sure the WeekView widget go to 0700 and start from Monday
                mWeekView.goToHour(8);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                mWeekView.goToDate(calendar);

                displayTimetable(eventList);
            }
        });
    }

    private void initSQLViewModel() {
        sqlViewModel = ViewModelProviders.of(requireActivity()).get(SQLViewModel.class);
        sqlViewModel.setmListener(this);
    }

    /**
     * Save the list of courses from the jsonViewModel into the member variable called queriedCourseList.
     * For use outside the ViewModel's onChanged Method
     *
     * @param courseList - List of courses to save in the class member variable
     */
    private void saveQueriedCourseList(List<Course> courseList) {
        this.queriedCourseList.clear();
        this.queriedCourseList.addAll(courseList);
        planFragmentActivityViewModel.setQueriedCourseList(this.queriedCourseList);
    }

    /**
     * Save the list of Exam POJO from the jsonViewModel into the member variable called queriedExamList.
     * For use outside the ViewModel's onChanged Method
     *
     * @param examList - List of exam to save in the class member variable
     */
    private void savedQueriedExamList(List<Exam> examList) {
        this.queriedExamList.clear();
        this.queriedExamList.addAll(examList);
        planFragmentActivityViewModel.setQueriedExamList(this.queriedExamList);
    }


    /**
     * Functionality of the weekview widget is defined here
     */
    private void initWeekViewWidget() {

        /*
        This sets up the calendar to show the days MONDAY to FRIDAY and 0700.
        And since horizontal scrolling is disabled, the user cannot scroll to other days.
        It also sets the widget to start from 0700, but the user can still scroll up and down.
         */
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mWeekView.goToDate(cal);
        mWeekView.goToHour(8);

        mWeekView.setMonthChangeListener(this);

        mWeekView.setOnEventClickListener(this);

        mWeekView.setDateTimeInterpreter(this);

    }

    /**
     * Initialize the autocompletetextview.
     * When the user clicks on 1 drop down list item, the string gets added to courseSelectionsList list for storage,
     * so that we can later query the courses that the user has selected
     */
    private void setupAutoCompleteTextView() {

        multiAutoCompleteTextView.setText(planFragmentActivityViewModel.getEnterModuleQuery());

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, allCourseCodesList);

        multiAutoCompleteTextView.setAdapter(listAdapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Don't need to do anything, the string is automatically added to the textview
            }
        });

        multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString().toUpperCase().trim();
                planFragmentActivityViewModel.setEnterModuleQuery(str);

                // Removes all whitespaces and commas
                List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
                courseSelectionsList.clear();
                courseSelectionsList.addAll(items);
            }
        });
    }

    /**
     * Check if all of the user input courses are correct/exists by checking if all entered course codes can be
     * found in the allCourseCodesList.
     * <p>
     * This function is called when the plan button is pressed.
     * <p>
     * Returns true if user input is valid, false if not
     */
    private boolean validationCheck() {

        // Clear finalCourseCodeSelList list to get rid of previous entries!
        finalCourseCodeSelList.clear();
        // Removes all whitespaces and converts to upper case for sanity check
        for (int i = 0; i < courseSelectionsList.size(); i++) {
            finalCourseCodeSelList.add(courseSelectionsList.get(i).trim().toUpperCase());
        }

        if (finalCourseCodeSelList.size() == 0) {
            errorTV.setText(getString(R.string.error_message));
            errorTV.setVisibility(View.VISIBLE);
            return false;
        } else if (allCourseCodesList.containsAll(finalCourseCodeSelList)) {
            errorTV.setVisibility(View.GONE);
            return true;
        } else {
            errorTV.setText(getString(R.string.error_message));
            errorTV.setVisibility(View.VISIBLE);
            return false;
        }
    }

    /**
     * Takes in the list of event objects and stores them in the class member variable eventList,
     * <p>
     * Called in the planFragmentActivityViewModel.getTimetableEvents() observable
     */
    private void displayTimetable(List<Event> eventList) {
        this.eventList.clear();
        //mWeekView.notifyDataSetChanged();
        this.eventList.addAll(eventList);

        // Important -- Notifies the WeekView widget to refresh its data and display the new events/timetable
        mWeekView.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_fragment_clear_button:
                clearButtonPressed();
                break;
            case R.id.submit_fragment_plan_button:
                submitButtonPressed();
                break;
            case R.id.plan_fragment_choose_indexes:
                PlanFragmentBottomSheet planFragmentBottomSheet = new PlanFragmentBottomSheet();
                planFragmentBottomSheet.setPlanFragmentBottomSheetInterface(this);
                planFragmentBottomSheet.show(getChildFragmentManager(), "plan_fragment_bottom_sheet");
                break;
            case R.id.plan_fragment_save_icon:
                if (saveIcon.isSelected()) {
                    saveIcon.setSelected(false);
                } else {
                    saveIcon.setSelected(true);
                    openSaveTimetableDialog();
                }
                break;
        }
    }

    /**
     * Functionality for the fragment's clear button pressed
     * 1) Clear the multiAutoCompleteTextView
     * 2) Clear the courseSelectionsList and indexesSel variable
     * 3) Clear the parameters in the planFragmentActivityViewModel
     * 4) Clear the list of events to clear the WeekView widget
     */
    private void clearButtonPressed() {
        multiAutoCompleteTextView.setText("");
        errorTV.setVisibility(View.GONE);

        courseSelectionsList.clear();
        queriedCourseList.clear();
        indexesSel.clear();

        planFragmentActivityViewModel.setEnterModuleQuery("");
        planFragmentActivityViewModel.setQueriedCourseList(this.queriedCourseList);
        planFragmentActivityViewModel.setIndexesSel(this.indexesSel);
        planFragmentActivityViewModel.convertCoursesToEvents();
    }

    /**
     * Functionality for the fragment's submit button
     * When the user presses submit, two things happen :
     * 1) Send the query through jsonViewModel.queryPlanningTimetableCourses() to get the list of Course objects
     * 2) Send the query through  jsonViewModel.queryExamData() to get the list of Exam objects
     */
    private void submitButtonPressed() {
        if (validationCheck()) {
            openBottomSheet = true;
            jsonViewModel.queryPlanningTimetableCourses(finalCourseCodeSelList);
            jsonViewModel.queryExamData(finalCourseCodeSelList);
        }
    }

    /**
     * Show a dialog that allows the user to enter name of the new timetable for the user to save in Room
     */
    private void openSaveTimetableDialog() {
        SaveTimetableDialog saveTimetableDialog = new SaveTimetableDialog();
        saveTimetableDialog.setDialogInterface(this);
        saveTimetableDialog.show(getChildFragmentManager(), "save_timetable_dialog");
    }

    /**
     * Bottomsheet plan button click listener
     *
     * @param newIndexesSel - HashMap of indexes selected by the user for each course, returned by the bottom sheet
     */
    @Override
    public void onPlanButtonClicked(Map<String, String> newIndexesSel) {
        indexesSel.clear();
        indexesSel.putAll(newIndexesSel);
        planFragmentActivityViewModel.setIndexesSel(this.indexesSel);
        planFragmentActivityViewModel.convertCoursesToEvents();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * IMPORTANT! - We pass the list of events that we want to display in the WeekView widget here.
     */
    @NotNull
    @Override
    public List<WeekViewDisplayable<Event>> onMonthChange(@NotNull Calendar
                                                                  calendar, @NotNull Calendar calendar1) {
        /*
          This check is important -- without it, the same event will be duplicated three times as the library will
          preload three months of events!
         */
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
            return eventList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * WeekView widget event click listener
     *
     * @param event Event clicked
     * @param rectF The drawable clicked
     */
    @Override
    public void onEventClick(Event event, @NotNull RectF rectF) {
        TimetableEventDetailDialog dialog = new TimetableEventDetailDialog(event);
        dialog.show(getChildFragmentManager(), "timetable_event_detail_dialog");
    }

    /**
     * This method affects how the date/day (horizontal axis) are displayed. I override the
     * original implementation in order to remove the date as I only want to show day.
     *
     * @param calendar Calendar
     * @return String to display
     */
    @NotNull
    @Override
    public String interpretDate(@NotNull Calendar calendar) {
        SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        return weekdayNameFormat.format(calendar.getTime());
    }

    /**
     * This method affects how the time (vertical axis) are displayed. I override the
     * original implementation in order to show time in 24h format.
     */
    @NotNull
    @Override
    public String interpretTime(int i) {
        if (i < 10) {
            return "0" + i + ":00";
        } else {
            return i + ":00";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Save timetable dialog save button click callback
     *
     * @param timetableName        String of the name of the new timetable saved
     * @param timetableDescription String of the description of the new timetable saved
     */
    @Override
    public void saveButtonPressed(final String timetableName, final String timetableDescription, final boolean isMainTimeTable) {

        // Don't allow user to save timetable if it is empty
        if (courseSelectionsList.size() == 0) {
            Toasty.info(requireContext(), "Timetable is empty", Toasty.LENGTH_SHORT).show();
        } else {
            this.timetableEntity = new TimetableEntity(timetableName, timetableDescription, isMainTimeTable);
            sqlViewModel.insertTimetable(this.timetableEntity);

            if (saveIcon.isSelected()) {
                saveIcon.setSelected(false);
            }
        }

    }

    /**
     * Interface callback for save timetable dialog cancel button pressed
     */
    @Override
    public void cancelButtonPressed() {
        if (saveIcon.isSelected()) {
            saveIcon.setSelected(false);
        }
    }

    /**
     * Callback method from SQLViewModel that returns the id of the newly inserted timetable
     *
     * @param timetableId id of the newly inserted timetable
     */
    @Override
    public void onInsertCallback(Long timetableId) {
        // If the newly inserted timetable is the main timetable, set is as such and set the previous main timetable to false
        if (timetableEntity.getIsMainTimetable()) {
            sqlViewModel.setIsMainTimetable(timetableId.intValue());
        }
        // Save the courses into Room
        sqlViewModel.insertCourses(timetableId.intValue(), this.queriedCourseList, this.indexesSel);
        sqlViewModel.insertExams(this.queriedExamList, timetableId.intValue());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toasty.success(requireContext(), "Timetable saved", Toasty.LENGTH_SHORT).show();
            }
        }, 200);

    }
}
