package com.example.habittracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        // Get passed bundle from AddRemoveHabitActivity
        Bundle b = this.getArguments();
        assert b != null;   // Ensure bundle has been passed
        long habitStartDate = b.getLong("habitStartDate");

        // Initialize datePicker to habitStartDate
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(habitStartDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}