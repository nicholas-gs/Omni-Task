package com.example.ntu_timetable_calendar.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.EventClickListener;
import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.ntu_timetable_calendar.Converters.CourseEntityToEventConverter;
import com.example.ntu_timetable_calendar.Dialogs.EditTimetableDialog;
import com.example.ntu_timetable_calendar.Dialogs.TimetableEventDetailDialog;
import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.example.ntu_timetable_calendar.ViewModels.SavedTimetableActivityViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class TimetableDetailFragment extends Fragment implements MonthChangeListener<Event>, DateTimeInterpreter,
        EditTimetableDialog.EditTimetableDialogInterface, EventClickListener<Event> {

    // Views
    private Toolbar mToolbar;
    private WeekView<Event> mWeekView;

    private TimetableEntity timetableEntity;
    private SQLViewModel sqlViewModel;
    private SavedTimetableActivityViewModel savedTimetableActivityViewModel;

    // We store the events we want to display in the weekview widget here
    private List<WeekViewDisplayable<Event>> eventList = new ArrayList<>();

    public TimetableDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timetable_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupToolbar();
        setupViewModel();
    }

    private void initViews(View view) {
        mWeekView = view.findViewById(R.id.timetable_detail_weekview);
        mToolbar = view.findViewById(R.id.timetable_detail_toolbar);

        mWeekView.setMonthChangeListener(this);
        mWeekView.setDateTimeInterpreter(this);
        mWeekView.setOnEventClickListener(this);
    }

    private void setupToolbar() {
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_back_white_24dp));

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
                if (item.getItemId() == R.id.timetable_detail_toolbar_menu_delete) {
                    displayDeleteDialog();
                } else if (item.getItemId() == R.id.timetable_detail_toolbar_menu_edit) {
                    displayEditDialog();
                }
                return true;
            }
        });
    }

    private void setupViewModel() {
        savedTimetableActivityViewModel = ViewModelProviders.of(requireActivity()).get(SavedTimetableActivityViewModel.class);
        this.timetableEntity = savedTimetableActivityViewModel.getTimetableEntity();
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        sqlViewModel.getTimetableCourses(timetableEntity.getId()).observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                if (courseEntities != null && courseEntities.size() != 0) {
                    mWeekView.goToHour(8);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    mWeekView.goToDate(calendar);

                    displayTimetable(courseEntities);
                }
            }
        });
    }

    /**
     * AlertDialog that confirms if the user wants to delete the timetable
     */
    private void displayDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to delete this timetable?");
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sqlViewModel.deleteTimetable(timetableEntity);
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(requireContext(),
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryDark));
            }
        });
        alertDialog.show();
    }

    /**
     * AlertDialog that allows the user to edit the timetable's information
     */
    private void displayEditDialog() {
        EditTimetableDialog editTimetableDialog = new EditTimetableDialog(this.timetableEntity);
        editTimetableDialog.setEditTimetableDialogInterface(this);
        editTimetableDialog.show(getChildFragmentManager(), "save_timetable_dialog");
    }


    private void displayTimetable(List<CourseEntity> courseEntities) {

        // Set the size factor of the WeekView widget (simulate pinching the widget). The user can still adjust the size/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        mWeekView.setHourHeight(height / 12f);

        this.eventList.clear();
        this.eventList.addAll(CourseEntityToEventConverter.convertEntitiesToEvents(courseEntities));

        mWeekView.notifyDataSetChanged();
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

    /**
     * This method affects how the date/day (horizontal axis) are displayed. I override the
     * original implementation in order to remove the date as I only want to show day.
     *
     * @param calendar Calendar
     * @return String to display
     */
    @NotNull
    @Override
    public String interpretDate(@NotNull Calendar calendar) {
        SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        return weekdayNameFormat.format(calendar.getTime());
    }

    /**
     * This method affects how the time (vertical axis) are displayed. I override the
     * original implementation in order to show time in 24h format.
     */
    @NotNull
    @Override
    public String interpretTime(int i) {
        if (i < 10) {
            return "0" + i + ":00";
        } else {
            return i + ":00";
        }
    }

    /**
     * Edit timetable dialog save button click listener
     */
    @Override
    public void onSaveClick() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toasty.success(requireContext(), getString(R.string.changes_saved), Toasty.LENGTH_SHORT).show();
            }
        }, 200);
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
}
