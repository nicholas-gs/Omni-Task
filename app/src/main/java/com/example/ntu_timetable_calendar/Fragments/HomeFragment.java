package com.example.ntu_timetable_calendar.Fragments;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.example.ntu_timetable_calendar.Dialogs.TimetableEventDetailDialog;
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
import java.util.Objects;

public class HomeFragment extends Fragment implements EventClickListener<Event>,
        MonthChangeListener<Event> {

    private AppBarLayout mAppbarLayout;
    private Toolbar mToolbar;
    private WeekView<Event> mWeekView;

    // We store the events we want to display in the WeekView widget here
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

        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        initViews(view);
        getMainTimetable();
    }

    private void initViews(View view) {
        mAppbarLayout = view.findViewById(R.id.home_fragment_appbarlayout);

        mToolbar = view.findViewById(R.id.home_fragment_toolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.go_to_today_menu_item:
                        goToNow();
                        break;
                    case R.id.day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.three_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.five_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
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
        mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
        setEventHeight();
        goToNow();
    }

    private void setNumberOfVisibleDays(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(1);
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
                break;
            case R.id.three_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(3);
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
                break;
            case R.id.five_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(5);
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_12_SP));
                break;
        }
        item.setChecked(true);
    }

    /**
     * Go to current day and time for the WeekView widget
     */
    private void goToNow() {
        mWeekView.goToToday();
        mWeekView.goToCurrentTime();
    }

    /**
     * Since we are only going to display the main timetable in the WeekView widget, the will get only it from Room
     */
    private void getMainTimetable() {
        sqlViewModel.getMainTimetable().observe(this, new Observer<TimetableEntity>() {
            @Override
            public void onChanged(TimetableEntity timetableEntity) {
                // TimetableEntity may be null if the user does not have a saved timetable set as main
                if (timetableEntity != null) {
                    getEvents(timetableEntity);
                }
            }
        });
    }

    /**
     * Get all the CourseEventEntities belonging to the main timetable
     *
     * @param timetableEntity Main timetable from Room
     */
    private void getEvents(TimetableEntity timetableEntity) {
        sqlViewModel.getTimetableCourseEvents(timetableEntity.getId()).observe(this, new Observer<List<CourseEventEntity>>() {
            @Override
            public void onChanged(List<CourseEventEntity> courseEventEntityList) {
                displayEvents(courseEventEntityList);
            }
        });
    }

    /**
     * Takes in a list of CourseEventEntity from Room and converts them into Event for display in the WeekView widget
     *
     * @param courseEventEntityList List of CourseEventEntity from Room
     */
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

    /**
     * Set the size factor of the WeekView widget (simulate pinching the widget). The user can still adjust the size.
     */
    private void setEventHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        mWeekView.setHourHeight(height / 12f);
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
        TimetableEventDetailDialog dialog = new TimetableEventDetailDialog(event);
        dialog.show(getChildFragmentManager(), "timetable_event_detail_dialog");
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

        List<WeekViewDisplayable<Event>> finalList = new ArrayList<>();

        for (WeekViewDisplayable<Event> event : eventList) {
            if (event.toWeekViewEvent().getStartTime().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                finalList.add(event);
            }
        }
        return finalList;
    }

}
