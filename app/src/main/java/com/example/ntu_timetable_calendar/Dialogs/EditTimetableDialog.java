package com.example.ntu_timetable_calendar.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.ViewModels.SQLViewModel;
import com.google.android.material.button.MaterialButton;

public class EditTimetableDialog extends DialogFragment implements View.OnClickListener {

    private EditText mTitleEditText, mDescriptionEditText;
    private CheckBox mIsMainTimetableCheckbox;
    private MaterialButton mSaveButton, mCancelButton;

    private TimetableEntity timetableEntity;
    private SQLViewModel sqlViewModel;

    public EditTimetableDialog() {
    }

    public EditTimetableDialog(TimetableEntity timetableEntity) {
        this.timetableEntity = timetableEntity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface EditTimetableDialogInterface {
        void onSaveClick();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
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

        String titleStr = mTitleEditText.getText().toString().trim();
        if (titleStr.length() == 0) {
            titleStr = getString(R.string.untitled_timetable);
        }

        String descriptionStr = mDescriptionEditText.getText().toString().trim();
        if (descriptionStr.length() == 0) {
            descriptionStr = getString(R.string.no_description);
        }

        timetableEntity.setName(titleStr);
        timetableEntity.setDescription(descriptionStr);
        timetableEntity.setMainTimetable(mIsMainTimetableCheckbox.isChecked());
        sqlViewModel.updateTimetable(this.timetableEntity);

        if (mListener != null) {
            mListener.onSaveClick();
        }

        dismiss();
    }
}
