package com.example.ntu_timetable_calendar.Fragments;

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

import com.example.ntu_timetable_calendar.Entity.ProjectEntity;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.RVAdapters.ProjectsRVAdapter;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;

import java.util.List;

/**
 * This fragment displays all the ProjectEntities
 */
public class TaskFragmentFrag4 extends Fragment implements ProjectsRVAdapter.onItemClickedListener {

    // Views
    private RecyclerView mRecyclerView;

    private ProjectsRVAdapter mAdapter;

    // ViewModels
    private SQLViewModel sqlViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment_frag4_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        initViewModels();
    }

    private void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.tasks_frag4_recyclerview);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        this.mAdapter = new ProjectsRVAdapter(requireContext());
        this.mAdapter.setOnItemClickedListener(this);
        mRecyclerView.setAdapter(this.mAdapter);
    }

    private void initViewModels() {
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);

        sqlViewModel.getAllProjects().observe(this, new Observer<List<ProjectEntity>>() {
            @Override
            public void onChanged(List<ProjectEntity> projectEntities) {
                if (projectEntities != null) {
                    passData(projectEntities);
                }
            }
        });
    }

    private void passData(List<ProjectEntity> projectEntities) {
        if (projectEntities != null) {
            this.mAdapter.submitList(projectEntities);
        }
    }

    /**
     * Callback method for ProjectsRVAdapter item clicked listener
     *
     * @param projectEntity ProjectEntity clicked
     * @param position      Index of item clicked
     */
    @Override
    public void onItemClicked(ProjectEntity projectEntity, int position) {

    }

}