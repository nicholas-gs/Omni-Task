package com.example.ntu_timetable_calendar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.ExpandableCardView.ExpandableCardView;
import com.example.ntu_timetable_calendar.Helper.StringHelper;
import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Exam;
import com.example.ntu_timetable_calendar.JsonModels.Index;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.RVAdapters.IndexRVAdapter;
import com.example.ntu_timetable_calendar.ViewModels.JsonViewModel;
import com.example.ntu_timetable_calendar.ViewModels.SearchFragmentActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseDetailFragment extends Fragment {

    // Views
    private Toolbar toolbar;
    private TextView nameTV, codeTV, auTV;
    private TextView dateTV, timeTV, durationTV;
    private ExpandableCardView expandableCardView;
    private Spinner spinner;
    private RecyclerView recyclerView;

    // Variables
    private List<Index> indexList; // Course POJO passed from SearchFragment
    private Exam exam;
    private JsonViewModel jsonViewModel;
    private SearchFragmentActivityViewModel searchFragmentActivityViewModel;
    private IndexRVAdapter indexRVAdapter;

    private static final String TAG = "CardView";

    public CourseDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void initialiseViews(View view) {
        toolbar = view.findViewById(R.id.course_detail_toolbar);
        nameTV = view.findViewById(R.id.course_detail_name);
        codeTV = view.findViewById(R.id.course_detail_code);
        auTV = view.findViewById(R.id.course_detail_au);
        spinner = view.findViewById(R.id.course_detail_spinner);
        recyclerView = view.findViewById(R.id.course_detail_rv);
        ////////////////////////////////////////////////////////////////////////////////////////////
        expandableCardView = view.findViewById(R.id.course_detail_expandableCardView);

        dateTV = view.findViewById(R.id.course_detail_date);
        timeTV = view.findViewById(R.id.course_detail_time);
        durationTV = view.findViewById(R.id.course_detail_duration);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_back_white_24dp));

        // Close fragment
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        setupRecyclerView();

    }


    /**
     * Setup both the searchFragmentActivityViewModel (pass the course object from the search fragment to this fragment),
     * as well as the searchFragmentViewModel (tied to this fragment's lifecycle). The latter is so that we can send
     * a query for the course's exam data.
     * <p>
     * DO NOT CHANGE THE ORDER OF INSTANTIATION OF THE TWO VIEWMODELS!!!!!
     */
    private void setupViewModel() {
        jsonViewModel = ViewModelProviders.of(this).get(JsonViewModel.class);
        searchFragmentActivityViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SearchFragmentActivityViewModel.class);

        jsonViewModel.getFilteredExamData().observe(getViewLifecycleOwner(), new Observer<List<Exam>>() {
            @Override
            public void onChanged(List<Exam> exams) {
                bindExamData(exams);
            }
        });

        searchFragmentActivityViewModel.getCourseToDetail().observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                saveIndexes(course);
                bindCourseData(course);
                String s = course.getCourseCode();
                ArrayList<String> queryStrList = new ArrayList<>();
                queryStrList.add(s);
                jsonViewModel.queryExamData(queryStrList);
            }
        });
    }

    private void saveIndexes(Course course) {
        this.indexList = course.getIndexes();
    }

    /**
     * Bind data to the fragment's header (course detail)
     *
     * @param course Course to display it's information
     */
    private void bindCourseData(Course course) {
        nameTV.setText(StringHelper.formatNameString(course.getName()));
        codeTV.setText(course.getCourseCode());
        auTV.setText(course.getAu());

        bindSpinnerData(course);
    }

    /**
     * Bind data to the expandable card view
     *
     * @param exams Exam detail to display
     */
    private void bindExamData(List<Exam> exams) {

        if (exams.size() != 0) {

            expandableCardView.setIsExpandable(true);
            expandableCardView.setTitle(-1, "Final Exam");
            expandableCardView.setIndicator(-1, ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.arrow_down));

            exam = exams.get(0);
            String dateStr = exam.getDay() + ", " + exam.getDate();
            dateTV.setText(dateStr);
            String timeStr = "Start : " + exam.getTime();
            timeTV.setText(timeStr);
            String durationStr = "Duration : " + exam.getDuration() + "h";
            durationTV.setText(durationStr);
        } else {
            expandableCardView.removeIndicator();
            expandableCardView.setIsExpandable(false);
            expandableCardView.setTitle(-1, "No Final Exam");
        }
    }

    /**
     * Bind data of course indexes to be shown in the spinner, as well as handle the onItemSelectedListener.
     * This method is called inside bindCourseData() because courses index data is inside the course POJO
     */
    private void bindSpinnerData(Course course) {

        final List<String> listOfIndexes = new ArrayList<>();
        for (Index index : course.getIndexes()) {
            listOfIndexes.add(index.getIndexNumber());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.course_detail_fragment_spinner, listOfIndexes);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.course_detail_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSel = adapterView.getItemAtPosition(i).toString();

                for (Index index : indexList) {
                    if (index.getIndexNumber().equals(itemSel)) {
                        indexRVAdapter.setData(index.getDetails());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        indexRVAdapter = new IndexRVAdapter(getContext());
        recyclerView.setAdapter(indexRVAdapter);
    }

    /**
     * In order to destroy the viewmodel attached to this Fragment, we have to call this fragment's onDestroy().
     * onDestroy() is called when when we press back, when we initialise another fragment, onDestroy() is not called,
     * hence we manually call it inside onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}
