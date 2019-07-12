package com.example.ntu_timetable_calendar.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ntu_timetable_calendar.R;
import com.google.android.material.button.MaterialButton;

public class SaveTimetableDialog extends DialogFragment implements View.OnClickListener {

    private EditText mEdittext;
    private MaterialButton saveBtn, cancelBtn;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Interface callback method for dialog button pressed
     */
    public interface DialogCallbackInterface {
        void saveButtonPressed(String timetableName);

        void cancelButtonPressed();
    }

    private DialogCallbackInterface dialogCallbackInterface;

    public void setDialogInterface(DialogCallbackInterface dialogCallbackInterface) {
        this.dialogCallbackInterface = dialogCallbackInterface;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.new_timetable_dialog_layout, null);

        builder.setView(view);

        mEdittext = view.findViewById(R.id.plan_fragment_savetimetable_dialog_edittext);
        saveBtn = view.findViewById(R.id.plan_fragment_savetimetable_dialog_save_btn);
        saveBtn.setOnClickListener(this);
        cancelBtn = view.findViewById(R.id.plan_fragment_savetimetable_dialog_cancel_btn);
        cancelBtn.setOnClickListener(this);

        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_fragment_savetimetable_dialog_save_btn:
                saveButtonPressed();
                break;
            case R.id.plan_fragment_savetimetable_dialog_cancel_btn:
                cancelButtonPressed();
                break;
        }
    }

    private void saveButtonPressed() {
        String name = mEdittext.getText().toString().trim();
        if (name.length() == 0) {
            name = getString(R.string.untitled_timetable);
        }
        if (dialogCallbackInterface != null) {
            dialogCallbackInterface.saveButtonPressed(name);
        }
        dismiss();
    }

    private void cancelButtonPressed() {
        if (dialogCallbackInterface != null) {
            dialogCallbackInterface.cancelButtonPressed();
        }
        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dialogCallbackInterface != null) {
            dialogCallbackInterface.cancelButtonPressed();
        }
    }

}
