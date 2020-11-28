package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface IOnDateSetListener {
        void onDateSet(Date date, String callerFragmentTag);
    }

    public static DatePickerFragment newInstance(String callerFragmentTag) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString("callerFrag", callerFragmentTag);
        fragment.setArguments(args);
        return fragment;
    }

    private IOnDateSetListener iOnDateSetListener;
    private String callerFragmentTag = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callerFragmentTag = getArguments().getString("callerFrag", "");
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IOnDateSetListener) {
            iOnDateSetListener = (IOnDateSetListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iOnDateSetListener = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.i(getClass().getSimpleName(), "Year is " + year) ;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month + 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date dateRepresentation = cal.getTime();

        iOnDateSetListener.onDateSet(dateRepresentation, callerFragmentTag);
        Log.i(getClass().getSimpleName(), dateRepresentation.toString());
    }
}
