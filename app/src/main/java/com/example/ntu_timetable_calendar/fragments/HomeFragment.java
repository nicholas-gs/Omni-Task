package com.example.ntu_timetable_calendar.fragments;

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
import com.alamkanak.weekview.ScrollListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.ntu_timetable_calendar.utils.dialogs.TimetableEventDetailDialog;
import com.example.ntu_timetable_calendar.models.entities.CourseEventEntity;
import com.example.ntu_timetable_calendar.models.entities.ExamEventEntity;
import com.example.ntu_timetable_calendar.models.entities.TimetableEntity;
import com.example.ntu_timetable_calendar.models.Event;
import com.example.ntu_timetable_calendar.converters.objectconverters.WeekViewConverter;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.activities.SecondActivity;
import com.example.ntu_timetable_calendar.viewmodels.HomeFragmentActivityViewModel;
import com.example.ntu_timetable_calendar.viewmodels.SQLViewModel;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements EventClickListener<Event>,
        MonthChangeListener<Event>, ScrollListener {

    private AppBarLayout mAppbarLayout;
    private Toolbar mToolbar;
    private WeekView<Event> mWeekView;

    // We store the events we want to display in the WeekView widget here
    private List<WeekViewDisplayable<Event>> eventList = new ArrayList<>();

    // ViewModels
    private SQLViewModel sqlViewModel;
    private HomeFragmentActivityViewModel homeFragmentActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModels();
        initViews(view);
        goToTime();
        setEventHeight();
        initVisibleDays();
        getMainTimetableClasses();
    }

    private void initViewModels() {
        sqlViewModel = ViewModelProviders.of(requireActivity()).get(SQLViewModel.class);
        homeFragmentActivityViewModel = ViewModelProviders.of(requireActivity()).get(HomeFragmentActivityViewModel.class);
    }

    private void initViews(View view) {
        mAppbarLayout = view.findViewById(R.id.home_fragment_appbarlayout);

        mToolbar = view.findViewById(R.id.home_fragment_toolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_fragment_go_to_today_menu_item:
                        goToNow();
                        break;
                    case R.id.home_fragment_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.home_fragment_three_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.home_fragment_five_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.home_fragment_saved_timetables_menu_item:
                        startSecondActivity(getString(R.string.SAVED_TIMETABLES_INTENT));
                        break;
                    case R.id.home_fragment_about_menu_item:
                        startSecondActivity(getString(R.string.ABOUT_APP_INTENT));
                        break;
                }
                return true;
            }
        });

        mWeekView = view.findViewById(R.id.home_fragment_weekview);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setScrollListener(this);
    }

    /**
     * Get the number of visible days from the HomeFragmentActivityViewModel and set the WeekView widget to it by mimicking a toolbar menu click
     */
    private void initVisibleDays() {
        Integer i = homeFragmentActivityViewModel.getNoOfVisibleDays();

        if (i != null) {
            if (i == getResources().getInteger(R.integer.VISIBLE_DAYS_1)) {
                mToolbar.getMenu().performIdentifierAction(R.id.home_fragment_day_view_menu_item, 0);
            } else if (i == getResources().getInteger(R.integer.VISIBLE_DAYS_3)) {
                mToolbar.getMenu().performIdentifierAction(R.id.home_fragment_three_day_view_menu_item, 0);
            } else if (i == getResources().getInteger(R.integer.VISIBLE_DAYS_5)) {
                mToolbar.getMenu().performIdentifierAction(R.id.home_fragment_five_day_view_menu_item, 0);
            }
        }
    }

    /**
     * Get the firstVisibleDay saved in the ViewModel and set the WeekView widget to it
     */
    private void goToTime() {
        Calendar firstVisibleDay = homeFragmentActivityViewModel.getFirstDayVisible();
        if (firstVisibleDay != null) {
            mWeekView.goToDate(firstVisibleDay);
            mWeekView.goToHour(8);
        } else {
            goToNow();
        }
    }

    /**
     * Changes the number of visible days for the WeekView widget
     *
     * @param item Toolbar menu's item pressed
     */
    private void setNumberOfVisibleDays(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_fragment_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_1));
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
                homeFragmentActivityViewModel.setNoOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_1));
                break;
            case R.id.home_fragment_three_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_3));
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
                homeFragmentActivityViewModel.setNoOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_3));
                break;
            case R.id.home_fragment_five_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_5));
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_12_SP));
                homeFragmentActivityViewModel.setNoOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_5));
                break;
        }
        item.setChecked(true);
    }

    /**
     * Go to current day and time for the WeekView widget
     */
    private void goToNow() {
        homeFragmentActivityViewModel.setFirstDayVisible(null);
        mWeekView.goToToday();
        mWeekView.goToCurrentTime();
    }

    /**
     * Since we are only going to display the main timetable in the WeekView widget, the will get only it from Room
     */
    private void getMainTimetableClasses() {
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

        this.eventList.clear();

        sqlViewModel.getTimetableCourseEvents(timetableEntity.getId()).observe(this, new Observer<List<CourseEventEntity>>() {
            @Override
            public void onChanged(List<CourseEventEntity> courseEventEntityList) {
                saveCourseEventEntityList(courseEventEntityList);
                mWeekView.notifyDataSetChanged();
            }
        });

        sqlViewModel.getTimetableExamEvents(timetableEntity.getId()).observe(this, new Observer<List<ExamEventEntity>>() {
            @Override
            public void onChanged(List<ExamEventEntity> examEventEntityList) {
                saveExamEventEntityList(examEventEntityList);
                mWeekView.notifyDataSetChanged();
            }
        });
    }

    /**
     * Takes in a list of CourseEventEntity from Room and converts them into Event for display in the WeekView widget
     *
     * @param courseEventEntityList List of CourseEventEntity from Room
     */
    private void saveCourseEventEntityList(List<CourseEventEntity> courseEventEntityList) {
        this.eventList.addAll(WeekViewConverter.saveCourseEventEntityList(courseEventEntityList));
    }

    /**
     * Takes in a list of ExamEventEntity from Room and converts them into Event for display in the WeekView widget
     *
     * @param examEventEntityList List of ExamEventEntity from Room
     */
    private void saveExamEventEntityList(List<ExamEventEntity> examEventEntityList) {
        this.eventList.addAll(WeekViewConverter.saveExamEventEntityList(examEventEntityList));
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
        intent.putExtra(getString(R.string.SECOND_ACTIVITY_INTENT_KEY), intentString);
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

    /**
     * WeekView widget horizontal scroll listener -- here we save the first visible date to the ViewModel. This is so that when the
     * user returns to this HomeFragment, he does not have to scroll to the date again!
     *
     * @param calendar  Calendar for the first day visible
     * @param calendar1 Calendar for the last day visible?
     */
    @Override
    public void onFirstVisibleDayChanged(@NotNull Calendar calendar, @org.jetbrains.annotations.Nullable Calendar calendar1) {
        Calendar now = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            goToNow();
        } else {
            homeFragmentActivityViewModel.setFirstDayVisible(calendar);
        }
    }
}