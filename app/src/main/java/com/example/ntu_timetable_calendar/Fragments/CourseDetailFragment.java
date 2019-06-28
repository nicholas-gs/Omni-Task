package com.example.ntu_timetable_calendar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.ExamModels.Exam;
import com.example.ntu_timetable_calendar.Helper.StringHelper;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.ActivityViewModel;
import com.example.ntu_timetable_calendar.ViewModels.SearchViewModel;

import java.util.List;
import java.util.Objects;

public class CourseDetailFragment extends Fragment {

    // Views
    private Toolbar toolbar;
    private TextView nameTV, codeTV, auTV;
    private TextView dateTV, timeTV, durationTV, examTV;

    // Variables
    private Exam exam;
    private SearchViewModel searchViewModel;
    private ActivityViewModel activityViewModel;

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

        dateTV = view.findViewById(R.id.course_detail_date);
        timeTV = view.findViewById(R.id.course_detail_time);
        durationTV = view.findViewById(R.id.course_detail_duration);
        examTV = view.findViewById(R.id.course_detail_examTV);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_back_lightblue_24dp));

        // Close fragment
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
    }


    /**
     * Setup both the activityViewModel (pass the course object from the search fragment to this fragment),
     * as well as the searchFragmentViewModel (tied to this fragment's lifecycle). The latter is so that we can send
     * a query for the course's exam data.
     * <p>
     * DO NOT CHANGE THE ORDER OF INSTANTIATION OF THE TWO VIEWMODELS!!!!!
     */
    private void setupViewModel() {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        activityViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ActivityViewModel.class);

        searchViewModel.getFilteredExamData().observe(getViewLifecycleOwner(), new Observer<List<Exam>>() {
            @Override
            public void onChanged(List<Exam> exams) {

                if (exams.size() != 0) {
                    exam = exams.get(0);
                    examTV.setText("Final Exam");
                    String dateStr = exam.getDay() + ", " + exam.getDate();
                    dateTV.setText(dateStr);
                    String timeStr = "Start : " + exam.getTime();
                    timeTV.setText(timeStr);
                    String durationStr = "Duration : " + Float.toString(exam.getDuration()) + "h";
                    durationTV.setText(durationStr);
                } else {
                    examTV.setText("No Final Exam");
                    durationTV.setText("");
                    dateTV.setText("");
                    timeTV.setText("");
                }
            }
        });

        activityViewModel.getCourseToDetail().observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                bindCourseData(course);
                searchViewModel.queryExamData(course.getCourseCode());
            }
        });

    }

    /**
     * Bind data to the fragment's header (course detail)
     *
     * @param course
     */
    private void bindCourseData(Course course) {
        nameTV.setText(StringHelper.formatNameString(course.getName()));
        codeTV.setText(course.getCourseCode());
        auTV.setText(course.getAu());
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
