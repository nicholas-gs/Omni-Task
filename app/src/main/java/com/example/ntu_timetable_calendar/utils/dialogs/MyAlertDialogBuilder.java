package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.ntu_timetable_calendar.R;

/**
 * My helper class that uses the builder pattern to return an AlertDialog based on customisation.
 * The purpose is to abstract out all the duplicate code since multiple similar/identical AlertDialogs
 * are used by different fragments.
 */
public class MyAlertDialogBuilder {

    private Context context;
    private String title, message, positiveButtonText, negativeButtonText, neutralButtonText;
    private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked, neutralButtonClicked, singleItemClickedListener;
    private DialogInterface.OnMultiChoiceClickListener multiItemClickedListener;
    private CharSequence[] items;
    private boolean[] checkedItems;
    private int checkedItem;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    // Private Constructor
    private MyAlertDialogBuilder(Builder builder) {
        initVariable(builder);
    }

    private void initVariable(Builder builder) {
        this.context = builder.context;
        this.title = builder.title;
        this.message = builder.message;
        this.positiveButtonText = builder.positiveButtonText;
        this.negativeButtonText = builder.negativeButtonText;
        this.neutralButtonText = builder.neutralButtonText;
        this.positiveButtonClicked = builder.positiveButtonClicked;
        this.negativeButtonClicked = builder.negativeButtonClicked;
        this.neutralButtonClicked = builder.neutralButtonClicked;
        this.singleItemClickedListener = builder.singleItemClickedListener;
        this.items = builder.items;
        this.checkedItem = builder.checkedItem;
        this.multiItemClickedListener = builder.multiItemClickedListener;
        this.checkedItems = builder.checkedItems;
    }

    private AlertDialog initialise() {
        builder = new AlertDialog.Builder(context);
        initTitle();
        initMessage();
        initPositiveButton();
        initNegativeButton();
        initNeutralButton();
        initSingleChoiceItems();
        initMultiChoiceItems();
        alertDialog = builder.create();
        initButtonColor(this.positiveButtonClicked, this.negativeButtonClicked, this.neutralButtonClicked);
        return alertDialog;
    }

    private void initTitle() {
        if (this.title != null) {
            builder.setTitle(this.title);
        }
    }

    private void initMessage() {
        if (this.message != null) {
            builder.setMessage(this.message);
        }
    }

    private void initPositiveButton() {
        if (this.positiveButtonText != null && this.positiveButtonClicked != null) {
            builder.setPositiveButton(this.positiveButtonText, this.positiveButtonClicked);
        }
    }

    private void initNegativeButton() {
        if (this.negativeButtonText != null && this.negativeButtonClicked != null) {
            builder.setNegativeButton(this.negativeButtonText, this.negativeButtonClicked);
        }
    }

    private void initNeutralButton() {
        if (this.neutralButtonText != null && this.neutralButtonClicked != null) {
            builder.setNeutralButton(this.neutralButtonText, this.neutralButtonClicked);
        }
    }

    private void initSingleChoiceItems() {
        if (this.singleItemClickedListener != null) {
            builder.setSingleChoiceItems(this.items, this.checkedItem, this.singleItemClickedListener);
        }
    }

    private void initMultiChoiceItems() {
        if (this.multiItemClickedListener != null) {
            builder.setMultiChoiceItems(this.items, this.checkedItems, this.multiItemClickedListener);
        }
    }

    private void initButtonColor(final DialogInterface.OnClickListener positiveButtonClicked,
                                 final DialogInterface.OnClickListener negativeButtonClicked,
                                 final DialogInterface.OnClickListener neutralButtonClicked) {
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (positiveButtonClicked != null) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.background_light));
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,
                            R.color.colorPrimaryDark));
                }
                if (negativeButtonClicked != null) {
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.background_light));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,
                            R.color.colorPrimaryDark));
                }
                if (neutralButtonClicked != null) {
                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.background_light));
                    alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context,
                            R.color.colorPrimaryDark));
                }
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Builder {

        private Context context;
        private String title, message, positiveButtonText, negativeButtonText, neutralButtonText;
        private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked, neutralButtonClicked,
                singleItemClickedListener;
        private DialogInterface.OnMultiChoiceClickListener multiItemClickedListener;
        private CharSequence[] items;
        private int checkedItem;
        private boolean[] checkedItems;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        Builder setMessage(@NonNull String message) {
            this.message = message;
            return this;
        }

        Builder setPositiveButtonListener(@NonNull DialogInterface.OnClickListener positiveButtonClicked,
                                          @NonNull String positiveButtonText) {
            this.positiveButtonClicked = positiveButtonClicked;
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        Builder setNegativeButtonListener(@NonNull DialogInterface.OnClickListener negativeButtonClicked,
                                          @NonNull String negativeButtonText) {
            this.negativeButtonClicked = negativeButtonClicked;
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        Builder setNeutralButtonListener(@NonNull DialogInterface.OnClickListener neutralButtonClicked,
                                         @NonNull String neutralButtonText) {
            this.neutralButtonClicked = neutralButtonClicked;
            this.neutralButtonText = neutralButtonText;
            return this;
        }

        Builder setSingleChoiceItems(@NonNull String[] items, int checkedItem, @NonNull DialogInterface.OnClickListener listener) {
            this.items = items;
            this.checkedItem = checkedItem;
            this.singleItemClickedListener = listener;
            return this;
        }


        Builder setMultiChoiceItems(@NonNull String[] items, boolean[] checkedItems, @NonNull DialogInterface.OnMultiChoiceClickListener listener) {
            this.items = items;
            this.checkedItems = checkedItems;
            this.multiItemClickedListener = listener;
            return this;
        }

        public AlertDialog build() {
            return new MyAlertDialogBuilder(this).initialise();
        }
    }

}