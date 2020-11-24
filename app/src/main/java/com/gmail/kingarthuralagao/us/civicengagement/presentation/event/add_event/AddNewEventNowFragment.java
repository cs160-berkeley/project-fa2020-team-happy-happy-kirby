package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civilengagement.databinding.IncludeAddEventHappeningNowBinding;

import java.util.Date;

public class AddNewEventNowFragment extends Fragment {

    public static AddNewEventNowFragment newInstance() {
        AddNewEventNowFragment fragment = new AddNewEventNowFragment();
        return fragment;
    }

    private IncludeAddEventHappeningNowBinding binding;
    private DatePickerFragment datePickerFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datePickerFragment = DatePickerFragment.newInstance();
        Date selectedDate = datePickerFragment.getDate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IncludeAddEventHappeningNowBinding.inflate(inflater, container, false);
        setUpEvents();
        return binding.getRoot();
    }

    private void setUpEvents() {
        // Present user with a calendar to select a date from.
        binding.eventEndLayout.setOnClickListener(view -> {
            datePickerFragment.showDatePickerDialog(view);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
