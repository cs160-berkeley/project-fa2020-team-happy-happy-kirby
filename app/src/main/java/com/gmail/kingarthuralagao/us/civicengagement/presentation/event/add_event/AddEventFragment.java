package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.EventBuilder;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.now.AddNewEventNowFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.soon.AddNewEventSoonFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.viewmodel.AddNewEventViewModel;
import com.gmail.kingarthuralagao.us.civilengagement.BuildConfig;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogAddNewEventBinding;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;

public class AddEventFragment extends Fragment {


    public static AddEventFragment newInstance() {
        AddEventFragment fragment = new AddEventFragment();
        return fragment;
    }

    public final static String ADD_EVENT_NOW_FRAGMENT = "AddEventNow";
    public final static String ADD_EVENT_SOON_FRAGMENT = "AddEventSoon";
    private static Geocoder geocoder;
    private LoadingDialog loadingDialog;
    private DialogAddNewEventBinding binding;
    private AddNewEventNowFragment addNewEventNowFragment;
    private AddNewEventSoonFragment addNewEventSoonFragment;
    private AddNewEventViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewEventNowFragment = AddNewEventNowFragment.newInstance();
        viewModel = new ViewModelProvider(this).get(AddNewEventViewModel.class);

        //setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddNewEventBinding.inflate(inflater, container, false);
        getChildFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), addNewEventNowFragment, ADD_EVENT_NOW_FRAGMENT)
                .commit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeToLiveData();
        setUpEvents();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void subscribeToLiveData() {
        viewModel.getTimeZoneResponse.observe(this, responseResource -> {
            switch (responseResource.getStatus()) {
                case LOADING:
                    loadingDialog = LoadingDialog.newInstance("Processing Timezone");
                    loadingDialog.show(getChildFragmentManager(), "");
                    break;
                case ERROR:
                    initializeEventPost(TimeZone.getDefault().getDisplayName());
                    break;
                case SUCCESS:
                    String timeZoneName = responseResource.getData().getTimeZoneName();
                    initializeEventPost(timeZoneName);
                    break;
                default:
            }
        });

        viewModel.postEventResponse.observe(this, statusResource -> {
            switch (statusResource.getStatus()) {
                case ERROR:
                    loadingDialog.dismiss();
                    Toasty.error(requireContext(), "Error adding event", Toast.LENGTH_LONG, true).show();
                    break;
                case SUCCESS:
                    loadingDialog.dismiss();
                    //Toasty.success(requireContext(), "Event successfully posted!", Toast.LENGTH_LONG, true).show();
                    Toasty.custom(requireContext(),
                            "Event successfully added!",
                            getResources().getDrawable(R.drawable.ic_check_white, null),
                            getResources().getColor(R.color.secondary_blue, null),
                            getResources().getColor(R.color.white, null),
                            Toast.LENGTH_LONG,
                            true,
                            true).show();
                    requireActivity().finish();
                    break;
                case LOADING:
                    loadingDialog.setLoadingText("Posting Event");
                default:
            }
        });
    }

    private void setUpEvents() {
        setAccessibilityCheckBoxListener();
        binding.backArrow.setOnClickListener(view -> requireActivity().finish());

        binding.includeHappeningNowHappeningSoon.happeningNowBtn.setOnClickListener(view -> {
            getChildFragmentManager()
                    .beginTransaction()
                    .hide(addNewEventSoonFragment)
                    .show(addNewEventNowFragment)
                    .commit();
            binding.includeHappeningNowHappeningSoon.happeningNowBtn.setEnabled(false);
            binding.includeHappeningNowHappeningSoon.happeningSoonBtn.setEnabled(true);
        });

        binding.includeHappeningNowHappeningSoon.happeningSoonBtn.setOnClickListener(view -> {
            if (addNewEventSoonFragment == null) {
                addNewEventSoonFragment = AddNewEventSoonFragment.newInstance();

                getChildFragmentManager()
                        .beginTransaction()
                        .hide(addNewEventNowFragment)
                        .add(binding.fragmentContainer.getId(), addNewEventSoonFragment, ADD_EVENT_SOON_FRAGMENT)
                        .commit();
            } else {
                getChildFragmentManager()
                        .beginTransaction()
                        .hide(addNewEventNowFragment)
                        .show(addNewEventSoonFragment)
                        .commit();
            }
            binding.includeHappeningNowHappeningSoon.happeningNowBtn.setEnabled(true);
            binding.includeHappeningNowHappeningSoon.happeningSoonBtn.setEnabled(false);
        });

        binding.addEventBtn.setOnClickListener(view -> {
            Place eventLocation;
            if (binding.includeHappeningNowHappeningSoon.happeningSoonBtn.isEnabled()) { // In addneweventnow
                boolean incompleteInput = false;
                if (addNewEventNowFragment.hasEmptyField())
                    incompleteInput = true;

                if (addNewEventNowFragment.hasInvalidLink())
                    incompleteInput = true;

                if (hasMissingCause())
                    incompleteInput = true;

                if (incompleteInput) {
                    return;
                }

                if (addNewEventNowFragment.hasInvalidTimeStamp()) {
                    Toasty.error(requireContext(), "Please enter a valid time range", Toasty.LENGTH_LONG, true).show();
                    return;
                }

                eventLocation = addNewEventNowFragment.getPlace();
            } else { // In addneweventsoon
                Log.i("AddEventFrag", "InAddeventsoon");
                boolean incompleteInput = false;
                if (addNewEventSoonFragment.hasEmptyField())
                    incompleteInput = true;

                if (addNewEventSoonFragment.hasInvalidLink())
                    incompleteInput = true;

                if (hasMissingCause())
                    incompleteInput = true;

                if (incompleteInput) {
                    return;
                }

                if (addNewEventSoonFragment.hasInvalidTimeStamp()) {
                    Toasty.error(requireContext(), "Please enter a valid time range", Toasty.LENGTH_LONG, true).show();
                    return;
                }

                eventLocation = addNewEventSoonFragment.getPlace();
            }
            String locationInput = eventLocation.getLatLng().latitude + ", " + eventLocation.getLatLng().longitude;
            viewModel.getTimeZone(locationInput, System.currentTimeMillis() / 1000, BuildConfig.API_KEY);
        });
    }

    private void setAccessibilityCheckBoxListener() {
        binding.includeAccessibilityCheckboxes.layAccessibilityOne.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilityOneCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilityOneCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilityOneCheckbox.setChecked(true);
            }
        });

        binding.includeAccessibilityCheckboxes.layAccessibilityTwo.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilityTwoCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilityTwoCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilityTwoCheckbox.setChecked(true);
            }
        });

        binding.includeAccessibilityCheckboxes.layAccessibilityThree.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilityThreeCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilityThreeCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilityThreeCheckbox.setChecked(true);
            }
        });

        binding.includeAccessibilityCheckboxes.layAccessibilityFour.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilityFourCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilityFourCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilityFourCheckbox.setChecked(true);
            }
        });

        binding.includeAccessibilityCheckboxes.layAccessibilityFive.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilityFiveCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilityFiveCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilityFiveCheckbox.setChecked(true);
            }
        });

        binding.includeAccessibilityCheckboxes.layAccessibilitySix.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilitySixCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilitySixCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilitySixCheckbox.setChecked(true);
            }
        });

        binding.includeAccessibilityCheckboxes.layAccessibilitySeven.setOnClickListener(view -> {
            if (binding.includeAccessibilityCheckboxes.accessibilitySevenCheckbox.isChecked()) {
                binding.includeAccessibilityCheckboxes.accessibilitySevenCheckbox.setChecked(false);
            } else {
                binding.includeAccessibilityCheckboxes.accessibilitySevenCheckbox.setChecked(true);
            }
        });
    }

    private void initializeEventPost(String timeZoneName) {
        String timeZone = timeZoneName;

        if (timeZoneName == null || timeZoneName.length() == 0)
            timeZone = TimeZone.getDefault().getDisplayName();

        if (binding.includeHappeningNowHappeningSoon.happeningSoonBtn.isEnabled()) // In addneweventnow
            createEventHappeningNow(timeZone);
        else  // In addneweventsoon
            createEventHappeningSoon(timeZone);
    }

    private boolean hasMissingCause() {
        List<String> causes = getCauses();
        if (causes.isEmpty() || (causes.contains("Other") && causes.size() <= 1)) {
            binding.includeCausesChips.causesPrompt.setText(setErrorText());
            return true;
        } else {
            binding.includeCausesChips.causesPrompt.setText(setRegularText());
            return false;
        }
    }

    private void createEventHappeningNow(String timeZoneName) {
        String eventName = addNewEventNowFragment.getName();
        String eventDescription = addNewEventNowFragment.getDescription();
        String eventDateEnd = addNewEventNowFragment.getDateEnd();
        String eventTimeEnd = addNewEventNowFragment.getTimeEnd();
        String eventGoFundMeLink = addNewEventNowFragment.getGoFundMeLink();

        Place place = addNewEventNowFragment.getPlace();
        String address = place.getAddress();
        String locationName = place.getName() == null ? "" : place.getName();
        String eventLocation = locationName.length() == 0 ? place.getAddress() : locationName + "\n" + address;

        EventBuilder eventBuilder = new EventBuilder();

        eventBuilder
                .withName(eventName)
                .withDateStart(System.currentTimeMillis() / 1000)
                .withDateEnd(eventDateEnd, eventTimeEnd)
                .withTimeStart("10:00")
                .withTimeEnd(eventTimeEnd)
                .withCity(getCityFromPlace(place))
                .withDescription(eventDescription)
                .withLocation(eventLocation)
                .withTimeZone(timeZoneName)
                .withCheckIns()
                .withCauses(getCauses())
                .withAccessibilities(getAccessibilities())
                .withEventID()
                .withGoFundMeLink(eventGoFundMeLink)
                .build();

        viewModel.postEvent(eventBuilder.getEventDTO());
    }

    private void createEventHappeningSoon(String timeZoneName) {
        String eventName = addNewEventSoonFragment.getName();
        String eventDescription = addNewEventSoonFragment.getDescription();
        Long eventDateEnd = addNewEventSoonFragment.getDateEnd();
        Long eventDateStart = addNewEventSoonFragment.getDateStart();
        String eventTimeEnd = addNewEventSoonFragment.getTimeEnd();
        String eventTimeStart = addNewEventSoonFragment.getTimeStart();
        String eventGoFundMeLink = addNewEventSoonFragment.getGoFundMeLink();

        Place place = addNewEventSoonFragment.getPlace();
        String address = place.getAddress();
        String locationName = place.getName() == null ? "" : place.getName();
        String eventLocation = locationName.length() == 0 ? place.getAddress() : locationName + "\n" + address;

        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder
                .withName(eventName)
                .withDateStart(eventDateStart)
                .withDateEnd(eventDateEnd)
                .withTimeStart(eventTimeStart)
                .withTimeEnd(eventTimeEnd)
                .withCity(getCityFromPlace(place))
                .withDescription(eventDescription)
                .withLocation(eventLocation)
                .withTimeZone(timeZoneName)
                .withCheckIns()
                .withCauses(getCauses())
                .withAccessibilities(getAccessibilities())
                .withEventID()
                .withGoFundMeLink(eventGoFundMeLink)
                .withKeywords(generateSearchKeywords(eventName))
                .build();

        viewModel.postEvent(eventBuilder.getEventDTO());
    }

    private Map<String, Boolean> getAccessibilities() {
        Map<String, Boolean> accessibilities = new HashMap<>();

        if (binding.includeAccessibilityCheckboxes.accessibilityOneCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_one), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_one), false);
        }

        if (binding.includeAccessibilityCheckboxes.accessibilityTwoCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_two), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_two), false);
        }

        if (binding.includeAccessibilityCheckboxes.accessibilityThreeCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_three), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_three), false);
        }

        if (binding.includeAccessibilityCheckboxes.accessibilityFourCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_four), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_four), false);
        }

        if (binding.includeAccessibilityCheckboxes.accessibilityFiveCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_five), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_five), false);
        }

        if (binding.includeAccessibilityCheckboxes.accessibilitySixCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_six), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_six), false);
        }

        if (binding.includeAccessibilityCheckboxes.accessibilitySevenCheckbox.isChecked()) {
            accessibilities.put(getResources().getString(R.string.accessibility_seven), true);
        } else {
            accessibilities.put(getResources().getString(R.string.accessibility_seven), false);
        }
        return accessibilities;
    }

    private List<String> getCauses() {
        List<String> causes = new ArrayList<>();
        List<Integer> checkedChipsID = binding.includeCausesChips.chipGroup.getCheckedChipIds();

        for (Integer resourceID : checkedChipsID) {
            Chip chip = binding.includeCausesChips.chipGroup.findViewById(resourceID);
            causes.add(chip.getText().toString());
        }

        if (causes.contains(getResources().getString(R.string.other)) && binding.otherEt.getText().length() != 0) {
            String[] causesInput = binding.otherEt.getText().toString().split(", ");

            for (int i = 0; i < causesInput.length; i++) {
                causes.add(causesInput[i]);
            }
        }
        return causes;
    }

    private static String getCityFromPlace(Place place) {
        geocoder = new Geocoder(CivicEngagementApp.getContext(), Locale.getDefault());

        try {
            String city = getCityNameByCoordinates(place.getLatLng().latitude, place.getLatLng().longitude);
            if(city.length() == 0) {
                String address = place.getAddress();
                int commaIndex = address.indexOf(",");
                city = address.substring(0, commaIndex);
            }
            return city;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getCityNameByCoordinates(double lat, double lon) throws IOException {

        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            Log.i("AddNewEventDialogFrag", addresses.toString());
            return addresses.get(0).getLocality() == null ? addresses.get(0).getAdminArea() : addresses.get(0).getLocality();
        }
        return "";
    }

    private Spannable setErrorText() {
        String text = "What causes does the event support? (Select all that apply*). Must specify a cause";

        Spannable spannable = new SpannableString(text);

        int dotIndex = text.indexOf(".");

        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_text, null)),
                0,
                (dotIndex + 1),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(Color.RED), (dotIndex + 1), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    private Spannable setRegularText() {
        String text = "What causes does the event support? (Select all that apply*).";

        Spannable spannable = new SpannableString(text);

        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_text, null)),
                0,
                text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    private List<String> generateSearchKeywords(String eventName) {
        String lowerCase = eventName.toLowerCase();
        List<String> keywords = new ArrayList<>();

        String[] splits = lowerCase.split(" ");

        for (int i = 0; i < splits.length; i++) {
            String appendString = "";

            for (int j = 0; j < lowerCase.length(); j++) {
                Character c = lowerCase.charAt(j);
                appendString += c.toString();
                keywords.add(appendString);
                Log.i("AddEventFrag", appendString);
            }

            lowerCase = lowerCase.replace(splits[i] + " ", "");
        }

        return keywords;
    }
}
