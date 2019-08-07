package com.example.ntu_timetable_calendar.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class EditTimetableDialog extends DialogFragment implements View.OnClickListener {

    private EditText mTitleEditText, mDescriptionEditText;
    private CheckBox mIsMainTimetableCheckbox;
    private MaterialButton mSaveButton, mCancelButton;

    private TimetableEntity timetableEntity;

    // Variables to send back to TimetableDetailFragment through interface
    private String mTitle, mDescription;
    private boolean mIsMainTimetable;

    public EditTimetableDialog() {
    }

    public EditTimetableDialog(TimetableEntity timetableEntity) {
        this.timetableEntity = timetableEntity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface EditTimetableDialogInterface {
        void onSaveClick(String mTitle, String mDescription, boolean mIsMainTimetable);
    }

    private EditTimetableDialogInterface mListener;

    public void setEditTimetableDialogInterface(EditTimetableDialogInterface mListener) {
        this.mListener = mListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.timetable_detail_edit_dialog, null);

        builder.setView(view);
        initViews(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Lock the orientation of the UI
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else
            Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews(View view) {
        mTitleEditText = view.findViewById(R.id.timetable_detail_editdialog_title);
        mDescriptionEditText = view.findViewById(R.id.timetable_detail_editdialog_description);
        mIsMainTimetableCheckbox = view.findViewById(R.id.timetable_detail_editdialog_checkbox);
        mSaveButton = view.findViewById(R.id.timetable_detail_editdialog_save_button);
        mCancelButton = view.findViewById(R.id.timetable_detail_editdialog_cancel_button);

        if (timetableEntity != null) {
            mTitleEditText.setText(timetableEntity.getName());
            mDescriptionEditText.setText(timetableEntity.getDescription());
            mIsMainTimetableCheckbox.setChecked(timetableEntity.getIsMainTimetable());
        }

        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.timetable_detail_editdialog_cancel_button:
                dismiss();
                break;
            case R.id.timetable_detail_editdialog_save_button:
                saveEditedTimetable();
                break;
        }
    }

    private void saveEditedTimetable() {
        // Title
        mTitle = mTitleEditText.getText().toString().trim();
        if (mTitle.length() == 0) {
            mTitle = getString(R.string.untitled_timetable);
        }
        // Description
        mDescription = mDescriptionEditText.getText().toString().trim();
        if (mDescription.length() == 0) {
            mDescription = getString(R.string.no_description);
        }
        // IsMainTimetable
        mIsMainTimetable = mIsMainTimetableCheckbox.isChecked();

        if (mListener != null) {
            mListener.onSaveClick(mTitle, mDescription, mIsMainTimetable);
        }

        dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unlock orientation change
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}