package com.example.ntu_timetable_calendar.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.Adapter.SearchRVAdapter;
import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.ExamModels.Exam;
import com.example.ntu_timetable_calendar.Helper.KeyboardHelper;
import com.example.ntu_timetable_calendar.Helper.ListHelper;
import com.example.ntu_timetable_calendar.ItemDecorators.SearchRVItemDecorator;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements SearchRVAdapter.onItemClickListener {

    // Views
    private CardView searchWidget;
    private EditText searchEditText;
    private RecyclerView recyclerView;

    // Variables
    private static final String TAG = "SearchFragment";
    private List<Exam> allExams;
    private List<Course> allCourses;
    private SearchViewModel searchViewModel = null;
    private SearchRVAdapter searchRVAdapter;
    private List<String> list = new ArrayList<>();
    private Observer<List<Course>> observer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long startTime = System.nanoTime();

        initialiseViews(view);
        setupRecyclerView();
        setupViewModel();
        setupFilter();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        Log.d(TAG, "onViewCreated: DURATION : " + duration);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initialiseViews(View view) {
        searchWidget = view.findViewById(R.id.search_widget_container);
        searchEditText = view.findViewById(R.id.search_widget_edittext);
        recyclerView = view.findViewById(R.id.search_fragment_recyclerview);

        // Handle search widget on click - Show the soft keyboard programmatically
        searchWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.requestFocus();
                KeyboardHelper.showKeyBoard(Objects.requireNonNull(getActivity()));
            }
        });
    }

    private void setupViewModel() {
        if (searchViewModel == null) {
            searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        }

        observer = new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courseList) {
                Log.d(TAG, "onChanged: Triggered");
                List<Course> c = new ArrayList<>(courseList);
                ListHelper.insertDummyCourse(0, c);
                searchRVAdapter.submitList(c);
            }
        };

        searchViewModel.getFilteredList().observe(this, observer);

        // In the JsonDAO, we defined that if the query string == "" (empty string), we return the entire list of courses.
        // Hence we send this query in order to populate the recyclerview when the fragment is first initialised
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchViewModel.queryCourseData("");
            }
        }, 200);

    }

    /**
     * Handle functionality for search widget - Sends query string to searchViewModel
     */
    private void setupFilter() {

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String queryStr = editable.toString().toUpperCase().trim();

                searchViewModel.queryCourseData(queryStr);
            }
        });
    }

    private void setupRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        // Setup customised recyclerview item divider
        SearchRVItemDecorator dividerItemDecoration = new SearchRVItemDecorator(Objects.requireNonNull(getContext()),
                LinearLayout.VERTICAL, true);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(
                getContext(), R.drawable.rv_item_divider
        )));
        recyclerView.addItemDecoration(dividerItemDecoration);

        searchRVAdapter = new SearchRVAdapter(getContext());

        recyclerView.setAdapter(searchRVAdapter);

        searchRVAdapter.setOnItemClickListener(this);
    }

    /**
     * RecyclerView Item Clicked callback method
     *
     * @param position
     * @param course
     */
    @Override
    public void onClick(int position, Course course) {
        Toast.makeText(getContext(), "Code : " + course.getCourseCode(), Toast.LENGTH_SHORT).show();
    }
}
