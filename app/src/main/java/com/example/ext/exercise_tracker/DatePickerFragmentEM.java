package com.example.ext.exercise_tracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by 7435916 on 31/10/2016.
 */
public class DatePickerFragmentEM extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet2;
    private int year, month, day;

    public DatePickerFragmentEM() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate2) {
        ondateSet2 = ondate2;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet2, year, month, day);
    }
}
