package com.example.ntu_timetable_calendar.Fragments;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alamkanak.weekview.EventClickListener;
import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.SecondActivity;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements EventClickListener<Event>,
        MonthChangeListener<Event> {

    private AppBarLayout mAppbarLayout;
    private Toolbar mToolbar;
    private WeekView<Event> mWeekView;

    // We store the events we want to display in the weekview widget here
    private List<WeekViewDisplayable<Event>> eventList = new ArrayList<>();

    private SQLViewModel sqlViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        getMainTimetable();
    }

    private void initViews(View view) {
        mAppbarLayout = view.findViewById(R.id.home_fragment_appbarlayout);

        mToolbar = view.findViewById(R.id.home_fragment_toolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.saved_timetables_menu_item:
                        startSecondActivity(getString(R.string.SAVED_TIMETABLES_INTENT));
                        break;
                    case R.id.about_menu_item:
                        startSecondActivity(getString(R.string.ABOUT_APP_INTENT));
                        break;
                }
                return true;
            }
        });

        mWeekView = view.findViewById(R.id.home_fragment_weekview);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.goToHour(8);
    }


    private void getMainTimetable() {
        sqlViewModel.getMainTimetable().observe(this, new Observer<TimetableEntity>() {
            @Override
            public void onChanged(TimetableEntity timetableEntity) {
                if (timetableEntity != null) {
                    getEvents(timetableEntity);
                }
            }
        });
    }

    private void getEvents(TimetableEntity timetableEntity) {

        sqlViewModel.getTimetableCourseEvents(timetableEntity.getId()).observe(this, new Observer<List<CourseEventEntity>>() {
            @Override
            public void onChanged(List<CourseEventEntity> courseEventEntityList) {
                displayEvents(courseEventEntityList);
            }
        });
    }

    private void displayEvents(List<CourseEventEntity> courseEventEntityList) {
        this.eventList.clear();

        Calendar calendar = Calendar.getInstance();

        for (CourseEventEntity courseEventEntity : courseEventEntityList) {
            Calendar start = (Calendar) calendar.clone();
            start.setTimeInMillis(courseEventEntity.getStartTime());
            Calendar end = (Calendar) calendar.clone();
            end.setTimeInMillis(courseEventEntity.getEndTime());

            Event event = new Event(courseEventEntity.getId(), courseEventEntity.getTitle(), start, end
                    , courseEventEntity.getLocation(), courseEventEntity.getColor(),
                    courseEventEntity.getAllDay(), courseEventEntity.getCanceled());

            this.eventList.add(event);
        }
        mWeekView.notifyDataSetChanged();
    }

    private void startSecondActivity(String intentString) {
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(getString(R.string.ACTIVITY_INTENT), intentString);
        startActivity(intent);
    }

    /**
     * WeekView widget event click listener
     *
     * @param event Event clicked
     * @param rectF The drawable clicked
     */
    @Override
    public void onEventClick(Event event, @NotNull RectF rectF) {

    }

    /**
     * IMPORTANT! - We pass the list of events that we want to display in the WeekView widget here.
     */
    @NotNull
    @Override
    public List<WeekViewDisplayable<Event>> onMonthChange(@NotNull Calendar calendar, @NotNull Calendar calendar1) {
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

}
