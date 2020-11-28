package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.kingarthuralagao.us.civicengagement.data.Resource;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.now.AddNewEventNowFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.soon.AddNewEventSoonFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.EventsViewViewModel;
import com.gmail.kingarthuralagao.us.civilengagement.BuildConfig;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogAddNewEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getTimeStampFromDate;

public class AddNewEventDialogFragment extends DialogFragment {

    public static AddNewEventDialogFragment newInstance() {
        AddNewEventDialogFragment fragment = new AddNewEventDialogFragment();
        return fragment;
    }

    public final static String ADD_EVENT_NOW_FRAGMENT = "AddEventNow";
    public final static String ADD_EVENT_SOON_FRAGMENT = "AddEventSoon";
    private DialogAddNewEventBinding binding;
    private AddNewEventNowFragment addNewEventNowFragment;
    private AddNewEventSoonFragment addNewEventSoonFragment;
    private AddNewEventViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewEventNowFragment = AddNewEventNowFragment.newInstance();
        viewModel = new ViewModelProvider(this).get(AddNewEventViewModel.class);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddNewEventBinding.inflate(inflater, container, false);
        getChildFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), addNewEventNowFragment, ADD_EVENT_NOW_FRAGMENT)
                .commit();

        subscribeToLiveData();
        setUpEvents();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_Slide);
        }
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
                case ERROR:
                    //AddNewEventNowFragment.getEvent(responseResource.getData());
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    Log.i("AddNewEventDialog", "InAddNewEvent");
                    Event event = addNewEventNowFragment.getEvent(responseResource.getData());
                    postNewEvent(event);
                    break;
                default:
            }
        });
    }

    private void setUpEvents() {
        binding.backArrow.setOnClickListener(view -> dismiss());

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
            Place location = addNewEventNowFragment.getPlace();
            String locationInput = location.getLatLng().latitude + ", " + location.getLatLng().longitude;
            viewModel.getTimeZone(locationInput, System.currentTimeMillis() / 1000, BuildConfig.API_KEY);
            //postEvent();
            //getEvent();
        });
    }

    private void getEvent() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("events").document("events");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("AddEventDialogFragment", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("AddEventDialogFragment", "No such document");
                    }
                } else {
                    Log.d("AddEventDialogFragment", "get failed with ", task.getException());
                }
            }
        });

    }

    private void postNewEvent(Event event) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .document("Testing")
                .set(event)
                .addOnCompleteListener(task -> {
                    Toast.makeText(requireActivity(), "Success!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(requireActivity(), "Fail!", Toast.LENGTH_SHORT).show();
        });
    }
    private void postEvent() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String date1 = "11/22/2020 21:14:00";
        String date2 = "11/23/2020 21:14:00";
        Long dateStart = getTimeStampFromDate(date1);
        Long dateEnd = getTimeStampFromDate(date2);

        List<String> causes = new ArrayList<>();
        causes.add("School");
        causes.add("Climate");
        HashMap<String, Boolean> accessibilities = new HashMap<>();
        accessibilities.put("Curb cuts for wheelchair", true);
        accessibilities.put("Medic station with supplies", true);
        accessibilities.put("Medic station with trained staff", false);
        accessibilities.put("Easy access to seating", false);
        Event event1 = new Event("#SchoolStrike4Climate", dateStart, dateEnd, "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, causes, accessibilities, "123", "Cali");

        for(int i = 0; i < 10; i++) {
            String name = "event" + i;
            db.collection("events")
                    .document(name)
                    .set(event1)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(requireActivity(), "Success!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                Toast.makeText(requireActivity(), "Fail!", Toast.LENGTH_SHORT).show();
            });
        }
        db.collection("events")
                .document("event1")
                .set(event1)
                .addOnCompleteListener(task -> {
                    Toast.makeText(requireActivity(), "Success!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Fail!", Toast.LENGTH_SHORT).show();
                });
    }


    /****************************************** Code for making full screen Dialog *******************************/
    /*
    @NonNull
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }*/

    /****************************************** End of code for making full screen Dialog *******************************/
}
