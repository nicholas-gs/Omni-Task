package com.example.ntu_timetable_calendar.Fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.alamkanak.weekview.EventClickListener;
import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.Helper.StringHelper;
import com.example.ntu_timetable_calendar.Helper.WeekViewParser;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.example.ntu_timetable_calendar.ViewModels.TasksFragmentViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ChooseClassFragment extends Fragment implements MonthChangeListener<Event>, EventClickListener<Event> {

    // Views
    private Toolbar mToolbar;
    private TextView titleTV, timingTV;
    private WeekView<Event> mWeekView;

    // We store the events we want to display in the WeekView widget here
    private List<WeekViewDisplayable<Event>> eventList = new ArrayList<>();

    // ViewModels
    private SQLViewModel sqlViewModel;
    private TasksFragmentViewModel tasksFragmentViewModel;

    // Variables
    private Integer chosenClassId;
    private String chosenClassTitle, chosenClassTiming;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_class_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initToolbar();
        initViewModels();
        getMainTimetable();
    }

    private void initViews(View view) {
        mToolbar = view.findViewById(R.id.choose_class_fragment_toolbar);
        titleTV = view.findViewById(R.id.choose_class_fragment_class_title);
        timingTV = view.findViewById(R.id.choose_class_fragment_class_time);
        mWeekView = view.findViewById(R.id.choose_class_fragment_weekview);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_close_white_24dp));
        // Close fragment
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.choose_class_fragment_save_menu_item:
                        saveInput();
                        break;
                    case R.id.choose_class_fragment_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.choose_class_fragment_three_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                    case R.id.choose_class_fragment_five_day_view_menu_item:
                        setNumberOfVisibleDays(item);
                        break;
                }
                return true;
            }
        });
    }

    private void initViewModels() {
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        tasksFragmentViewModel = ViewModelProviders.of(requireActivity()).get(TasksFragmentViewModel.class);

        tasksFragmentViewModel.getChosenClassId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                // Save variables to local fields
                chosenClassId = integer;
                chosenClassTitle = tasksFragmentViewModel.getChosenClassTitle();
                chosenClassTiming = tasksFragmentViewModel.getChosenClassTiming();
                // Update the 2 text views
                updateClassTextViews();
            }
        });
    }

    /**
     * Update the two text views according to the current title and timing strings
     */
    private void updateClassTextViews() {
        if (chosenClassId != null && chosenClassId >= 0 && chosenClassTitle != null && chosenClassTiming != null) {
            titleTV.setText(chosenClassTitle.trim());
            timingTV.setText(chosenClassTiming.trim());
        } else {
            titleTV.setText(getString(R.string.no_class_chosen));
        }
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
        this.eventList.clear();

        sqlViewModel.getTimetableCourseEvents(timetableEntity.getId()).observe(this, new Observer<List<CourseEventEntity>>() {
            @Override
            public void onChanged(List<CourseEventEntity> courseEventEntityList) {
                saveCourseEventEntityList(courseEventEntityList);
                mWeekView.notifyDataSetChanged();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Changes the number of visible days for the WeekView widget
     *
     * @param item Toolbar menu's item pressed
     */
    private void setNumberOfVisibleDays(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_class_fragment_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_1));
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
                break;
            case R.id.choose_class_fragment_three_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_3));
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_14_SP));
                break;
            case R.id.choose_class_fragment_five_day_view_menu_item:
                mWeekView.setNumberOfVisibleDays(getResources().getInteger(R.integer.VISIBLE_DAYS_5));
                mWeekView.setHeaderRowTextSize(getResources().getInteger(R.integer.HEADER_12_SP));
                break;
        }
        item.setChecked(true);
    }

    /**
     * Pass the chosen class/event id back to the underlying fragment using the TaskFragmentViewModel
     */
    private void saveInput() {

        if (chosenClassId != null) {
            tasksFragmentViewModel.setChosenClassId(chosenClassId);
            tasksFragmentViewModel.setChosenClassTitle(chosenClassTitle.trim());
            tasksFragmentViewModel.setChosenClassTiming(chosenClassTiming.trim());
        } else {
            tasksFragmentViewModel.setChosenClassId(-1);
            tasksFragmentViewModel.setChosenClassTitle(null);
            tasksFragmentViewModel.setChosenClassTiming(null);
        }

        requireActivity().onBackPressed();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Takes in a list of CourseEventEntity from Room and converts them into Event for display in the WeekView widget
     *
     * @param courseEventEntityList List of CourseEventEntity from Room
     */
    private void saveCourseEventEntityList(List<CourseEventEntity> courseEventEntityList) {
        this.eventList.addAll(WeekViewParser.saveCourseEventEntityList(courseEventEntityList));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public void onEventClick(Event event, @NotNull RectF rectF) {
        // Save the id of the class event in the class variable
        this.chosenClassId = (int) event.getId();
        // Set the title
        this.chosenClassTitle = event.getTitle();
        // Set the time
        this.chosenClassTiming = StringHelper.ClassTimingParser(event.getStartTime(), event.getEndTime());
        // Update the 2 text views
        updateClassTextViews();
    }
}