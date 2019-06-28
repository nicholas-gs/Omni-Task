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
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.example.ntu_timetable_calendar.Helper.KeyboardHelper;
import com.example.ntu_timetable_calendar.Helper.ListHelper;
import com.example.ntu_timetable_calendar.ItemDecorators.SearchRVItemDecorator;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.ActivityViewModel;
import com.example.ntu_timetable_calendar.ViewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements SearchRVAdapter.onItemClickListener {

    // Views
    private CardView searchWidget;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ImageView clearBtn;

    // Variables
    private static final String TAG = "SearchFragment";
    private SearchViewModel searchViewModel;
    private SearchRVAdapter searchRVAdapter;
    private Observer<List<Course>> observer;
    private ActivityViewModel activityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseViews(view);
        setupRecyclerView();
        setupFilter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialise the activityViewModel
        activityViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ActivityViewModel.class);
        // Initialise the SearchViewModel
        setupViewModel();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initialiseViews(View view) {
        searchWidget = view.findViewById(R.id.search_widget_container);
        searchEditText = view.findViewById(R.id.search_widget_edittext);
        recyclerView = view.findViewById(R.id.search_fragment_recyclerview);
        clearBtn = view.findViewById(R.id.search_clear);

        // Handle search widget on click - Show the soft keyboard programmatically
        searchWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.requestFocus();
                KeyboardHelper.showKeyBoard(Objects.requireNonNull(getActivity()));
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
            }
        });
    }

    /**
     * Setup SearchViewModel that is scoped to the lifecycle of the fragment
     */
    private void setupViewModel() {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        observer = new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courseList) {
                Log.d(TAG, "onChanged: Triggered");
                List<Course> c = new ArrayList<>(courseList);
                // Insert a dummy course object for the first item in the list (header)
                ListHelper.insertDummyCourse(0, c);
                searchRVAdapter.submitList(c);
            }
        };

        // By passing getViewLifecycleOwner
        searchViewModel.getFilteredList().observe(getViewLifecycleOwner(), observer);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: RUNNING");

                if (activityViewModel != null) {
                    // Get the previously stored query
                    String previousQuery = activityViewModel.getSearchQuery();
                    if (previousQuery != null && !(previousQuery.length() == 0)) {
                        searchViewModel.queryCourseData(previousQuery);
                        searchEditText.requestFocus();
                        searchEditText.setText(previousQuery);
                    } else {
                        searchViewModel.queryCourseData("");
                    }
                } else {
                    searchViewModel.queryCourseData("");
                }

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
                // Save query into SharedPreference
                String queryStr = editable.toString();
                activityViewModel.setSearchQuery(queryStr);
                searchViewModel.queryCourseData(queryStr.toUpperCase().trim());

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
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().add(R.id.activitymain_fragment_container, new CourseDetailFragment(), "course_detail_fragment")
                .addToBackStack(null).commit();
    }

}
