package com.example.ntu_timetable_calendar.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.utils.viewformatters.PriorityTextViewFormatter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddNewProjectFragment extends Fragment {

    // Views
    private AppBarLayout mAppbarLayout;
    private Toolbar mToolbar;
    private TextInputLayout mTitleInputLayout, mDescriptionInputLayout;
    private TextInputEditText mTitleEditText, mDescriptionEditText;
    private TextView mChoosePriorityTextView;

    // Variables
    private String title, description;
    private int priority;

    // Util
    private PriorityTextViewFormatter priorityTextViewFormatter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Handles what happens when activity onBackPressed is called.
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                closeFragmentDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_project_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initToolbar();
        initUtil();
    }

    private void initView(View view) {
        mAppbarLayout = view.findViewById(R.id.add_new_project_appbarlayout);
        mToolbar = view.findViewById(R.id.add_new_project_toolbar);
        mTitleInputLayout = view.findViewById(R.id.add_new_project_title_textinputlayout);
        mDescriptionInputLayout = view.findViewById(R.id.add_new_project_description_textinputlayout);
        mTitleEditText = view.findViewById(R.id.add_new_project_title_edittext);
        mDescriptionEditText = view.findViewById(R.id.add_new_project_description_edittext);
        mChoosePriorityTextView = view.findViewById(R.id.add_new_project_add_priority_textview);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_close_white_24dp));

        // Close fragment
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        mToolbar.inflateMenu(R.menu.add_new_project_toolbar_menu);

        // Functionality for the save button clicked
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_new_project_save) {
                    //saveTaskDialog();
                }
                return true;
            }
        });
    }

    private void initUtil() {
        priorityTextViewFormatter = new PriorityTextViewFormatter(requireContext(), mChoosePriorityTextView);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void updatePriorityTextView() {
        priorityTextViewFormatter.format(priority);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void closeFragmentDialog() {
        Context context = requireContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.task_detail_fragment_close_dialog_message));
        builder.setNegativeButton(context.getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(context.getString(R.string.discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requireActivity().finish();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

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
}
