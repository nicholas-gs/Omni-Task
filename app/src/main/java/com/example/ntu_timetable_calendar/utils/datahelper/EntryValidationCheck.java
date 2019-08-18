package com.example.ntu_timetable_calendar.utils.datahelper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Carry out validation check for the title and description entered by the user
 */
public class EntryValidationCheck {

    private Context context;
    private TextInputLayout titleInputLayout;
    private TextInputLayout descriptionInputLayout;


    public EntryValidationCheck(@NonNull Context context, @NonNull TextInputLayout titleInputLayout, @NonNull TextInputLayout descriptionInputLayout) {
        this.context = context;
        this.titleInputLayout = titleInputLayout;
        this.descriptionInputLayout = descriptionInputLayout;
    }

    public boolean validate(String mTitle, String mDescription) {

        titleInputLayout.setErrorEnabled(false);
        descriptionInputLayout.setErrorEnabled(false);

        boolean validTitle = true;
        boolean validDescription = true;

        if (mTitle.length() == 0) {
            titleInputLayout.setError(context.getString(R.string.title_is_empty));
            validTitle = false;
        }

        if (mDescription.length() == 0) {
            descriptionInputLayout.setError(context.getString(R.string.description_is_empty));
            validDescription = false;
        }

        return (validTitle && validDescription);
    }

}