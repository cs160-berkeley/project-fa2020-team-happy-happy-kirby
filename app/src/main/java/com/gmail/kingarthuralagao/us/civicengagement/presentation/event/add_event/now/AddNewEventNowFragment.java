package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.now;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddNewEventNowFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, CausesDialogFragment.ICausesSetListener {


    public static AddNewEventNowFragment newInstance() {
        AddNewEventNowFragment fragment = new AddNewEventNowFragment();
        return fragment;
    }

    private static IncludeAddEventHappeningNowBinding binding;
    private static final int AUTOCOMPLETE_LOCATION_REQUEST_CODE = 100;
    private static final String TAG = "AddNewEventNowFragment";

    // Params for an Event object
    private static String name;
    private static String dateStart;
    private static String dateEnd;
    private static String timeStart;
    private static String city;
    private static String timeEnd;
    private static String description;
    private static String location;
    private static String timeZone;
    private static List<String> causes = new ArrayList<>();
    private static Map<String, Boolean> accessibilities;
    private static Place place;
    private static TimeZone timeZoneResponse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IncludeAddEventHappeningNowBinding.inflate(inflater, container, false);
        setUpViews();
        setUpEvents();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                this.place = place;
                String address = place.getAddress();
                binding.eventLocationEt.setText(address);
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
        this.dateEnd = (month + 1) + "/" + day + "/" + year;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String amOrPm = "AM";
        String formattedMinute = String.valueOf(minute);

        if (hour >= 12) {
            amOrPm = "PM";
            hour = Utils.convertToStandardTime(hour); // Converts 24-hour based clock to 12-hour based
        }

        if (minute < 10) {
            formattedMinute = "0" + formattedMinute;
        }

        binding.eventTimeEndEt.setText(hour + ":" + formattedMinute + " " + amOrPm);
        this.timeEnd = hour + ":" + formattedMinute;
    }

    @Override
    public void onCausesSet(List<String> checkedCauses) {
        binding.eventCausesEt.setText(checkedCauses.toString().substring(1, checkedCauses.toString().length() - 1));

        this.causes.clear();
        this.causes.addAll(checkedCauses);
    }

    private void setUpViews() {
        binding.eventCausesEt.setFocusable(false);
        binding.eventLocationEt.setFocusable(false);
        binding.eventTimeEndEt.setFocusable(false);
        binding.eventDateEndEt.setFocusable(false);

        /*
        Chip chip = new Chip(requireContext());
        chip.setText("Hello");
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setClickable(false);*/


        /*
        int[] DEFAULT = new int[android.R.attr.state_pressed];

        ChipDrawable chipDrawable = ChipDrawable.createFromResource(getContext(), R.xml.standalone_chip);
        chipDrawable.setText("Hello");
        chipDrawable.setBounds(0, 0, chipDrawable.getIntrinsicWidth(), chipDrawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(chipDrawable);

        ChipDrawable chipDrawable1 = ChipDrawable.createFromResource(getContext(), R.xml.standalone_chip);
        chipDrawable1.setText("Hi");
        chipDrawable.setBounds(0, 0, chipDrawable1.getIntrinsicWidth(), chipDrawable1.getIntrinsicHeight());
        ImageSpan imageSpan1 = new ImageSpan(chipDrawable1);

        binding.eventCausesEt.setText("HelloHi");
        Editable spannableString = binding.eventCausesEt.getText();
        spannableString.setSpan(imageSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(imageSpan1, 6, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        chipDrawable.setCloseIconState(DEFAULT);
        binding.eventCausesEt.setText(spannableString);


        Log.i(getClass().getSimpleName(), chipDrawable.getCloseIconState().toString());

        causesChipGroup.addView();
        causesChipGroup.addView(chip);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpEvents() {
        // Present user with a calendar to select a date from.
        binding.eventDateEndEt.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        binding.eventLocationEt.setOnClickListener( view -> {
            initializeLocationSearch();
        });

        binding.eventTimeEndEt.setOnClickListener( view -> {
            showTimePickerDialog();
        });

        binding.eventCausesEt.setOnClickListener(view -> {
            showCausesDialog();
        });
    }

    private void showCausesDialog() {
        CausesDialogFragment causesDialogFragment = CausesDialogFragment.newInstance((ArrayList) causes);
        causesDialogFragment.show(getChildFragmentManager(), null);
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

    public static Place getPlace() {
        return place;
    }

    public static Event getEvent(TimeZone timeZone) {
        EventBuilder eventBuilder = new EventBuilder();

        eventBuilder
                .withName(binding.eventNameEt.getText().toString())
                .withDateStart("11/27/2020", "22:30")
                .withDateEnd(dateEnd, timeEnd)
                .withTimeStart("22:30")
                .withTimeEnd(timeEnd)
                .withCity("San Francisco")
                .withDescription(binding.eventNotesEt.getText().toString())
                .withLocation(place.getAddress())
                .withTimeZone(timeZone)
                .withCheckIns()
                .withCauses(causes)
                .withAccessibilities(accessibilities)
                .withEventID()
                .build();

        Log.i(TAG, eventBuilder.toString());
        return eventBuilder.getEventDTO();
    }
}
