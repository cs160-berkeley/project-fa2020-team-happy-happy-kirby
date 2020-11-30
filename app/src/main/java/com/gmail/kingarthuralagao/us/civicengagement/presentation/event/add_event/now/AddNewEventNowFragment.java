package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.now;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.EventBuilder;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.CausesDialogFragment;
import com.gmail.kingarthuralagao.us.civilengagement.BuildConfig;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.IncludeAddEventHappeningNowBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.ChipDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.getSystemService;

public class AddNewEventNowFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {


    public static AddNewEventNowFragment newInstance() {
        AddNewEventNowFragment fragment = new AddNewEventNowFragment();
        return fragment;
    }

    private static IncludeAddEventHappeningNowBinding binding;
    private static final int AUTOCOMPLETE_LOCATION_REQUEST_CODE = 100;
    private static final String TAG = "AddNewEventNowFragment";

    private static String dateEnd;
    private static String timeEnd;
    private static Place place;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IncludeAddEventHappeningNowBinding.inflate(inflater, container, false);
        setUpViews();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                this.place = place;
                String address = place.getAddress();
                String name = place.getName();
                binding.eventLocationEt.setText(name + "\n" + address);
                binding.eventLocationLayout.setError("");
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toasty.error(requireContext(), status.getStatusMessage(), Toast.LENGTH_LONG, true);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "Cancelled");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        binding.eventDateEndEt.setText((month + 1) + "/" + day + "/" + year);
        binding.eventDateEndLayout.setError("");
        Log.i(TAG, "" + year);
        this.dateEnd = (month + 1) + "/" + day + "/" + year;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = Utils.buildTimeString(hour, minute);

        binding.eventTimeEndEt.setText(time);
        binding.eventTimeEndLayout.setError("");
        this.timeEnd = time;
    }

    private void setUpViews() {
        binding.eventLocationEt.setFocusable(false);
        binding.eventTimeEndEt.setFocusable(false);
        binding.eventDateEndEt.setFocusable(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpEvents() {
        // Present user with a calendar to select a date from.
        binding.eventDateEndEt.setOnClickListener(view -> {
            hideKeyboard(binding.eventDateEndEt);
            showDatePickerDialog();
        });

        binding.eventLocationEt.setOnClickListener( view -> {
            hideKeyboard(binding.eventLocationEt);
            initializeLocationSearch();
        });

        binding.eventTimeEndEt.setOnClickListener( view -> {
            hideKeyboard(binding.eventTimeEndEt);
            showTimePickerDialog();
        });

        binding.lay.setOnClickListener(view -> hideKeyboard(binding.lay));

        binding.eventNameEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        binding.eventNotesEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard(view);
                }
            }
        });

        binding.eventNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.eventNameLayout.setError("");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showTimePickerDialog() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                this,
                hour,
                minutes,
                false);
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Log.i(TAG, "" + year);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(), this, year, month, day);
        datePickerDialog.show();
    }

    private void initializeLocationSearch() {
        Places.initialize(requireContext(), BuildConfig.API_KEY);
        Places.createClient(requireContext());

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_LOCATION_REQUEST_CODE);
    }

    public static String getName() {
        return binding.eventNameEt.getText().toString();
    }

    public static String getDateEnd() {
        return dateEnd;
    }

    public static String getTimeEnd() {
        return timeEnd;
    }

    public static String getDescription() {
        return binding.eventNotesEt.getText().toString();
    }

    public static Place getPlace() {
        return place;
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        binding.lay.requestFocus();
    }

    public static boolean hasEmptyField() {
        boolean hasAnEmptyField = false;

        if (binding.eventNameEt.getText().length() == 0) {
            binding.eventNameLayout.setError("Must provide an event name");
            hasAnEmptyField = true;
        }

        if (binding.eventLocationEt.getText().length() == 0) {
            binding.eventLocationLayout.setError("Must provide a location");
            hasAnEmptyField = true;
        }

        if (binding.eventDateEndEt.getText().length() == 0) {
            binding.eventDateEndLayout.setError("Must provide an end date");
            hasAnEmptyField = true;
        }

        if (binding.eventTimeEndEt.getText().length() == 0) {
            binding.eventTimeEndLayout.setError("Must provide an end time");
            hasAnEmptyField = true;
        }

        return hasAnEmptyField;
    }
}
