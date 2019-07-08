package com.example.ntu_timetable_calendar.BottomSheets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.Helper.BottomNavColor;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.RVAdapters.PlanFragmentBottomSheetRVAdapter;
import com.example.ntu_timetable_calendar.ViewModels.PlanFragmentActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanFragmentBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener,
        PlanFragmentBottomSheetRVAdapter.ItemSpinnerInterface {

    // Views
    private ImageView closeBtn;
    private MaterialButton planBtn;
    private RecyclerView recyclerView;

    // Variables
    private List<Course> courseList;

    // A HashMap that stores all the changes from the original index selection sent by the plan fragment. This new
    // copy is sent back to the plan fragment only through the submit button click listener. If the bottom sheet's plan close
    // button is pressed, all changes are discarded as the callback method is not triggered.
    private Map<String, String> newIndexesSel = new HashMap<>();

    private PlanFragmentActivityViewModel planFragmentActivityViewModel;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface PlanFragmentBottomSheetInterface {
        // Callback method for when submit button is clicked
        void onPlanButtonClicked(Map<String, String> newIndexesSel);
    }

    private PlanFragmentBottomSheetInterface planFragmentBottomSheetInterface;

    public void setPlanFragmentBottomSheetInterface(PlanFragmentBottomSheetInterface planFragmentBottomSheetInterface) {
        this.planFragmentBottomSheetInterface = planFragmentBottomSheetInterface;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_fragment_bottomsheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModels();
        initRecyclerView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Set Bottom Nav Color to white instead of faded out
        Activity activity = getActivity();
        if (activity != null) {
            BottomNavColor.setWhiteNavigationBar(dialog, getActivity());
        }

        return dialog;
    }

    private void initViews(View view) {
        closeBtn = view.findViewById(R.id.plan_fragment_bs_close_btn);
        closeBtn.setOnClickListener(this);
        planBtn = view.findViewById(R.id.plan_fragment_bs_plan_btn);
        planBtn.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.plan_fragment_bs_rv);
    }

    /**
     * We get passed the data to show in the bottom sheet from the PlanFragment through the PlanFragmentActivityViewModel
     */
    private void initViewModels() {
        planFragmentActivityViewModel = ViewModelProviders.of(getActivity()).get(PlanFragmentActivityViewModel.class);
        this.courseList = planFragmentActivityViewModel.getQueriedCourseList();
        // Make a copy of the original indexesSel in internalIndexesSel variable
        this.newIndexesSel.putAll(planFragmentActivityViewModel.getIndexesSel());
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PlanFragmentBottomSheetRVAdapter mAdapter = new PlanFragmentBottomSheetRVAdapter(courseList, getContext(), newIndexesSel);
        mAdapter.setItemSpinnerInterface(this);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Callback method for RecyclerView item spinner item selected, defined in PlanFragmentBottomSheetRVAdapter
     *
     * @param courseCode
     * @param newIndexSelection
     */
    @Override
    public void spinnerItemSelected(String courseCode, String newIndexSelection) {
        newIndexesSel.put(courseCode, newIndexSelection);
    }

    /**
     * Functionality for when the submit button is pressed
     */
    private void submitIndexSelections() {
        if (planFragmentBottomSheetInterface != null) {
            planFragmentBottomSheetInterface.onPlanButtonClicked(this.newIndexesSel);
        }
        dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_fragment_bs_close_btn:
                dismiss();
                break;
            case R.id.plan_fragment_bs_plan_btn:
                submitIndexSelections();
                break;
        }
    }

}
