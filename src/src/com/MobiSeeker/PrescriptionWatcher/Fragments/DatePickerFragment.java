package com.MobiSeeker.PrescriptionWatcher.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.MobiSeeker.PrescriptionWatcher.activities.Prescription;

public class DatePickerFragment extends DialogFragment {

    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int dayOfMonth = getArguments().getInt("dayOfMonth");

        return new DatePickerDialog(getActivity(), (Prescription)getActivity(), year, month, dayOfMonth);
    }
}
