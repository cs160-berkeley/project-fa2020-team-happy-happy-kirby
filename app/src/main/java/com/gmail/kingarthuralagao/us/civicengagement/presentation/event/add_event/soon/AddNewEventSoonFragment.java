package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.soon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.LocationOptionDialogFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.RangeTimePickerDialogFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.UploadLandmarkImageActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.adapter.LandmarkResultsAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.BuildConfig;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.IncludeAddEventHappeningSoonBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddNewEventSoonFragment extends Fragment implements
        RangeTimePickerDialogFragment.ITimePickerListener, LocationOptionDialogFragment.IChoiceSetListener {

    public static AddNewEventSoonFragment newInstance() {
        AddNewEventSoonFragment fragment = new AddNewEventSoonFragment();
        return fragment;
    }

    private static IncludeAddEventHappeningSoonBinding binding;
    private static final int AUTOCOMPLETE_LOCATION_REQUEST_CODE = 100;
    private static final int LANDMARK_LOCATION_REQUEST_CODE = 300;
    private static final String TAG = "AddNewEventSoonFragment";

    // Params for an Event object
    private static Long dateStart;
    private static Long dateEnd;
    private static String timeStart;
    private static String timeEnd;
    private static Place place;
    private static String goFundMeLink;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IncludeAddEventHappeningSoonBinding.inflate(inflater, container, false);
        setUpViews();
        return binding.getRoot();
    }

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
        } else if (requestCode == LANDMARK_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "In OKAY");
                LandmarkResultsAdapter.LandmarkEntity landmarkEntity =
                        (LandmarkResultsAdapter.LandmarkEntity) data.getSerializableExtra(UploadLandmarkImageActivity.LANDMARK_RESULT);
                binding.eventLocationEt.setText(landmarkEntity.getName() + "\n" + landmarkEntity.getAddress());
            } else if(resultCode == RESULT_CANCELED) {

            } else {
                Log.e(TAG, "Error");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTimeRangeSet(int startHour, int startMinute, int endHour, int endMinute) {
        Log.i(TAG, startHour + ":" + startMinute + " " + endHour + ":" + endMinute);

        String startTime = Utils.buildTimeString(startHour, startMinute);
        String endTime = Utils.buildTimeString(endHour, endMinute);

        binding.eventTimeStartEt.setText(startTime);
        binding.eventTimeStartLayout.setError("");
        binding.eventTimeEndEt.setText(endTime);
        binding.eventTimeEndLayout.setError("");

        this.timeStart = startTime;
        this.timeEnd = endTime;
    }

    @Override
    public void onLocationChoiceSet(int choice) {
        if (choice == 0) {
            initializeLocationSearch();
        } else {
            initializeLandmarkCapture(choice);
        }
    }
    public static String getName() {
        return binding.eventNameEt.getText().toString();
    }

    public static Long getDateEnd() {
        return dateEnd;
    }

    public static String getTimeEnd() {
        return timeEnd;
    }

    public static String getDescription() {
        return binding.eventNotesEt.getText().toString();
    }

    public static Long getDateStart() {
        return dateStart;
    }

    public static String getTimeStart() {
        return timeStart;
    }

    public static Place getPlace() {
        return place;
    }

    public static String getGoFundMeLink() {return goFundMeLink;}

    private void setUpEvents() {
        binding.eventDateEndEt.setOnClickListener(view -> {
            hideKeyboard(binding.eventDateEndEt);
            showDatePickerDialog();
        });

        binding.eventDateStartEt.setOnClickListener(view -> {
            hideKeyboard(binding.eventDateEndEt);
            showDatePickerDialog();
        });

        binding.eventLocationEt.setOnClickListener( view -> {
            LocationOptionDialogFragment dialogFragment = LocationOptionDialogFragment.newInstance();
            dialogFragment.show(getChildFragmentManager(), "");
        });

        binding.eventTimeEndEt.setOnClickListener( view -> {
            hideKeyboard(binding.eventTimeEndEt);
            showTimePickerDialog();
        });

        binding.eventTimeStartEt.setOnClickListener( view -> {
            hideKeyboard(binding.eventTimeEndEt);
            showTimePickerDialog();
        });

        binding.lay.setOnClickListener(view -> hideKeyboard(binding.lay));

        binding.eventNameEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        binding.eventNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {binding.eventNameLayout.setError("");}
        });

        binding.eventGofundLinkEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                binding.eventGofundmeLinkLayout.setError("");
            }
        });
    }

    private void setUpViews() {
        binding.eventLocationEt.setFocusable(false);
        binding.eventDateEndEt.setFocusable(false);
        binding.eventDateStartEt.setFocusable(false);
        binding.eventTimeEndEt.setFocusable(false);
        binding.eventTimeStartEt.setFocusable(false);
    }

    private void showTimePickerDialog() {
        RangeTimePickerDialogFragment rangeTimePickerDialogFragment = RangeTimePickerDialogFragment.newInstance();
        rangeTimePickerDialogFragment.show(getChildFragmentManager(), null);
    }

    private void initializeLocationSearch() {
        Places.initialize(requireContext(), BuildConfig.API_KEY);
        Places.createClient(requireContext());

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete
                .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_LOCATION_REQUEST_CODE);
    }

    private void initializeLandmarkCapture(int choice) {
        Intent intent = new Intent(requireActivity(), UploadLandmarkImageActivity.class);
        intent.putExtra("choice", choice);
        startActivityForResult(intent, LANDMARK_LOCATION_REQUEST_CODE);
    }

    private void showDatePickerDialog() {
        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker().build();
        datePicker.show(getChildFragmentManager(), "");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Log.i(TAG, selection.first + " " + selection.second);
            dateStart = (selection.first + 1000 * 60 * 60 * 24) / 1000;
            dateEnd = (selection.second + 1000 * 60 * 60 * 24) / 1000;

            binding.eventDateStartEt.setText(Utils.getDateFromTimeStamp(dateStart));
            binding.eventDateStartLayout.setError("");
            binding.eventDateEndEt.setText(Utils.getDateFromTimeStamp(dateEnd));
            binding.eventDateEndLayout.setError("");
        });

        datePicker.addOnNegativeButtonClickListener(view -> datePicker.dismiss());
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        binding.lay.requestFocus();
    }

    public static boolean hasInvalidLink() {
        boolean hasInvalidLink = false;
        if (binding.eventGofundLinkEt.getText().length() != 0
                && !Patterns.WEB_URL.matcher(binding.eventGofundLinkEt.getText().toString().toLowerCase()).matches()) {
            binding.eventGofundmeLinkLayout.setError("Please provide a valid link");
            hasInvalidLink = true;
        }
        return hasInvalidLink;
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

        if (binding.eventDateStartEt.getText().length() == 0) {
            binding.eventDateStartLayout.setError("Must provide a start date");
            binding.eventDateEndLayout.setError("Must provide an end date");
            hasAnEmptyField = true;
        }

        if (binding.eventTimeStartEt.getText().length() == 0) {
            binding.eventTimeStartLayout.setError("Must provide a start time");
            binding.eventTimeEndLayout.setError("Must provide an end time");
            hasAnEmptyField = true;
        }

        return hasAnEmptyField;
    }

    public static boolean hasInvalidTimeStamp() {
        Log.i(TAG, "In invalidTimes");
        if (dateEnd != null  && timeEnd != null && timeEnd.length() != 0) {
            Log.i(TAG, "dateStart: " + dateStart);
            Log.i(TAG, "dateEnddd: " + dateEnd);
            if (!String.valueOf(dateStart).equals(String.valueOf(dateEnd))) {
                return false;
            }
            Log.i(TAG, "In invalidTimes. Inside if");

            String[] timeEndParts = timeEnd.split(":");
            String hourEnd = timeEndParts[0];
            String minuteEnd = timeEndParts[1];

            if (minuteEnd.contains("PM")) {
                hourEnd = String.valueOf(Utils.convertToMilitaryTime(Integer.valueOf(hourEnd)));
            }

            String minuteEndMod = minuteEnd.replace(" AM", "");
            minuteEndMod = minuteEndMod.replace(" PM", "");
            hourEnd = hourEnd.length() == 2 ? hourEnd : "0" + hourEnd;

            String timeEndMod = hourEnd + ":" + minuteEndMod + ":" + "00";

            String[] timeStartParts = timeStart.split(":");
            String hourStart = timeStartParts[0];
            String minuteStart = timeStartParts[1];

            if (minuteStart.contains("PM")) {
                hourStart = String.valueOf(Utils.convertToMilitaryTime(Integer.valueOf(hourStart)));
            }

            String minuteStartMod = minuteStart.replace(" AM", "");
            minuteStartMod = minuteStartMod.replace(" PM", "");
            hourStart = hourStart.length() == 2 ? hourStart : "0" + hourStart;

            String timeStartMod = hourStart + ":" + minuteStartMod + ":" + "00";

            Long endTimeStamp = Utils.getTimeStampFromDate("09/20/2020" + " " + timeEndMod);
            Long startTimeStamp = Utils.getTimeStampFromDate("09/20/2020" + " " + timeStartMod);

            Log.i(TAG, "TimeEnd: " + timeEndMod);
            Log.i(TAG, "TimeStart: " + timeStartMod);
            Log.i(TAG, "EndTimeSt: " + endTimeStamp);
            Log.i(TAG, "StartTimeStam: " + startTimeStamp);

            //return true;
            return endTimeStamp < startTimeStamp;

            /*
            String timeEndMod = timeEnd.replace(" PM", "");
            timeEndMod = timeEndMod.replace(" AM", "");
            timeEndMod = timeEndMod.replace("\\s", "");
            String[] timeParts = timeEndMod.split(":");
            String hourEnd = timeParts[0];
            String minuteEnd = timeParts[1];

            hourEnd = String.valueOf(Utils.convertToMilitaryTime(Integer.valueOf(hourEnd)));
            hourEnd = hourEnd.length() == 2 ? hourEnd : "0" + hourEnd;*/


            /*
            String date = month + "/" + day + "/" + year;
            String time = hour + ":" + minute + ":" + "00";

            String timeStartMod = timeStart.replace(" PM", "");
            timeStartMod = timeStartMod.replace(" AM", "");
            timeStartMod = timeStartMod.replace("\\s", "");
            timeParts = timeStartMod.split(":");
            String hourStart = timeParts[0];
            String minuteStart = timeParts[1];


            String[] dateParts = dateEnd.split("/");
            String month = dateParts[0];
            String day = dateParts[1];
            String year = dateParts[2];

            month = month.length() == 2 ? month : "0" + month;
            day = day.length() == 2 ? day : "0" + day;

            Log.i(TAG,  " time is:" + timeEnd);
            String timeEndMod = timeEnd.replace(" PM", "");
            timeEndMod = timeEndMod.replace(" AM", "");
            timeEndMod = timeEndMod.replace("\\s", "");
            String[] timeParts = timeEndMod.split(":");
            String hour = timeParts[0];
            String minute = timeParts[1];

            hour = String.valueOf(Utils.convertToMilitaryTime(Integer.valueOf(hour)));
            hour = hour.length() == 2 ? hour : "0" + hour;

            String date = month + "/" + day + "/" + year;
            String time = hour + ":" + minute + ":" + "00";

            Long inputDateTimeStamp = Utils.getTimeStampFromDate(date + " " + time);
            Long currentTimeStamp = System.currentTimeMillis() / 1000;*/
        }
        return false;
    }
}
