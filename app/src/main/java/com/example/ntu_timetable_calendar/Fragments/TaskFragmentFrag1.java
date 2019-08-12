package com.example.ntu_timetable_calendar.Fragments;

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

import com.example.ntu_timetable_calendar.Entity.TaskEntity;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.RVAdapters.TaskFragmentRVAdapter;
import com.example.ntu_timetable_calendar.SecondActivity;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;

import java.util.Calendar;
import java.util.List;

/**
 * Fragment showing the tasks for the next 7 days (NEXT 7 DAYS)
 */
public class TaskFragmentFrag1 extends Fragment implements TaskFragmentRVAdapter.onItemClickedListener {

    // Views
    private RecyclerView mRecyclerView;

    private TaskFragmentRVAdapter mAdapter;

    // ViewModels
    private SQLViewModel sqlViewModel;

    // Variables
    private Calendar nowCalendar;
    private Calendar deadlineCalendar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment_frag1_layout, container, false);
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
        mRecyclerView = view.findViewById(R.id.tasks_frag1_recyclerview);
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
        this.nowCalendar = Calendar.getInstance();
        this.deadlineCalendar = (Calendar) nowCalendar.clone();
        this.deadlineCalendar.add(Calendar.DAY_OF_YEAR, 7);
    }

    private void initViewModels() {
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);

        sqlViewModel.getTasksWithinTime(this.nowCalendar.getTimeInMillis(), this.deadlineCalendar.getTimeInMillis())
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