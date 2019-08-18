package com.example.ntu_timetable_calendar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.models.entities.TaskEntity;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.adapters.recyclerviewadapters.TaskFragmentRVAdapter;
import com.example.ntu_timetable_calendar.activities.SecondActivity;
import com.example.ntu_timetable_calendar.viewmodels.SQLViewModel;

import java.util.Calendar;
import java.util.List;

/**
 * Show tasks whose deadline is within the current month (THIS MONTH)
 */
public class TaskFragmentFrag2 extends Fragment implements TaskFragmentRVAdapter.onItemClickedListener {

    // Views
    private RecyclerView mRecyclerView;

    private TaskFragmentRVAdapter mAdapter;

    // ViewModels
    private SQLViewModel sqlViewModel;

    // Variables
    private Calendar firstDayOfMonth;
    private Calendar lastDayOfMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment_frag2_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setUpRecyclerView();
        initVariables();
        initViewModels();
    }

    private void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.tasks_frag2_recyclerview);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        this.mAdapter = new TaskFragmentRVAdapter(requireContext());
        this.mAdapter.setOnItemClickedListener(this);
        mRecyclerView.setAdapter(this.mAdapter);
    }

    private void initVariables() {
        int lastDate = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDate = Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH);

        // Calendar for first day of the current month (For example 01/08/2019 00:00:00:000)
        this.firstDayOfMonth = Calendar.getInstance();
        this.firstDayOfMonth.set(Calendar.DAY_OF_MONTH, firstDate);
        this.firstDayOfMonth.get(Calendar.DAY_OF_MONTH);
        this.firstDayOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        this.firstDayOfMonth.get(Calendar.HOUR_OF_DAY);
        this.firstDayOfMonth.set(Calendar.MINUTE, 0);
        this.firstDayOfMonth.get(Calendar.MINUTE);
        this.firstDayOfMonth.set(Calendar.SECOND, 0);
        this.firstDayOfMonth.get(Calendar.SECOND);
        this.firstDayOfMonth.set(Calendar.MILLISECOND, 0);
        this.firstDayOfMonth.get(Calendar.MILLISECOND);

        // Calendar for the last day of the current month (For example 31/08/2019 23:59:59:999)
        this.lastDayOfMonth = Calendar.getInstance();
        this.lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDate);
        this.lastDayOfMonth.get(Calendar.DAY_OF_MONTH);
        this.lastDayOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        this.lastDayOfMonth.get(Calendar.HOUR_OF_DAY);
        this.lastDayOfMonth.set(Calendar.MINUTE, 59);
        this.lastDayOfMonth.get(Calendar.MINUTE);
        this.lastDayOfMonth.set(Calendar.SECOND, 59);
        this.lastDayOfMonth.get(Calendar.SECOND);
        this.lastDayOfMonth.set(Calendar.MILLISECOND, 999);
        this.lastDayOfMonth.get(Calendar.MILLISECOND);
    }

    private void initViewModels() {
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);

        sqlViewModel.getTasksWithinTime(this.firstDayOfMonth.getTimeInMillis(), this.lastDayOfMonth.getTimeInMillis())
                .observe(this, new Observer<List<TaskEntity>>() {
                    @Override
                    public void onChanged(List<TaskEntity> taskEntities) {
                        passData(taskEntities);
                    }
                });
    }

    /**
     * Pass data to the RecyclerView's adapter for display
     *
     * @param taskEntities List of TaskEntity from SQLViewModel
     */
    private void passData(List<TaskEntity> taskEntities) {
        if (taskEntities != null) {
            this.mAdapter.submitList(taskEntities);
        }
    }

    /**
     * Callback method for RecyclerView item clicked
     *
     * @param taskEntity TaskEntity object clicked
     * @param position   Index of RecyclerView ViewHolder clicked
     */
    @Override
    public void onItemClicked(TaskEntity taskEntity, int position) {
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(getString(R.string.SECOND_ACTIVITY_INTENT_KEY), getString(R.string.TASK_DETAIL_INTENT));
        intent.putExtra(getString(R.string.TASK_ENTITY_ID), taskEntity.getId());
        startActivity(intent);
    }
}