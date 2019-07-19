package com.example.ntu_timetable_calendar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.RVAdapters.SavedTimetablesRVAdapter;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.example.ntu_timetable_calendar.ViewModels.SavedTimetableActivityViewModel;

import java.util.List;
import java.util.Objects;

public class SavedTimetablesFragment extends Fragment implements SavedTimetablesRVAdapter.RecyclerViewItemClickListener {

    // Views
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    private SQLViewModel sqlViewModel;
    private SavedTimetablesRVAdapter mAdapter;

    // ViewModel
    private SavedTimetableActivityViewModel savedTimetableActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_timetables_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initRecyclerView();
        initSQLViewModel();
        initActivityViewModel();
    }

    private void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.saved_timetable_fragment_recyclerview);
        mToolbar = view.findViewById(R.id.saved_timetable_fragment_toolbar);

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_back_white_24dp));

        // Close fragment
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
    }

    private void initSQLViewModel() {
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        sqlViewModel.getAllTimetables().observe(this, new Observer<List<TimetableEntity>>() {
            @Override
            public void onChanged(List<TimetableEntity> timetableEntities) {
                mAdapter.passData(timetableEntities);
            }
        });
    }

    /**
     * Initialise the SavedTimetableActivityViewModel that is used to pass data between this fragment and the TimetableDetailFragment/Prevent
     * crashes with orientation changes
     */
    private void initActivityViewModel(){
        savedTimetableActivityViewModel = ViewModelProviders.of(requireActivity()).get(SavedTimetableActivityViewModel.class);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);

        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new SavedTimetablesRVAdapter(requireContext());
        mAdapter.setRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void itemListener(TimetableEntity timetableEntity) {
        savedTimetableActivityViewModel.setTimetableEntity(timetableEntity);
        FragmentTransaction fragmentTransaction =  Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_up, R.anim.fragment_slide_down, R.anim.fragment_slide_up,
                R.anim.fragment_slide_down);
        fragmentTransaction.add(R.id.second_activity_fragment_container,
                new TimetableDetailFragment(),
                "saved_timetables_fragment").addToBackStack(null).commit();
    }
}
