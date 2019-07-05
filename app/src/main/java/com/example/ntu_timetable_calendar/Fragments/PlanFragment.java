package com.example.ntu_timetable_calendar.Fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.EventClickListener;
import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.SearchViewModel;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlanFragment extends Fragment implements View.OnClickListener {

    private SearchViewModel searchViewModel;

    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private List<String> courseSelectionsList = new ArrayList<>();
    private List<String> finalSelList = new ArrayList<>();
    private List<String> allCourseCodesList = new ArrayList<>();

    // We store the events we want to display in the weekview widget here
    List<WeekViewDisplayable> eventList = new ArrayList<>();

    private static final String TAG = "PlanFragmentTAG";

    // Views
    private MaterialButton planButton, clearButton, chooseIndexesButton;
    private TextView errorTV;
    private WeekView mWeekView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initViewModel();
        initWeekViewWidget();
    }

    private void initViews(View view) {
        planButton = view.findViewById(R.id.plan_fragment_plan_button);
        planButton.setOnClickListener(this);
        clearButton = view.findViewById(R.id.plan_fragment_clear_button);
        clearButton.setOnClickListener(this);
        chooseIndexesButton = view.findViewById(R.id.plan_fragment_choose_indexes);
        chooseIndexesButton.setOnClickListener(this);
        mWeekView = view.findViewById(R.id.plan_fragment_weekView);
        errorTV = view.findViewById(R.id.plan_fragment_error_textview);
        multiAutoCompleteTextView = view.findViewById(R.id.plan_fragment_autocompletetextview);

    }

    private void initViewModel() {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        /*
          Setup viewmodel -- when it is complete and the list of all course codes is retrieved, call
          setupAutoCompleteTextView(List<String> courseCodeList)
         */
        searchViewModel.getAllCourseCode().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                // Save List<String> strings to allCourseCode object
                allCourseCodesList.addAll(strings);
                setupAutoCompleteTextView();
            }
        });

        /*
          Send the query to get all course codes
         */
        searchViewModel.queryAllCourseCode();

        /*
            Observe changes to the list of courses chosen by the user -- triggered when the user presses the plan button
            and the viewmodel receives the list of courses according to the query
         */
        searchViewModel.getTimetablePlanningCourseList().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courseList) {
                displayTimetable(courseList);
            }
        });
    }

    /**
     * Functionality of the weekview widget is defined here
     */
    private void initWeekViewWidget() {

        /*
          IMPORTANT! - We pass the list of events that we want to display in the widget here.
         */
        mWeekView.setMonthChangeListener(new MonthChangeListener() {
            @NotNull
            @Override
            public List<WeekViewDisplayable> onMonthChange(@NotNull Calendar calendar, @NotNull Calendar calendar1) {

                // This sets up the WeekView widget to display starting from Monday and 0700 -- DO NOT MOVE THESE LINES OF CODE
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                mWeekView.goToDate(cal);
                mWeekView.goToHour(7);



                /*
                This check is important -- without it, the same event will be duplicated three times as the library will
                preload three months of events!
                 */
                Calendar today = Calendar.getInstance();
                if (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    return eventList;
                } else {
                    List<WeekViewDisplayable> emptyList = new ArrayList<>();
                    return emptyList;
                }

            }
        });

        mWeekView.setOnEventClickListener(new EventClickListener() {
            @Override
            public void onEventClick(Object o, @NotNull RectF rectF) {

            }
        });

        /*
          This method affects how the time (vertical axis) and date/day (horizontal axis) are displayed. I override the
          original implementation in order to remove the date as I only want to show day.
         */
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @NotNull
            @Override
            public String interpretDate(@NotNull Calendar calendar) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                return weekdayNameFormat.format(calendar.getTime());
            }

            @NotNull
            @Override
            public String interpretTime(int i) {
                if (i < 10) {
                    return "0" + i + ":00";
                } else {
                    return i + ":00";
                }
            }
        });

    }

    /**
     * Initialize the autocompletetextview.
     * When the user clicks on 1 drop down list item, the string gets added to courseSelectionsList list for storage,
     * so that we can later query the courses that the user has selected
     */
    private void setupAutoCompleteTextView() {

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(),
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
                String str = editable.toString().toUpperCase();

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

        // Clear finalSelList list to get rid of previous entries!
        finalSelList.clear();
        // Removes all whitespaces and converts to upper case for sanity check
        for (int i = 0; i < courseSelectionsList.size(); i++) {
            finalSelList.add(courseSelectionsList.get(i).trim().toUpperCase());
        }

        if (finalSelList.size() == 0) {
            errorTV.setText(getString(R.string.error_message));
            errorTV.setVisibility(View.VISIBLE);
            return false;
        } else if (allCourseCodesList.containsAll(finalSelList)) {
            errorTV.setVisibility(View.GONE);
            return true;
        } else {
            errorTV.setText(getString(R.string.error_message));
            errorTV.setVisibility(View.VISIBLE);
            return false;
        }
    }

    /**
     * Takes in the list of queried courses and displays them in the timetable widget by 'converting' them
     * into a list of event objects stored in the class member variable eventList.
     * <p>
     * Called in the searchViewModel.getTimetablePlanningCourseList() observable above.
     *
     * @param courseList
     */
    private void displayTimetable(List<Course> courseList) {
        eventList.clear();

       /* Calendar startTime = Calendar.getInstance();
        Calendar endTime = (Calendar) startTime.clone();
        startTime.add(Calendar.MINUTE, -120);

        eventList.add(new Event(1, "Testing Event", startTime, endTime, "Testing Location",
                ContextCompat.getColor(getContext(), R.color.event_color_03), false, false));*/

        // Important -- Notifies the WeekView widget to refresh its data and display the new events/timetable
        mWeekView.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_fragment_clear_button:
                multiAutoCompleteTextView.setText("");
                courseSelectionsList.clear();
                break;
            case R.id.plan_fragment_plan_button:
                if (validationCheck()) {
                    searchViewModel.queryPlanningTimetableCourses(finalSelList);
                }
                break;
            case R.id.plan_fragment_choose_indexes:
                break;
        }
    }
}
