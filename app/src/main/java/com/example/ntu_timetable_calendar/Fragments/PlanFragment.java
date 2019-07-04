package com.example.ntu_timetable_calendar.Fragments;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.SearchViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanFragment extends Fragment implements View.OnClickListener {

    private SearchViewModel searchViewModel;

    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private List<String> courseSelectionsList = new ArrayList<>();
    private List<String> finalSelList = new ArrayList<>();
    private List<String> allCourseCodesList = new ArrayList<>();
    // private List<Course> queriedCourseList = new ArrayList<>();

    private static final String TAG = "PlanFragmentText";

    // Views
    private MaterialButton planButton, clearButton;
    private TextView errorTV;

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
    }

    private void initViews(View view) {
        planButton = view.findViewById(R.id.plan_fragment_plan_button);
        planButton.setOnClickListener(this);
        clearButton = view.findViewById(R.id.plan_fragment_clear_button);
        clearButton.setOnClickListener(this);
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
         */
        searchViewModel.getTimetablePlanningCourseList().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courseList) {
                for (Course course : courseList) {
                    Log.d(TAG, "onChanged: Course code : " + course.getCourseCode());
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
     * This function is called by "planTimetable(List<String> finalSelList)"
     * Returns true if user input is valid, false if not
     */
    private boolean validationCheck() {

        // Clear finalSelList list to get rid of previous entries!
        finalSelList.clear();
        // Removes all whitespaces and converts to upper case for sanity check
        for (int i = 0; i < courseSelectionsList.size(); i++) {
            finalSelList.add(courseSelectionsList.get(i).trim().toUpperCase());
        }

        if (allCourseCodesList.containsAll(finalSelList)) {
            errorTV.setVisibility(View.GONE);
            return true;
        } else {
            errorTV.setText(getString(R.string.error_message));
            errorTV.setVisibility(View.VISIBLE);
            return false;
        }
    }

    /**
     * Called when plan fragment button is pressed -- send the list of all course codes chosen by the user to the viewmodel
     */
    private void planTimetable() {
        if (validationCheck()) {
            searchViewModel.queryPlanningTimetableCourses(finalSelList);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_fragment_clear_button:
                multiAutoCompleteTextView.setText("");
                courseSelectionsList.clear();
                break;
            case R.id.plan_fragment_plan_button:
                planTimetable();
                break;
        }
    }
}
